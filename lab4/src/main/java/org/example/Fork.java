package org.example;

public class Fork {
    static int counter = 0;
    private final int id;
    private boolean isTaken;

    public Fork() {
        this.id = counter++;
        this.isTaken = false;
    }

   public boolean isTaken() {
        return isTaken;
    }


    public void take(){
        synchronized (this) {
            while (isTaken) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isTaken = true;
        }
    }

    public void putDown(){
        synchronized (this){
            isTaken = false;
            notifyAll();
        }
    }

    public int getId() {
        return id;
    }
}
