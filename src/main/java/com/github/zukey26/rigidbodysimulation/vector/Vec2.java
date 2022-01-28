package com.github.zukey26.rigidbodysimulation.vector;

import com.github.zukey26.rigidbodysimulation.util.Util;

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

    public void set(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    public Vec2 add(Vec2 other)
    {
        return new Vec2(other.getX() + this.x,other.getY()+this.y);
    }
    public Vec2 sub(Vec2 other)
    {
        return new Vec2(this.x - other.getX(), this.y - other.getY());
    }
    public Vec2 mul(Vec2 other)
    {
        return new Vec2(this.x * other.getX(), this.y * other.getY());
    }
    public Vec2 mul(double other)
    {
        return new Vec2(this.x * other, this.y * other);
    }

    public Vec2 clamp(Vec2 lower, Vec2 upper)
    {
        return new Vec2(Util.clamp(lower.getX(),upper.getX(),this.x),Util.clamp(lower.getY(),upper.getY(),this.y));
    }


    public double dot(Vec2 other)
    {
        return (this.x * other.getX()) + (this.y * other.getY());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vec2 vec2 = (Vec2) o;

        if (Double.compare(vec2.x, x) != 0) return false;
        return Double.compare(vec2.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
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

    @Override
    public String toString() {
        return "Vec2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
