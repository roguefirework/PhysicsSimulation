package com.github.zukey26.rigidbodysimulation.commands;

import com.github.zukey26.rigidbodysimulation.Simulation;

import java.util.Arrays;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandWatcher extends Thread {

    public ConcurrentLinkedQueue<String[]> commands = new ConcurrentLinkedQueue<>();

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (Simulation.running.get()) {
            if (scanner.hasNextLine()) {

                String[] command = scanner.nextLine().split(" ");
                commands.add(command);
            }
        }
    }
}