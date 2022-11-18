package wdprir.lecture003;

import wdprir.lecture002.Helpers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Pools001 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService ex = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for(int i = 0; i < 50; ++i) {
            int j = i;
            ex.execute(() -> System.out.println(j + " " + Helpers.fibon(j)));
        }

        ex.shutdown();
        ex.awaitTermination(1, TimeUnit.DAYS);
        System.out.println("All fibons finished");
    }
}
