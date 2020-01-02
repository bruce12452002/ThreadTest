package basic;

import java.util.concurrent.TimeUnit;

public class BasicThreadTest2 {
    private synchronized void m1() {
        System.out.println(Thread.currentThread().getName() + ", m1 start...");
        try {
            TimeUnit.SECONDS.sleep(6);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ", m1 end");
    }

    private void m2() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " m2 ");
    }

    public static void main(String[] args) {
        // 證明同步和非同步的方法可以同時調用
        BasicThreadTest2 t = new BasicThreadTest2();
        new Thread(t::m1, "t1").start();
        new Thread(t::m2, "t2").start();
    }
}
