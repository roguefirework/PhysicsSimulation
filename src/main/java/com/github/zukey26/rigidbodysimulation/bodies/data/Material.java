package com.github.zukey26.rigidbodysimulation.bodies.data;

public class Material {
    public static Material ROCK = new Material(0.5f,0.1f);
    public static Material IMMOVABLE = new Material(0,0.1f);
    public float density;
    public float restitution;

    public Material(float density, float restitution) {
        this.density = density;
        this.restitution = restitution;
    }
}
