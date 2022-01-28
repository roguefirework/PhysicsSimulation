package com.github.zukey26.rigidbodysimulation.bodies.Shape;

import com.github.zukey26.rigidbodysimulation.collider.CircleCollider;
import com.github.zukey26.rigidbodysimulation.util.Util;
import com.github.zukey26.rigidbodysimulation.vector.Vec2;

public class Circle extends Shape{
    public float radius;
    @Override
    public Collider getCollider() {
        return new CircleCollider(radius,position);
    }

    @Override
    public float ComputeMass(float density) {
        return (float) (Math.PI *(this.radius* this.radius) * density);
    }

    @Override
    public void Render() {
        Util.DrawCircle(position.getX(),position.getY(),radius,60);
    }

    public Circle(float radius, Vec2 position) {
        super(position);
        this.radius = radius;

    }
}
