package com.github.zukey26.rigidbodysimulation.bodies.data;

public class MassData {
    public float mass;
    public float invMass;

    public MassData(float mass) {
        this.mass = mass;
        this.invMass = mass == 0 ? 1 / mass : 0;
    }
}
