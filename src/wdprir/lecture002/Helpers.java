package wdprir.lecture002;

public class Helpers {
    public static boolean isPrime(int number) {
        for(int i = 2; i < number; ++i)
            if(number % i == 0)
                return false;
        return true;

    }

    public static void main(String[] args) {
        long start = System.nanoTime();
        for(int n = 2; n < 1000000; ++n)
            if(isPrime(n))
                System.out.println(n);
        long stop = System.nanoTime();
        System.out.println((stop - start) / 1000000000.0);
    }
}
