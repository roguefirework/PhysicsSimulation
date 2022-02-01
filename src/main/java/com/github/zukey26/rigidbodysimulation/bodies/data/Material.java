package com.github.zukey26.rigidbodysimulation.bodies.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Material {
    public static Material ROCK = new Material(0.5f,0.1f,"rock");
    public static Material BOUNCYBALL = new Material(0.3f,0.8f,"ball");
    public static List<Material> materials = Arrays.asList(ROCK,BOUNCYBALL);
    public float density;
    public float restitution;
    public String name;
    public Material(float density, float restitution,String name) {
        this.density = density;
        this.restitution = restitution;
        this.name = name;
    }
}
