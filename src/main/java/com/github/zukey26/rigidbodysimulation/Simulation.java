package com.github.zukey26.rigidbodysimulation;



import com.github.zukey26.rigidbodysimulation.bodies.Shape.Circle;
import com.github.zukey26.rigidbodysimulation.bodies.data.Material;
import com.github.zukey26.rigidbodysimulation.bodies.body.Body;
import com.github.zukey26.rigidbodysimulation.commands.Command;
import com.github.zukey26.rigidbodysimulation.commands.CommandWatcher;
import com.github.zukey26.rigidbodysimulation.util.Pair;
import com.github.zukey26.rigidbodysimulation.util.Util;
import com.github.zukey26.rigidbodysimulation.vector.Vec2;
import com.github.zukey26.rigidbodysimulation.collider.CircleCollider;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.opengl.GL11.*;
/**
 * Main simulation class. Runs off of this.
 */
public final class Simulation {
    static final float fps = 120;
    static final float dt = 1 /fps;
    static float accumulator = 0;
    long frameStartTime;
    static Vec2 gravity = new Vec2(0,0);
    static ArrayList<Manifold> collided = new ArrayList<>();
    public static ArrayList<Body> bodies = new ArrayList<>();
    public static AtomicBoolean running = new AtomicBoolean();
    public static CommandWatcher watcher = new CommandWatcher();
    public Simulation()
    {
        running.set(true);
        watcher.start();
        //Get time in milliseconds since jan 1 1970
        frameStartTime = System.currentTimeMillis();

        for (int i = -5; i < 5; i++) {
            for (int j = -5; j < 5; j++) {
                bodies.add(new Body(new Circle(0.25f,new Vec2(i,j)),Material.BOUNCYBALL,new Vec2(0,0),new Vec2(0,0)));
            }
        }

        bodies.add(new Body(new Circle(1f,new Vec2(7,0)),Material.ROCK,new Vec2(0,0),new Vec2(-5,0)));
        while(!Main.window.shouldClose())
        {
            while(!watcher.commands.isEmpty())
            {
                String[] command = watcher.commands.poll();
                for (Command c : Command.commands) {
                    if (c.name.toLowerCase().equals(command[0].toLowerCase())) {
                        c.execute(Arrays.copyOfRange(command, 1, command.length));
                    }
                }
            }
            long currentTime  = System.currentTimeMillis();
            accumulator += (currentTime - frameStartTime)/1000F;
            //Store this start time for the next frame
            frameStartTime = currentTime;
            //Avoid a spiral of death on slow machines by limiting the amount of physics steps can be called in a loop
            if(accumulator > 0.2f)
            {
                accumulator = 0.2f;
            }
            while(accumulator > dt)
            {
                doPhysicsStep(dt);
                accumulator -= dt;
            }

            renderGame(accumulator/dt);

        }
        running.set(false);
    }



    /**
     * Helper class with collision routines
     */
    static final class CollisionRoutines
    {
        public static Manifold CircleCorrectCircle(Body circle1, Body circle2)
        {
            //vector from 1 -> 2
            Vec2 normal = circle2.shape.position.sub(circle1.shape.position);
            double penetration;
            float r = ((CircleCollider)circle1.shape.getCollider()).getRadius() + ((CircleCollider)circle1.shape.getCollider()).getRadius();
            float d = (float) normal.length();



            // If distance between circles is not zero
            if(d != 0)
            {
                // Distance is difference between radius and distance
                penetration = r - d;

                // Utilize our d since we performed sqrt on it already within Length( )
                // Points from A to B, and is a unit vector
                normal = normal.mul(1/d);

            }

            // Circles are on same position
            else
            {
                // Choose random (but consistent) values
                penetration = ((CircleCollider)circle1.shape.getCollider()).getRadius();
                normal = new Vec2( 1, 0 );
            }
            return new Manifold(circle1,circle2,normal,penetration);
        }



    }
    /**
     * Manifold class for collisions
     */
    static class Manifold
    {
        Body a;
        Body b;
        Vec2 normal;
        double penetrationDepth;
        public void resolveCollision()
        {
            // Calculate relative velocity
            Vec2 rv = b.velocity.sub(a.velocity);

            // Calculate relative velocity in terms of the normal direction
            double velAlongNormal = rv.dot(normal);

            // Do not resolve if velocities are separating
            if(velAlongNormal > 0) {
                return;
            }
            // Calculate restitution
            float e = Math.min(a.material.restitution, b.material.restitution);

            // Calculate impulse scalar
            double j = -(1 + e) * velAlongNormal;
            j /= a.getMassData().invMass + b.getMassData().invMass;

            // Apply impulse
            Vec2 impulse = normal.mul(j);
            a.velocity = a.velocity.sub(impulse.mul(a.getMassData().invMass));
            b.velocity = b.velocity.add(impulse.mul(b.getMassData().invMass));
        }
        void PositionalCorrection()
        {
            /*
            final float k_slop = 0.1f; // Penetration allowance
            final float percent = 0.5f; // Penetration percentage to correct
            Vec2 correction = normal.mul((Math.max(penetrationDepth - k_slop, 0.0f ) / (a.getMassData().invMass + b.getMassData().invMass))).mul(percent);
            a.shape.position = a.shape.position.sub(correction.mul(a.getMassData().invMass));
            b.shape.position = b.shape.position.add(correction.mul(b.getMassData().invMass));
            */

        }



