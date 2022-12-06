package wdprir.lecture005;

import java.util.concurrent.locks.ReentrantLock;

public class ReadersWritersProblem001 {
    public static class Resource {
        private int value;

        public int getValue() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public static Resource resource = new Resource();
    public static ReentrantLock lock = new ReentrantLock();

    public static class Reader extends Thread {
        @Override
        public void run() {
            while (true) {
                lock.lock();
                System.out.println(getId() + " : " + resource.getValue());
                lock.unlock();
            }
        }
    }

    public static class Writer extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                lock.lock();
                int value = resource.getValue();
                value += 1;
                resource.setValue(value);
                System.out.println(getId() + " write: " + value);
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        Writer w = new Writer();
        w.start();

        Reader[] readers = new Reader[10];
        for (int i = 0; i < readers.length; i++) {
            readers[i] = new Reader();
            readers[i].start();
        }
    }
}
