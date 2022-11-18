package wdprir.lecture002;

import java.util.HashMap;

public class Helpers {
    public static boolean isPrime(int number) {
        for(int i = 2; i < number; ++i)
            if(number % i == 0)
                return false;
        return true;

    }

    public static long fibon(long n) {
        if(n < 2)
            return n;

        return fibon(n - 1) + fibon(n - 2);
    }

    public static HashMap<Long, Long> cache = new HashMap<>();
    public static long fibonCached(long n) {
        if(n < 2)
            return n;

        return cache.computeIfAbsent(n, i -> fibonCached(i - 1) + fibonCached(i - 2));
    }

    public static void main(String[] args) {
//        long start = System.nanoTime();
//        for(int n = 2; n < 1000000; ++n)
//            if(isPrime(n))
//                System.out.println(n);
//        long stop = System.nanoTime();
//        System.out.println((stop - start) / 1000000000.0);

        long start = System.nanoTime();
        for(int i = 0; i < 50; ++i) {
            System.out.println(i + " " + fibonCached(i));
        }
        long stop = System.nanoTime();
        System.out.println((stop - start) / 1000000000.0);
    }
}
