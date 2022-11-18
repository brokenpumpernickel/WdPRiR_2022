package wdprir.lecture004;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer {
    public static class Producer extends Thread{
        private BlockingQueue<String> queue;
        public Producer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            Random random = new Random();
            int index = 0;
            while(true) {
                try {
                    Thread.sleep(random.nextInt(1000));
                    String element = "Produced: " + index + " (" + getId() + ")";
                    System.out.println(element);
                    queue.put(element);
                    ++index;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class Consumer extends Thread{
        private BlockingQueue<String> queue;
        public Consumer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            Random random = new Random();
            while(true) {
                try {
                    String element = queue.take();
                    Thread.sleep(random.nextInt(2000));
                    System.out.println("Consumed: " + element + " (" + getId() + ")");

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void main(String[] args) {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();

        Producer[] producers = new Producer[2];
        for (int i = 0; i < producers.length; i++) {
            producers[i] = new Producer(queue);
            producers[i].start();
        }

        Consumer[] consumers = new Consumer[4];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer(queue);
            consumers[i].start();
        }
    }
}
