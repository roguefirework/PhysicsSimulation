package com.github.zukey26.rigidbodysimulation.collider;

import com.github.zukey26.rigidbodysimulation.bodies.Shape.Collider;
import com.github.zukey26.rigidbodysimulation.vector.Vec2;

public class CircleCollider extends Collider {
    float radius;
    Vec2 position;

    public CircleCollider(float radius, Vec2 position) {
        this.radius = radius;
        this.position = position;
    }


    public boolean collidesOther(CircleCollider other)
    {
        float radiusLengths = this.radius + other.getRadius();
        return radiusLengths > this.position.sub(other.position).length();
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
