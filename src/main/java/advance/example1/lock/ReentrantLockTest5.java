package advance.example1.lock;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// 修改 ReentrantLockTest4，使用 Condition 喚醒指定的執行緒
public class ReentrantLockTest5<T> {
    final private LinkedList<T> list = new LinkedList<>();
    private int count = 0;
    private Lock lock = new ReentrantLock();
    private Condition producer = lock.newCondition();
    private Condition consumer = lock.newCondition();

    private void put(T t) {
        lock.lock();
        try {
            while (list.size() == 10) { // 最多 10 個元素
                producer.await();
            }
            list.add(t);
            ++count;
            consumer.signalAll(); // 類似 notifyAll
            System.out.println("put count=" + count);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    private T get() {
        T t = null;
        lock.lock();
        try {
            while (list.size() == 0) {
                consumer.await();
            }
            t = list.removeFirst();
            count--;
            producer.signalAll(); // 類似 notifyAll
            System.out.println("get count=" + count);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        } finally {
            lock.unlock();
        }
        return t;
    }

    public static void main(String[] args) {
        ReentrantLockTest5<String> t = new ReentrantLockTest5<>();
        // 消費者執行緒
        for (var i = 0; i < 10; i++) {
            new Thread(() -> {
                for (var j = 0; j < 5; j++) {
                    System.out.println(t.get());
                }
            }, "consumer" + i).start();
        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 生產者執行緒
        for (var i = 0; i < 2; i++) {
            new Thread(() -> {
                for (var j = 0; j < 25; j++) {
                    t.put(Thread.currentThread().getName() + " j=" + j);
                }
            }, "producer" + i).start();
        }
    }
}
