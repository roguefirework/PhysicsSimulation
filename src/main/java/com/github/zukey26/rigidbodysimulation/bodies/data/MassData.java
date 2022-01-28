package com.github.zukey26.rigidbodysimulation.bodies.data;

public class MassData {
    public float mass;
    public float invMass;

    public MassData(float mass) {
        this.mass = mass;
        this.invMass = 0;
        if(this.mass != 0)
        {
            this.invMass = 1/mass;
        }
    }
}
