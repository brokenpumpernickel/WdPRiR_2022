package wdprir.lecture004;

import wdprir.lecture002.Helpers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Pools003 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService ex = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        CountDownLatch cl = new CountDownLatch(40);

        for(int i = 0; i < 40; ++i) {
            int j = i;
            ex.execute(() -> {
                System.out.println(j + " " + Helpers.fibon(j));
                cl.countDown();
            });
        }

        cl.await();
        System.out.println("All fibons finished");

        ex.shutdown();
        ex.awaitTermination(1, TimeUnit.DAYS);

    }
}
