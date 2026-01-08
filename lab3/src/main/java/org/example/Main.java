package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.case1();
//        main.case2();
//        main.case3();
    }

    private void case1(){
        System.out.println("More Consuments than Producents");
        Buffer buff = new Buffer();
        for(int i=0; i<3; i++){
            new Producent(buff).start();
            new Consument(buff).start();
        }
        for(int i=0; i<2; i++){
            new Consument(buff).start();
        }
    }

    private void case2(){
        System.out.println("More Producents than Consuments");
        Buffer buff = new Buffer();
        for(int i=0; i<3; i++){
            new Producent(buff).start();
            new Consument(buff).start();
        }
        for(int i=0; i<2; i++){
            new Producent(buff).start();
        }
    }

    private void case3(){
        System.out.println("Equal number of Producents and Consuments");
        Buffer buff = new Buffer();
        for(int i=0; i<3; i++){
            new Producent(buff).start();
            new Consument(buff).start();
        }
    }
}