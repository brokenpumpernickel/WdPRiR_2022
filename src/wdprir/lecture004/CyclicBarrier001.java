package wdprir.lecture004;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrier001 {
    public static void main(String[] args) {
        Thread[] jobs = new Thread[6];

        CyclicBarrier cb = new CyclicBarrier(jobs.length, () -> System.out.println("Phase finished!")); // Phaser
        for (int i = 0; i < jobs.length; i++) {
            jobs[i] = new Thread() {
                @Override
                public void run() {
                    Random random = new Random();
                    while (true) {
                        try {
                            Thread.sleep(random.nextInt(1000));
                            System.out.println("Part A finished: " + getId());

                            cb.await();

                            Thread.sleep(random.nextInt(1000));
                            System.out.println("Part B finished: " + getId());

                            cb.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } catch (BrokenBarrierException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            };
            jobs[i].start();
        }
    }
}
