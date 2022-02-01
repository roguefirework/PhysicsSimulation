package com.github.zukey26.rigidbodysimulation.commands;

import com.github.zukey26.rigidbodysimulation.Simulation;
import com.github.zukey26.rigidbodysimulation.bodies.Shape.Circle;
import com.github.zukey26.rigidbodysimulation.bodies.Shape.Shape;
import com.github.zukey26.rigidbodysimulation.bodies.body.Body;
import com.github.zukey26.rigidbodysimulation.bodies.data.Material;
import com.github.zukey26.rigidbodysimulation.vector.Vec2;

public class CSpawn extends Command
{

    public CSpawn() {
        super("Spawn");
    }

    @Override
    public boolean canExecute(String[] args) {
        return args.length == 6;
    }

    @Override
    public void execute(String[] args) {
        try {
            Shape shape;
            String[] position = args[2].replace("(","").replace(")","").split(",");
            double x = Double.parseDouble(position[0])+0.000001;
            double y = Double.parseDouble(position[1])+0.000001;
            Vec2 spot = new Vec2(x,y);
            shape = new Circle((float) Double.parseDouble(args[4]),spot);
            Material material = Material.ROCK;
            for (Material mat: Material.materials) {
                if(mat.name.equals(args[1].toLowerCase()))
                {
                    material = mat;
                    break;
                }
            }
            String[] velocity = args[3].replace("(","").replace(")","").split(",");
            double x1 = Double.parseDouble(position[0]);
            double y1 = Double.parseDouble(position[1]);
            Simulation.bodies.add(new Body(shape,material,new Vec2(0,0),new Vec2(x1,y1)));
        }
        catch( Exception exception)
        {
            sendError();
            exception.printStackTrace();
        }
    }

    @Override
    public void sendError() {
        System.err.println("CSpawn failed to execute");
    }

    @Override
    public String getUsage() {
        return "{shape} {Material} {position} {velocity} {size}";
    }
}
