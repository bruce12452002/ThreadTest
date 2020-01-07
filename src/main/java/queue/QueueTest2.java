package queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class QueueTest2 {
    BlockingQueue<MyDelayed> bq = new DelayQueue<>();

    public static void main(String[] args) {
        // DelayQueue：每一個元素自己記錄我還有多長時間可以被消費者拿走，有序，等待時間長的在最前面，也會最先被拿走，可用來做定時任務
        // LinkedTransferQueue：消費者先啟動，生產者直接找有沒有消費者，有就給消費者(所以效能更快)；沒有就阻塞(transfer 方法會阻塞，add、offer
        // put 不會阻塞，因為會放到隊列裡)
        // SynchronousQueue：容量為 0，生產者必需直接給消費者，否則例外錯誤(add)或阻塞(put, put 底層也是 transfer)
    }

    static class MyDelayed implements Delayed { // 因為 Delayed 有繼承 Comparable，所以還要實作 compareTo
        @Override
        public long getDelay(TimeUnit unit) {
            return 0;
        }

        @Override
        public int compareTo(Delayed o) {
            return 0;
        }
    }
}
