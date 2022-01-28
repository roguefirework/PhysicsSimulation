package com.github.zukey26.rigidbodysimulation;
import com.github.zukey26.rigidbodysimulation.render.Window;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    public static Window window;
    public static final int pixelsPerUnit = 50;
    /**
    * @param args - optional arguments
     */
    public static void main(String[] args)
    {
        setupOpenGL();

        new Simulation();
    }
    static void setupOpenGL()
    {
        Window.setCallback();
        if(!glfwInit())
        {
            System.err.println("GLFW Failed to intialize! This is not good");
            System.exit(-1);
        }
        window = new Window();
        window.createWindow("Simulation: ");

        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
    }
}
