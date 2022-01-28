package com.github.zukey26.rigidbodysimulation.bodies.Shape;

import com.github.zukey26.rigidbodysimulation.vector.Vec2;

public abstract class Shape {
    public Vec2 position;
    abstract public Collider getCollider();
    abstract public float ComputeMass(float density);
    abstract public void Render();
}
