package com.github.zukey26.rigidbodysimulation.bodies.body;

import com.github.zukey26.rigidbodysimulation.bodies.Shape.Shape;
import com.github.zukey26.rigidbodysimulation.bodies.data.MassData;
import com.github.zukey26.rigidbodysimulation.bodies.data.Material;
import com.github.zukey26.rigidbodysimulation.vector.Vec2;

public class Body {
    public Shape shape;
    public Material material;
    public MassData getMassData()
    {
        return new MassData(shape.ComputeMass(material.density));
    }
    public Vec2 force;
    public Vec2 velocity;

}
