package com.github.zukey26.rigidbodysimulation.bodies.Shape;

import com.github.zukey26.rigidbodysimulation.vector.Vec2;

public class AABB extends Collider{
    Vec2 min;
    Vec2 max;

    public AABB(Vec2 min, Vec2 max) {
        this.min = min;
        this.max = max;
    }


    public boolean intersects(AABB other)
    {
        if(this.getMax().getX() < other.getMin().getX() || this.min.getX() > other.getMax().getX())
        {
            return false;
        }
        return !(this.getMax().getY() < other.getMin().getY()) && !(this.min.getY() > other.getMax().getY());
    }



    public Vec2 getMin() {
        return min;
    }

    public void setMin(Vec2 min) {
        this.min = min;
    }

    public Vec2 getMax() {
        return max;
    }

    public void setMax(Vec2 max) {
        this.max = max;
    }
}
