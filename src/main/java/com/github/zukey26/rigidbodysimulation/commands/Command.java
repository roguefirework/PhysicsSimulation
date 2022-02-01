package com.github.zukey26.rigidbodysimulation.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command
{
    public static CHelp help = new CHelp();
    public static CSpawn spawn = new CSpawn();
    public static List<Command> commands = Arrays.asList(help, spawn);


    public String name;
    public Command(String name) {
        this.name = name;
    }
    public abstract boolean canExecute(String args[]);
    public abstract void execute(String[] args);
    public abstract void sendError();
    public abstract String getUsage();


}
