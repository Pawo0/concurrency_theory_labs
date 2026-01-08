package org.example;

import java.util.LinkedList;

public class Buffer {
    static LinkedList<Integer> buffer = new LinkedList<>();
    static final int LIMIT = 5;

    public void put(int val){
        synchronized (buffer){
            while (buffer.size() == LIMIT) {
                try {
                    buffer.wait();
                    System.out.println("Buffer full, waiting..." );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            buffer.add(val);
            System.out.println("Added: " + val + " Buffer size: " + buffer.size());
            buffer.notifyAll();
            System.out.println("Notified all" );
        }
    }

    public int get(){
        synchronized (buffer){
            while (buffer.isEmpty()) {
                try {
                    buffer.wait();
                    System.out.println("Buffer empty, waiting..." );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Removed: " + buffer.getFirst() + " Buffer size: " + (buffer.size()-1));
            return buffer.removeFirst();
        }
    }

}
