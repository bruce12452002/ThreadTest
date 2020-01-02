package basic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BasicThreadTest4 {
    private int count = 0;

    private Lock lock = new ReentrantLock();

    private /*synchronized*/ void m() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " 開始");
            for (; ; ) {
                count++;
                System.out.println(Thread.currentThread().getName() + ", count=" + count);

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (count % 3 == 0) {
//                    try {
                        System.out.println(2 / 0); // synchronized 例外會自動釋放鎖(加 try catch 可以解決)
                    // ReentrantLock 不會自動釋放鎖，要用 unlock() 才會釋放，所以如果 finally 不寫 unlock() 不會釋放
//                    } catch (Exception e) {
//                        System.err.println(e.getMessage());
//                    }
                }
            }
        } finally {
            // lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // t3a 執行緒佔著m方法，m方法是無限循環的迴圈，如果釋放鎖 t3b 才會執行
        // 使用同步和 ReentrantLock 看例外時會不會釋放鎖
        BasicThreadTest4 t4 = new BasicThreadTest4();
        new Thread(t4::m, "t3a").start();
        TimeUnit.SECONDS.sleep(3);
        new Thread(t4::m, "t3b").start();
    }
}
