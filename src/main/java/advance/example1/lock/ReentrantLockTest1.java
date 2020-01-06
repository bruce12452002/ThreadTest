package advance.example1.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest1 {
    private Lock lock = new ReentrantLock();

    private void m1() {
        boolean locked = false;

        try {
            // tryLock 為試著在指定的時間內鎖看看，可用來解決死鎖的問題
            // 也就是可以控制一直拿不到鎖的情形，拿到或拿不到可以做某些事情
            locked = lock.tryLock(5, TimeUnit.MILLISECONDS);
            TimeUnit.SECONDS.sleep(1);
            System.out.println(locked);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
    }

    private void m2() {
        try {
            while (true) {
                if (!lock.tryLock(5, TimeUnit.MILLISECONDS)) {
                    System.out.println("獲取鎖失敗，等100再重新嘗試解鎖");
                    TimeUnit.MILLISECONDS.sleep(100);
                } else {
                    System.out.println("獲取鎖成功");
                    break;
                }
            }
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantLockTest1 r = new ReentrantLockTest1();
        new Thread(r::m2).start();
    }
}
