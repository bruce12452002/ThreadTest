package queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class QueueTest2 {
    BlockingQueue<MyDelayed> bq = new DelayQueue<>();

    public static void main(String[] args) {
        // DelayQueue
        // LinkedTransferQueue
        // SynchronousQueue
    }

    static class MyDelayed implements Delayed {
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
