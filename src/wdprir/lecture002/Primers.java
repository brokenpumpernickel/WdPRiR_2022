package wdprir.lecture002;

public class Primers {
    public static class Worker extends Thread {
        private Counter counter;
        public Worker(Counter counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            int number;
            while ((number = counter.getAndIncrement()) < 1000000) {
                if(Helpers.isPrime(number))
                    System.out.println(number);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.nanoTime();
        Counter counter = new Counter(2);

        Worker[] workers = new Worker[8];
        for (int i = 0; i < workers.length; ++i)
            workers[i] = new Worker(counter);

        for (int i = 0; i < workers.length; ++i) {
            workers[i].start();
        }

        for (int i = 0; i < workers.length; ++i) {
            workers[i].join();
        }
        long stop = System.nanoTime();
        System.out.println((stop - start) / 1000000000.0);
    }
}
