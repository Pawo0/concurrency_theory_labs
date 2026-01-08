package org.example;

import java.util.concurrent.Semaphore;

public class Filozof extends Thread {
    static int counter = 0;
    private final Fork leftFork;
    private final Fork rightFork;
    private int variant;
    private final int id;
    private double averageWaitTime;
    private Semaphore arbiter;

    public Filozof(Fork leftFork, Fork rightFork, int variant) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.id = counter++;
        this.variant = variant;
    }
    public Filozof(Fork leftFork, Fork rightFork, int variant, Semaphore arbiter) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.id = counter++;
        this.variant = variant;
        this.arbiter = arbiter;
    }
    public void eat() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("Filozof " + id + " je.");

    }

    public void dineVariant1() throws InterruptedException {
        while (true) {
            System.out.println("Filozof " + id + " mysli.");
            leftFork.take();
            System.out.println("Filozof " + id + " wzial lewy widelec. (id widelca: " + leftFork.getId() + ")");
            rightFork.take();
            System.out.println("Filozof " + id + " wzial prawy widelec. (id widelca: " + rightFork.getId() + ")");
            this.eat();
            leftFork.putDown();
            rightFork.putDown();
        }
    }


    public void dineVariant2() throws InterruptedException {
        while (true) {
            System.out.println("Filozof " + id + " mysli.");
            while (leftFork.isTaken() || rightFork.isTaken()) {
                Thread.sleep(10);
            }
            leftFork.take();
            System.out.println("Filozof " + id + " wzial lewy widelec. (id widelca: " + leftFork.getId() + ")");
            rightFork.take();
            System.out.println("Filozof " + id + " wzial prawy widelec. (id widelca: " + rightFork.getId() + ")");
            this.eat();
            leftFork.putDown();
            rightFork.putDown();

        }
    }

    public void dineVariant3() throws InterruptedException {
        while (true) {
            System.out.println("Filozof " + id + " mysli.");
            if (id % 2 == 0) {
                leftFork.take();
                System.out.println("Filozof " + id + " wzial lewy widelec. (id widelca: " + leftFork.getId() + ")");
                rightFork.take();
                System.out.println("Filozof " + id + " wzial prawy widelec. (id widelca: " + rightFork.getId() + ")");
            } else {
                rightFork.take();
                System.out.println("Filozof " + id + " wzial prawy widelec. (id widelca: " + rightFork.getId() + ")");
                leftFork.take();
                System.out.println("Filozof " + id + " wzial lewy widelec. (id widelca: " + leftFork.getId() + ")");
            }
            this.eat();
            leftFork.putDown();
            rightFork.putDown();
        }
    }

    public void dineVariant4() throws InterruptedException {
        while (true) {
            System.out.println("Filozof " + id + " mysli.");
            arbiter.acquire();
            leftFork.take();
            System.out.println("Filozof " + id + " wzial lewy widelec. (id widelca: " + leftFork.getId() + ")");
            rightFork.take();
            System.out.println("Filozof " + id + " wzial prawy widelec. (id widelca: " + rightFork.getId() + ")");
            this.eat();
            leftFork.putDown();
            rightFork.putDown();
            arbiter.release();
        }
    }
    @Override
    public void run() {
        try {
            switch (variant) {
                case 1 -> dineVariant1();
                case 2 -> dineVariant2();
                case 3 -> dineVariant3();
                case 4 -> dineVariant4();
                default -> System.out.println("Nieznany wariant");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
