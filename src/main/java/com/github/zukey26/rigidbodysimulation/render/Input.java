package com.github.zukey26.rigidbodysimulation.render;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class Input
{
    private final long window;
    private final Boolean[] keys;

    public Input(long window) {
        keys = new Boolean[GLFW_KEY_LAST];
        this.window = window;
        for (int i = 0; i < GLFW_KEY_LAST; i++)
        {
            if(i<=GLFW_KEY_WORLD_2 && i >= GLFW_KEY_SPACE)
            {
                keys[i] = false;
            }
            else if(i>=GLFW_KEY_ESCAPE)
            {
                keys[i] = false;
            }
        }
    }


    public boolean isKeyDown(int key)
    {
        return glfwGetKey(window,key) == GLFW_PRESS;
    }
    public boolean isMouseButtonDown(int button)
    {
        return glfwGetMouseButton(window,button) == GLFW_PRESS;
    }

    public boolean isKeyPressed(int key)
    {
        return (isKeyDown(key) && !keys[key]);
    }
    public boolean isKeyReleased(int key)
    {
        return (!isKeyDown(key) && keys[key]);
    }

    public void update()
    {
        for (int i = 0; i < GLFW_KEY_LAST; i++)
        {
            if(i<=GLFW_KEY_WORLD_2 && i >= GLFW_KEY_SPACE)
            {
                keys[i] = isKeyDown(i);
            }
            else if(i>=GLFW_KEY_ESCAPE)
            {
                keys[i] = isKeyDown(i);
            }
        }
    }
}