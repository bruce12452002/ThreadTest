package advance.example1.advance.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest3 {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();

        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(Integer.MAX_VALUE); // 睡很久的執行緒
            } catch (InterruptedException e) {
                System.err.println("t1err=" + e.getMessage());
            } finally {
                lock.unlock();
            }
        }, "t1").start();

        Thread t2 = new Thread(() -> {
            boolean b = false;
            try {
                b = lock.tryLock(3, TimeUnit.SECONDS);
                System.out.println(b);
                lock.lockInterruptibly(); // 設定中斷點
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                System.err.println("t2err=" + e.getMessage());
            } finally {
                if (b) lock.unlock();
            }
        }, "t2");
        t2.start();

        try {
            TimeUnit.SECONDS.sleep(5);
            t2.interrupt(); // 主執行緒中斷 t2 的等待，會到 lockInterruptibly() catch 的 InterruptedException 裡面
            // 如果上一行休眠的時間 < tryLock的時間，也會到 InterruptedException；相反則不會
        } catch (InterruptedException e) {
            System.err.println("main tread=" + e.getMessage());
        }
    }
}
