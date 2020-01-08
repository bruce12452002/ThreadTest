package queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

// SynchronousQueue：容量為 0，生產者必需直接給消費者，否則例外錯誤(add)或阻塞(put, put 底層也是 transfer)
public class QueueTest4 {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> bq = new SynchronousQueue<>(); //  SynchronousQueue -> BlockingQueue

        consumerTake(bq); // 消費者在生產者之前啟動

        bq.put("xxx"); // 會阻塞
        // bq.add("xxx"); // 不可以 add，因為容量必需為 0，否則 java.lang.IllegalStateException: Queue full
        // System.out.println(bq.offer("xxx")); // 永遠都是 false，因為容量必需為 0
        System.out.println(bq.size());

        // consumerTake(bq);// 消費者在生產者之後啟動
    }

    private static void consumerTake(BlockingQueue<String> bq) {
        new Thread(() -> {
            try {
                System.out.println(bq.take());
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }).start();
    }
}
