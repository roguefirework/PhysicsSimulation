package com.github.zukey26.rigidbodysimulation.util;

public class Util {
    public static double clamp(double min, double max, double num)
    {
        if(num < min)
        {
            return min;
        }
        else if(num > max)
        {
            return max;
        }
        return  num;
    }
}
