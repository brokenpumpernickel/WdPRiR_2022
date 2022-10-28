package wdprir.lecture002;

public class Threads002 {

    public static class MyThread01 extends Thread {
        @Override
        public void run() {
            System.out.println("Hello from " + getId());
        }
    }

    public static class MyThread02 implements Runnable {
        @Override
        public void run() {
            System.out.println("Hello from " + Thread.currentThread().getId());
        }
    }

    public static void main(String[] args) throws InterruptedException {


        MyThread01 t01 = new MyThread01();
        t01.start();

        Thread t02 = new Thread(new MyThread02());
        t02.start();

        Thread t03 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello from " + Thread.currentThread().getId());
            }
        });
        t03.start();

        Thread t04 = new Thread(() -> System.out.println("Hello from " + Thread.currentThread().getId()));
        t04.start();

        t01.join();
        t02.join();
        t03.join();
        t04.join();
    }
}