        public Body getA() {
            return a;
        }

        public void setA(Body a) {
            this.a = a;
        }

        public Body getB() {
            return b;
        }

        public void setB(Body b) {
            this.b = b;
        }

        public Vec2 getNormal() {
            return normal;
        }

        public void setNormal(Vec2 normal) {
            this.normal = normal;
        }

        public double getPenetrationDepth() {
            return penetrationDepth;
        }

        public void setPenetrationDepth(double penetrationDepth) {
            this.penetrationDepth = penetrationDepth;
        }

        public Manifold(Body a, Body b, Vec2 normal, double penetrationDepth) {
            this.a = a;
            this.b = b;
            this.normal = normal;
            this.penetrationDepth = penetrationDepth;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Manifold manifold = (Manifold) o;

            if (Double.compare(manifold.penetrationDepth, penetrationDepth) != 0) return false;
            if (a != null ? !a.equals(manifold.a) : manifold.a != null) return false;
            if (b != null ? !b.equals(manifold.b) : manifold.b != null) return false;
            return normal != null ? normal.equals(manifold.normal) : manifold.normal == null;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = a != null ? a.hashCode() : 0;
            result = 31 * result + (b != null ? b.hashCode() : 0);
            result = 31 * result + (normal != null ? normal.hashCode() : 0);
            temp = Double.doubleToLongBits(penetrationDepth);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }


    void doPhysicsStep(float dt) {
        collided.clear();
        LinkedList<Pair> done = new LinkedList<>();

        for (int i = 0; i < bodies.size(); i++) {
            Body a = bodies.get(i);
            for (int j = 0; j < bodies.size(); j++) {
                Body b = bodies.get(j);
                if(i == j)
                {
                    continue;
                }
                if (checkForCollision(a, b)) {
                    collided.add(calculateColision(a, b));
                    done.add(new Pair(i, j));
                }
            }
        }
        collided = new ArrayList<>(new HashSet<>(collided));

        for (Body body : bodies) {
            integrateForces(body, dt);
        }
        for (Manifold manifold : collided) {
            manifold.resolveCollision();
        }
        // Integrate velocities
        for (Body body : bodies) {
            integrateVelocity(body, dt);
        }
        // Correct positions
        for (Manifold manifold : collided) {
            manifold.PositionalCorrection();
        }
        // Clear all forces
        for (Body b : bodies) {
            b.force.set(0, 0);
        }
    }

    private Manifold calculateColision(Body a, Body b) {
        if (a.shape.getCollider() instanceof CircleCollider) {
            if (b.shape.getCollider() instanceof CircleCollider) {
                return CollisionRoutines.CircleCorrectCircle(a, b);
            }
        }
        return null;
    }

    private boolean checkForCollision(Body a, Body b) {

        if(a.shape.getCollider() instanceof CircleCollider) {
            if (b.shape.getCollider() instanceof CircleCollider) {
                return ((CircleCollider) a.shape.getCollider()).collidesOther((CircleCollider) b.shape.getCollider());
            }
        }
        return false;
    }

    void integrateForces( Body b, float dt )
    {
        if(b.getMassData().invMass == 0.0f)
            return;
        Vec2 force = b.force.mul(b.getMassData().invMass).add(gravity).mul((dt / 2.0f));
        b.velocity = b.velocity.add(force);
    }

    void integrateVelocity( Body b, float dt )
    {
        if(b.getMassData().invMass == 0.0f)
            return;

        b.shape.position = b.shape.position.add(b.velocity.mul(dt));
        integrateForces( b, dt );
    }


    /**
     * Renders the screen of the game
     * @param alpha number for interpolation of shapes -does nothing at the moment
     */
    void renderGame(float alpha)
    {
        glClear(GL_COLOR_BUFFER_BIT);

        for (Body body: bodies) {
            body.shape.Render();
        }
        Main.window.update();
        Main.window.swapBuffers();
    }
}
