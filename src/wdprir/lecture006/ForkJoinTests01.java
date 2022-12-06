package wdprir.lecture006;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.*;

public class ForkJoinTests01 {
    public static ForkJoinPool fjp = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
    public static ExecutorService p = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static class FindMaxAction extends RecursiveTask<Double> {
        private double[] array;
        private int start;
        private int end;

        public FindMaxAction(double[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Double compute() {
            if(end - start < 1024) {
                double max = Double.MIN_VALUE;
                for(int i = start; i < end; ++i) {
                    max = max > array[i] ? max : array[i];
                }
                return max;
            }

//            if(end - start == 2)
//                return array[start] > array[start + 1] ? array[start] : array[start + 1];

            int split = (end - start) / 2;

            FindMaxAction left = new FindMaxAction(array, start, start + split);
            FindMaxAction right = new FindMaxAction(array, start + split, end);

            left.fork();
            Double rightValue = right.compute();
            Double leftValue = left.join();

            return leftValue > rightValue ? leftValue : rightValue;
        }
    }

    public static class FindMaxCallable implements Callable<Double> {
        private double[] array;
        private int start;
        private int end;

        public FindMaxCallable(double[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        public Double call() {
            double max = Double.MIN_VALUE;
            for(int i = start; i < end; ++i) {
                max = max > array[i] ? max : array[i];
            }
            return max;
        }
    }

    public static double findMaxStream(double[] array) {
        return Arrays.stream(array).parallel().max().getAsDouble();
    }

    public static double findMaxCallable(double[] array, ExecutorService pool) throws ExecutionException, InterruptedException {
        int processors = Runtime.getRuntime().availableProcessors();
        int chunk = array.length / processors;
        if(chunk == 0) {
            chunk = array.length;
            processors = 1;
        }

        LinkedList<Future<Double>> results = new LinkedList<>();
        for(int i = 0; i < processors; ++i)
            results.add(pool.submit(new FindMaxCallable(array, chunk * i, i == processors - 1 ? array.length : chunk * (i + 1))));

        double max = Double.MIN_VALUE;
        for(var future : results) {
            double result = future.get();
            max = max > result ? max : result;
        }

        return max;
    }

    public static double findMaxFJP(double[] array, ForkJoinPool pool) {
        return pool.invoke(new FindMaxAction(array, 0, array.length));
    }

    public static double findMaxSequential(double[] array) {
        FindMaxCallable fmc = new FindMaxCallable(array, 0, array.length);
        return fmc.call();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Random random = new Random();
//        System.out.println(findMaxCallable(array, p));
//        System.out.println(findMaxFJP(array, fjp));
//        System.out.println(findMaxSequential(array));

        for(int exp = 4; exp < 28; ++exp) {
            double[] array = new double[1 << exp];
            for(int i = 0; i < array.length; ++i)
                array[i] = -1 + 2 * random.nextDouble();

            long startSequential = System.nanoTime();
            findMaxSequential(array);
            long endSequential = System.nanoTime();

            long startCallable = System.nanoTime();
            findMaxCallable(array, p);
            long endCallable = System.nanoTime();

            long startFJP = System.nanoTime();
            findMaxFJP(array, fjp);
            long endFJP = System.nanoTime();

            long startStream = System.nanoTime();
            findMaxStream(array);
            long endStream = System.nanoTime();

            System.out.println((1 << exp) + " " + (endSequential - startSequential) + " " + (endCallable - startCallable) + " " + (endFJP - startFJP) + " " + (endStream - startStream));
        }

        fjp.shutdown();
        p.shutdown();

        fjp.awaitTermination(1, TimeUnit.DAYS);
        p.awaitTermination(1, TimeUnit.DAYS);
    }
}
