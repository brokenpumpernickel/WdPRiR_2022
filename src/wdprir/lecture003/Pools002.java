package wdprir.lecture003;

import wdprir.lecture002.Helpers;

import java.util.LinkedList;
import java.util.concurrent.*;

public class Pools002 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService ex = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        LinkedList<Future<String>> futures = new LinkedList<>();
        for(int i = 0; i < 50; ++i) {
            int j = i;
            futures.add(ex.submit(() -> (j + " " + Helpers.fibon(j))));
        }

        for(var future : futures) {
            System.out.println(future.get());
        }

        ex.shutdown();
        ex.awaitTermination(1, TimeUnit.DAYS);
        System.out.println("All fibons finished");
    }
}
