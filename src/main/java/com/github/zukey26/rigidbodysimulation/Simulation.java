package com.github.zukey26.rigidbodysimulation;


import com.github.zukey26.rigidbodysimulation.bodies.Shape.AABB;
import com.github.zukey26.rigidbodysimulation.bodies.Shape.Circle;
import com.github.zukey26.rigidbodysimulation.bodies.body.Body;
import com.github.zukey26.rigidbodysimulation.util.Util;
import com.github.zukey26.rigidbodysimulation.vector.Vec2;

import java.util.Objects;

/**
 * Main simulation class. Runs off of this.
 */
public final class Simulation {
    static final float fps = 60;
    static final float dt = 1 /fps;
    static float accumulator = 0;
    static long frameStartTime;
    public Simulation()
    {
        //Get time in milliseconds since jan 1 1970
        frameStartTime = System.currentTimeMillis();
        while(running)
        {
            long currentTime  = System.currentTimeMillis();
            accumulator += (float)(currentTime /1000L)- (float)(frameStartTime /1000L);
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
    }

    void doPhysicsStep(float dt)
    {

    }

    /**
     * Helper class with collision routines
     */
    static final class CollisionRoutines
    {
        public static Manifold CircleCorrectCircle(Body circle1, Body circle2)
        {
            //vector from 1 -> 2
            Vec2 normal = circle2.shape.position.sub(circle2.shape.position);
            double penetration;
            float r = ((Circle)circle1.shape.getCollider()).getRadius() + ((Circle)circle1.shape.getCollider()).getRadius();
            r *= r;
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
                penetration = ((Circle)circle1.shape.getCollider()).getRadius();
                normal = new Vec2( 1, 0 );
            }
            return new Manifold(circle1,circle2,normal,penetration);
        }
        public static Manifold AABBCorrectABBB(Body a, Body b)
        {

            // Vector from A to B
            Vec2 normal = b.shape.position.sub(a.shape.position);
            double penetration;
            AABB abox = (AABB) a.shape.getCollider();
            AABB bbox = (AABB) b.shape.getCollider();

            // Calculate half extents along x axis for each object
            double aExtent = (abox.getMax().getX() - abox.getMin().getX()) / 2;
            double bExtent = (bbox.getMax().getX() - bbox.getMin().getX()) / 2;

            // Calculate overlap on x axis
            double xOverlap = aExtent + bExtent - Math.abs( normal.getX());
            // Calculate half extents along x axis for each object
            aExtent = (abox.getMax().getY() - abox.getMin().getY()) / 2;
            bExtent = (bbox.getMax().getY() - bbox.getMin().getY()) / 2;
            // Calculate overlap on y axis
            double yOverlap = aExtent + bExtent - Math.abs(normal.getY());

            // Find out which axis is axis of least penetration
            if(xOverlap > yOverlap)
            {
                if(normal.getX() < 0) {
                    normal = new Vec2(-1, 0);
                }
                else {
                    normal = new Vec2(1, 0);
                }
                penetration = xOverlap;
            }
            else
            {
                if(normal.getY() < 0) {
                    normal = new Vec2(0, -1);
                }
                else {
                    normal = new Vec2(0, 1);
                }
                penetration = yOverlap;
            }
            return new Manifold(a,b,normal,penetration);
        }

        /**
         * AABB Correcting a circle
         * @param a colider extends AABB
         * @param b colider extends Circle
         * @return colision data
         */


        public Manifold AABBCorrectsCircle(Body a, Body b)
        {
            Vec2 n = b.shape.position.sub(a.shape.position);

            // Closest point on A to center of B
            Vec2 closest = n;

            // Calculate half extents along each axis
            double xExtent = (((AABB)a.shape.getCollider()).getMax().getX() - ((AABB)a.shape.getCollider()).getMin().getX()) / 2;
            double yExtent = (((AABB)a.shape.getCollider()).getMax().getY() - ((AABB)a.shape.getCollider()).getMin().getY()) / 2;

            // Clamp point to edges of the AABB
            closest.setX(Util.clamp(-xExtent, xExtent, closest.getX()));
            closest.setY(Util.clamp(-yExtent, yExtent, closest.getY()));

            boolean inside = false;

            // Circle is inside the AABB, so we need to clamp the circle's center
            // to the closest edge
            if(n == closest)
            {
                inside = true;

                // Find closest axis
                if(Math.abs( n.getY() ) > Math.abs( n.getY() ))
                {
                    // Clamp to closest extent
                    if(closest.getX() > 0) {
                        closest.setX(xExtent);
                    }
                    else {
                        closest.setX(-xExtent);
                    }
                }
                // y axis is shorter
                else
                {
                    // Clamp to closest extent
                    if(closest.getY() > 0) {
                        closest.setY(yExtent);
                    }
                    else {
                        closest.setY(-yExtent);
                    }
                }
            }

            Vec2 normal = n.sub(closest);
            double penetration;
            double d = normal.length();
            double r = ((Circle)b.shape.getCollider()).getRadius();

            // Early out of the radius is shorter than distance to closest point and
            // Circle not inside the AABB
            if(d > r * r && !inside) {
                return null;
            }
            // Collision normal needs to be flipped to point outside if circle was
            // inside the AABB
            if(inside)
            {
                normal = normal.mul(-1);
                penetration = r - d;
            }
            else
            {
                normal = n;
                penetration = r - d;
            }

            return new Manifold(a,b,normal,penetration);
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
        public void ResolveCollision()
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
            final float k_slop = 0.05f; // Penetration allowance
            final float percent = 0.4f; // Penetration percentage to correct
            Vec2 correction = normal.mul((Math.max(penetrationDepth - k_slop, 0.0f ) / (a.getMassData().invMass + b.getMassData().invMass))).mul(percent);
            a.shape.position = a.shape.position.sub(correction.mul(a.getMassData().invMass));
            b.shape.position = b.shape.position.add(correction.mul(b.getMassData().invMass));
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
    }


    /**
     * Renders the screen of the game
     * @param alpha number for interpolation of shapes
     */
    void renderGame(float alpha)
    {

    }
}
