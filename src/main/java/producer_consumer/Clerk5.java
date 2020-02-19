package producer_consumer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Clerk5 {
    private int book;
    private Lock lock = new ReentrantLock(true);
    private Condition condition = lock.newCondition();

    void get() { // 進貨
        lock.lock();
        try {
            while (book >= 1) {
                System.out.println("書已滿");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + " ,幾本書=" + ++book);
            condition.signalAll();
        } finally {
            lock.unlock();
        }

    }

    void sell() {
        lock.lock();
        try {
            while (book <= 0) {
                System.out.println("缺貨");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + " ,幾本書=" + --book);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
