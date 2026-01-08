package org.example;

import java.util.concurrent.Semaphore;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        final int VARIANT = 3;
        final int NUM_FILOZOF = 4;
        if(VARIANT == 4){
//            arbiter
            Semaphore arbiter = new Semaphore(NUM_FILOZOF - 1);
        }

        Fork[] forks = new Fork[NUM_FILOZOF];
        Filozof[] filozofowie = new Filozof[NUM_FILOZOF];

        for (int i = 0; i < NUM_FILOZOF; i++) {
            forks[i] = new Fork();
        }

        for (int i = 0; i < NUM_FILOZOF; i++) {
            Fork leftFork = forks[i];
            Fork rightFork = forks[(i + 1) % NUM_FILOZOF];
            filozofowie[i] = new Filozof(leftFork, rightFork, VARIANT);
            filozofowie[i].start();
        }
    }
}