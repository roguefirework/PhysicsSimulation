package com.github.zukey26.rigidbodysimulation.render;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;

public class Window {
    private long window;

    private int width, height;
    private Input input;
    private boolean fullScreen;
    private double time = 0;
    private double elaspedTime = 0;


    /**
     Probably outdated and bad, but it works
     */
    public static void setCallback()
    {

        glfwSetErrorCallback(new GLFWErrorCallback() {
            @Override
            public void invoke(int error, long description) {
                throw new IllegalAccessError("GLFW error [" + Integer.toHexString(error) + "]: " + GLFWErrorCallback.getDescription(description));
            }
        });
    }

    /**
     * Creates a simulation.Window
     * To use use simulation.Window.createWindow()
     */
    public Window() {
        setSize(640, 480);
    }

    /**
     * Creates and diplays the window
     * @param tile Name of the window
     */
    public void createWindow(String tile) {
        window = glfwCreateWindow(width, height, tile, fullScreen ? glfwGetPrimaryMonitor() : 0, 0);
        if (window == 0) {
            throw new IllegalStateException("Failed to create window!");
        }

        if(!fullScreen) {
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            assert vidMode != null;
            glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - width) / 2);
        }
        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        input = new Input(window);


    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }



    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Input getInput() {
        return input;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        if(fullScreen) {
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            assert vidMode != null;
            setWidth(vidMode.width());
            setHeight(vidMode.height());
        }
        this.fullScreen = fullScreen;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }
    public void setShouldClose(boolean shouldClose)
    {
        glfwSetWindowShouldClose(window,true);
    }
    public void update()
    {
        input.update();
        glfwPollEvents();
    }
    public long getWindow()
    {
        return window;
    }



    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getElaspedTime() {
        return elaspedTime;
    }

    public void setElaspedTime(double elaspedTime) {
        this.elaspedTime = elaspedTime;
    }

}
