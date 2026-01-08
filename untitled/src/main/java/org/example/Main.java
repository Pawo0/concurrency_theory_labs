package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("Press");


        Counter counter1 = new Counter();
        Counter counter2 = new Counter();

        System.out.println("without synchronized");

        try {
            counter1.runThreads();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        System.out.println("synchronized");

        try{
            counter2.runThreadsSync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("ThreadCounter");
        ThreadCounter.main(new String[]{});
    }
}