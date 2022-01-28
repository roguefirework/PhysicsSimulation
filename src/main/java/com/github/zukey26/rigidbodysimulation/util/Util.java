package com.github.zukey26.rigidbodysimulation.util;

import com.github.zukey26.rigidbodysimulation.Main;
import com.github.zukey26.rigidbodysimulation.vector.Vec2;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;

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
    public static void DrawCircle(double cx, double cy, float r, int num_segments)
    {
        cx = correctWidth(cx);
        cy = correctHeight(cy);
        glBegin(GL_TRIANGLE_FAN);
        for(int ii = 0; ii < num_segments; ii++)
        {
            float theta = 2.0f * 3.1415926f * (float)ii / (float) num_segments;//get the current angle

            double x = correctWidth(r) * (float) Math.cos(theta);//calculate the x component
            double y = correctHeight(r) * (float) Math.sin(theta);//calculate the y component

            glVertex2d(x + cx, y + cy);//output vertex
        }
        glEnd();
    }
    public static double lerp(double v0,double v1,double t)
    {
        return (1 - t) * v0 + (t * v1);
    }
    public static void DrawSquare(final Vec2 position,final double sideLength)
    {
        glBegin(GL_POLYGON);
        final Vec2 root = position.sub(new Vec2(sideLength/2,sideLength/2));
        final double rootX = correctWidth(root.getX());
        final double rootY = correctHeight(root.getY());
        glVertex2d(rootX,rootY);//(0,0)
        glVertex2d(rootX+correctWidth(sideLength),rootY);//(1,0)
        glVertex2d(rootX+correctWidth(sideLength),rootY+correctHeight(sideLength));//(1,1)
        glVertex2d(rootX,rootY+correctHeight(sideLength));//(0,1)
        glEnd();
    }


    public static double correctWidth(double x)
    {
        return Main.pixelsPerUnit * x / Main.window.getWidth();
    }
    public static double correctHeight(double y)
    {
        return Main.pixelsPerUnit * y / Main.window.getHeight();
    }
}
