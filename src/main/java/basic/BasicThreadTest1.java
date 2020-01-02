package basic;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BasicThreadTest1 implements Runnable {
    private int count = 10;

    private Lock lock = new ReentrantLock();

    @Override
    public /*synchronized*/ void run() {
        //Lock lock = new ReentrantLock(); // 寫在這表示每個線程的 lock 都不一樣
        lock.lock();
        try {
            // 下面兩行有可能執行到第一行時，其他執行緒進來執行完兩行，造成 count 數字一樣，所以必須加鎖
            // 例：一開始是 10，第一個執行緒已變成9，但還沒印出來，另外一個執行緒執行完兩行變成8，
            // 這時又回到第一個執行緒，印出來也是8
            count--;
            System.out.println(Thread.currentThread().getName() + ", count=" + count);
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        BasicThreadTest1 t = new BasicThreadTest1(); // 寫在這表示有 6 個「合作」的執行緒
        for (var i = 0; i < 6; i++) {
//            ThreadTest1 t = new ThreadTest1(); // 寫在這表示有 6 個「不合作」的執行緒
            new Thread(t, "t" + i).start();
        }
    }
}
