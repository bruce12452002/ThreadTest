package queue;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// DelayQueue：每一個元素自己記錄我還有多長時間可以被消費者拿走，有序的
// 等待時間長或短的在最前面(具體看 compareTo 方法)，也會最先被拿走，可用來做定時任務
public class QueueTest2 {
    BlockingQueue<MyDelayed> bq = new DelayQueue<>();

    public static void main(String[] args) {
        long now = System.currentTimeMillis();
        QueueTest2 qt = new QueueTest2();
        MyDelayed m1 = qt.new MyDelayed(now + 5);
        MyDelayed m2 = qt.new MyDelayed(now + 2);
        MyDelayed m3 = qt.new MyDelayed(now + 1);
        MyDelayed m4 = qt.new MyDelayed(now + 3);
        MyDelayed m5 = qt.new MyDelayed(now + 4);

        List<MyDelayed> list = Stream.of(m1, m2, m3, m4, m5).collect(Collectors.toList());

        list.forEach(m -> {
            try {
                qt.bq.put(m);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        });

        System.out.println(qt.bq);

        for (var i = 0; i < 5; i++) {
            try {
                System.out.println(qt.bq.take());
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private class MyDelayed implements Delayed { // 因為 Delayed 有繼承 Comparable，所以還要實作 compareTo
        private long time;

        MyDelayed(long time) {
            this.time = time;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(time - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(o.getDelay(TimeUnit.MILLISECONDS), this.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public String toString() {
            return "time=" + time;
        }
    }
}
