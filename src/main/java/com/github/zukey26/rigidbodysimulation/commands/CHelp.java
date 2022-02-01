package com.github.zukey26.rigidbodysimulation.commands;

import java.util.ArrayList;

public class CHelp extends Command{
    public CHelp() {
        super("Help");
    }

    @Override
    public boolean canExecute(String[] args) {
        return args.length ==0;
    }

    @Override
    public void execute(String[] args) {
        for (Command c: Command.commands) {
            System.out.println(c.name+" usage: "+c.name+" "+c.getUsage());
        }
    }

    @Override
    public void sendError() {
        System.err.println("CHelp failed to execute");
    }

    @Override
    public String getUsage() {
        return "";
    }
}
