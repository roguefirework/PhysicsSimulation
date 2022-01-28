package com.github.zukey26.rigidbodysimulation.bodies.Shape;

import com.github.zukey26.rigidbodysimulation.vector.Vec2;

public class Circle extends Collider{
    float radius;
    Vec2 position;

    public Circle(float radius, Vec2 position) {
        this.radius = radius;
        this.position = position;
    }


    public boolean collidesOther(Circle other)
    {
        float r = this.radius + other.getRadius();
        r *= r;
        return r < ((position.getX() + other.getPosition().getX()) + (position.getY() + other.getPosition().getY()));
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Vec2 getPosition() {
        return position;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }

}
