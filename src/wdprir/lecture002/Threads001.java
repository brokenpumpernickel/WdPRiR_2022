package wdprir.lecture002;

public class Threads001 {
    public static class MyThread extends Thread {
        @Override
        public void run() {
            for(int i = 0; i < 10; ++i)
                System.out.println("Hello from " + getId() + " / " + i);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyThread t = new MyThread();
        t.start();
        t.join();

        System.out.println("Done!");
    }
}
