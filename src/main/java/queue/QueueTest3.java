package queue;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

// LinkedTransferQueue：消費者先啟動，生產者直接找有沒有消費者，有就給消費者(所以效能更快)；
// 沒有就阻塞或放到隊列裡(transfer 方法會阻塞，add、offer、put 不會阻塞，會放到隊列裡)
public class QueueTest3 {
    public static void main(String[] args) {
        // LinkedTransferQueue -> TransferQueue -> BlockingQueue(沒有 transfer 方法)
        TransferQueue<String> bq = new LinkedTransferQueue<>();

        consumerTake(bq); // 消費者在生產者之前啟動

        try {
            bq.transfer("xxx"); // transfer 找不到消費者會阻塞

            // 在 BlockingQueue 的 put、take 會阻塞
            // 但這裡的 LinkedTransferQueue 重寫了 put(但沒重寫 take)，所以不會阻塞
            // bq.put("xxx");
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }

        // consumerTake(bq);// 消費者在生產者之後啟動
    }

    private static void consumerTake(TransferQueue<String> bq) {
        new Thread(() -> {
            try {
                System.out.println(bq.take());
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }).start();
    }
}
