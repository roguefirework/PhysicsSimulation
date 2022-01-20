package com.github.zukey26.rigidbodysimulation.vector;

public class Vec2 {
    private double x;
    private double y;

    /**
     * 2 dimensional vector
     * @param x x position
     * @param y y position
     */
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Vec2(Vec2 copy)
    {
        this.x = copy.getX();
        this.y = copy.getY();
    }


    public Vec2 add(Vec2 other)
    {
        return new Vec2(other.getX() + this.x,other.getY()+this.y);
    }
    public static Vec2 add(Vec2 first, Vec2 other)
    {
        return first.add(other);
    }
    public double length()
    {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }
    public void normalize()
    {
        x = this.x / this.length();
        y = this.y / this.length();
    }
    public static Vec2 normalize(Vec2 other)
    {
        Vec2 vec = new Vec2(other);
        vec.normalize();
        return vec;
    }




    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
