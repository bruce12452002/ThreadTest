package basic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BasicThreadTest5 {
    private /*volatile*/ int count = 0; // 這個例子沒鎖的情形下，volatile 沒用，因為內存可見性只保證讀而已；寫的時候還是有問題
    // 如兩個執行緒都讀到 0，然後都加1再寫回去是1，解決方法還是加鎖，這樣才會是2

    private Lock lock = new ReentrantLock();

    private int threadNum = 10; // 執行緒數

    private CountDownLatch latch = new CountDownLatch(threadNum);

    private void m(CountDownLatch latch) {
        lock.lock();
        try {
            for (var i = 0; i < 100; i++) {
                count++;
            }
        } finally {
            latch.countDown();
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BasicThreadTest5 t5 = new BasicThreadTest5();
        CountDownLatch latch = t5.latch;

        List<Thread> threads = new ArrayList<>();
        for (var i = 0; i < t5.threadNum; i++) {
            threads.add(new Thread(() -> t5.m(latch), "t" + i));
        }
        threads.forEach(Thread::start);

        latch.await();
        System.out.println(t5.count);
    }
}
