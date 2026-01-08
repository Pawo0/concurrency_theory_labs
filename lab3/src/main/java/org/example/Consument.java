package org.example;

public class Consument extends Thread{
    static int count = 0;
    public int id;
    private final Buffer _buff;

    public Consument(Buffer buff) {
        this.id = count++;
        this._buff = buff;
    }

    public void run(){
        for(int i=0; i<10; i++){
            int val = _buff.get();
        }
    }
}
