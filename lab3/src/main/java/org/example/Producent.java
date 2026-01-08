package org.example;

public class Producent extends Thread{
    static int count = 0;
    private final int id;
    private final Buffer _buff;

    public Producent(Buffer buff) {
        this.id = count++;
        this._buff = buff;
    }

    public void run(){
        for(int i=0; i<10; i++){
            _buff.put(i);
        }
    }
}
