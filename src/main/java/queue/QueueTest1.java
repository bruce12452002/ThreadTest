package queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

// 有界隊列：最多多少個元素的隊列 LinkedBlockingQueue ArrayBlockingQueue
// 無界隊列：元素可以一直加到記憶體沒有為止 ConcurrentLinkedQueue DelayQueue
public class QueueTest1 {
    // put(放) 和 take(取加刪) 都是阻塞方法，所以空或滿了會等在那裡
    // LinkedBlockingQueue 的生產者和消費者都是用獨立的鎖 ReentrantLock；而 ArrayBlockingQueue 是相同的鎖
    private static BlockingQueue<Integer> bq =
            // new LinkedBlockingQueue<>(3);
            new ArrayBlockingQueue(3);

    public static void main(String[] args) {
        new Thread(() -> {
            for (var i = 0; i < 100; i++) {
                try {
                    bq.put(i);
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }
        }, "product").start();

        for (var i = 0; i < 5; i++) {
            new Thread(() -> {
                while (true) {
                    try {
                        System.out.println(Thread.currentThread().getName() + ",take=" + bq.take());
                    } catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }, "consumer" + i).start();
        }
    }
}
