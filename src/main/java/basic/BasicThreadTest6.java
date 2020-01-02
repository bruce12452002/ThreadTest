package basic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class BasicThreadTest6 {
    private AtomicInteger count = new AtomicInteger(0);
    // ThreadTest5 的範例也可用 AtomicXXX 解決，用的是 CAS, 更加的高效
    // 如果只是簡單的運算，用 AtomicXXX 是好的選擇

    private int threadNum = 10; // 執行緒數

    private CountDownLatch latch = new CountDownLatch(threadNum);

    private void m(CountDownLatch latch) {
        for (var i = 0; i < 100; i++) {
            count.getAndIncrement();
        }
        latch.countDown();
    }

    public static void main(String[] args) throws InterruptedException {
        BasicThreadTest6 t6 = new BasicThreadTest6();
        CountDownLatch latch = t6.latch;

        List<Thread> threads = new ArrayList<>();
        for (var i = 0; i < t6.threadNum; i++) {
            threads.add(new Thread(() -> t6.m(latch), "t" + i));
        }
        threads.forEach(Thread::start);

        latch.await();
        System.out.println(t6.count.get());
    }
}
