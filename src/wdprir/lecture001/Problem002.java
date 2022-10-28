package wdprir.lecture001;

import java.util.Scanner;

public class Problem002 {
    private static volatile int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for(int i = 0; i < 1000000; ++i)
                ++counter;
        });

        Thread t2 = new Thread(() -> {
            for(int i = 0; i < 1000000; ++i)
                ++counter;
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(counter);
    }
}
