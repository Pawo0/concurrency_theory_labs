package org.example;

public class Counter {
    public int state = 0;

    public void increment() {
        state++;
    }

    public void decrement() {
        state--;
    }

    Thread incrementThread = new Thread() {
        @Override
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                Counter.this.increment();
            }
        }
    };

    Thread decrementThread = new Thread() {
        @Override
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                Counter.this.decrement();
            }
        }
    };

    Thread incrementThreadSync = new Thread() {
        @Override
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                synchronized (Counter.this) {
                    Counter.this.increment();
                }
            }
        }
    };

    Thread decrementThreadSync = new Thread() {
        @Override
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                synchronized (Counter.this) {
                    Counter.this.decrement();
                }
            }
        }
    };


    public void runThreads() throws InterruptedException {
        threadRunner(incrementThread, decrementThread);
    }

    public void runThreadsSync() throws InterruptedException {
        threadRunner(incrementThreadSync, decrementThreadSync);
    }

    private void threadRunner(Thread t1, Thread t2) throws InterruptedException {
        double start = System.currentTimeMillis();

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        double end = System.currentTimeMillis();
        System.out.println("Final state: " + this.state);
        System.out.println("Time taken: " + (end - start) + " ms");
    }
}
