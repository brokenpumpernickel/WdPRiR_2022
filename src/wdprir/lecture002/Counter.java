package wdprir.lecture002;

import java.util.concurrent.locks.ReentrantLock;

//public class Counter {
//    private int counter;
//    private ReentrantLock lock = new ReentrantLock();
//
//    public Counter(int init) {
//        counter = init;
//    }
//
//    public int getAndIncrement() {
//        lock.lock();
//        int tmp = counter++;
//        lock.unlock();
//        return tmp;
//    }
//}

public class Counter {
    private int counter;

    public Counter(int init) {
        counter = init;
    }

    public synchronized int getAndIncrement() {
        return counter++;
    }
}

//public class Counter {
//    private int counter;
//
//    public Counter(int init) {
//        counter = init;
//    }
//
//    public int getAndIncrement() {
//        synchronized (this) {
//            return counter++;
//        }
//    }
//}

// Nie robcie tego w domu

//public class Counter {
//    private int counter;
//    private ReentrantLock lock = new ReentrantLock();
//
//    public Counter(int init) {
//        counter = init;
//    }
//
//    public int getAndIncrement() {
//        lock.lock();
//        return counter++;
//        lock.unlock();
//    }
//}