package wdprir.lecture004;

import wdprir.lecture002.Helpers;

import java.util.concurrent.*;

public class CompletionService001 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService ex = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        CompletionService<String> cs = new ExecutorCompletionService<>(ex);

        for(int i = 0; i < 50; ++i) {
            int j = i;
            cs.submit(() -> (j + " " + Helpers.fibon(j)));
        }

        for (int i = 0; i < 50; i++) {
            System.out.println(cs.take().get());
        }

        ex.shutdown();
        ex.awaitTermination(1, TimeUnit.DAYS);
    }
}
