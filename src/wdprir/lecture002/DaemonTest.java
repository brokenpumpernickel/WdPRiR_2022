package wdprir.lecture002;

public class DaemonTest {
    public static class ParentThread extends Thread {
        private Thread child;

        @Override
        public void run() {
            child = new Thread() {
                @Override
                public void run() {
                    System.out.println("Child start");
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Child stop");
                }
            };
            //child.setDaemon(true);
            child.start();
        }

        public Thread getChild() {
            return child;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ParentThread parent = new ParentThread();
        parent.start();

        Thread.sleep(1000);

        System.out.println(parent.isAlive());
        System.out.println(parent.getChild().isAlive());
    }
}
