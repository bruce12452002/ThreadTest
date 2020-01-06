package advance.example1.lock;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

// 生產者與消費者：寫個容器，有 put 和 get 方法
// 支援 2 個生產者執行緒和 10 個消費者執行緒調用
public class ReentrantLockTest4<T> {
    final private LinkedList<T> list = new LinkedList<>();
    private int count = 0;

    private synchronized void put(T t) {
        // 最多 10 個元素
        // 不可使用 if，因為 wait() 往下會 add，但有可能還沒 add 時，另一個生產者執行緒已經 wait 並 add 了，這時就超過 10 了
        // 使用 while 會再判斷一次，然後看要繼續 wait 或 add，可以解決這個問題
        // 所以綜上所述，只有一個消費者和生產者，不會有這個問題
        // 以上就算將 notifyAll 改成 notify 結果也是一樣，原因不清楚，但官方 API 有說明，有可能不用 notify|notifyAll 確會喚醒
        // 這個名稱叫假喚醒，推薦用 while 代替 if，但沒說原因
        while (list.size() == 10) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                System.err.println("put err=" + e.getMessage());
            }
        }
        list.add(t);
        ++count;
        this.notifyAll();
        System.out.println("put count=" + count);
    }

    private synchronized T get() {
        while (list.size() == 0) { // 不可使用 if
            try {
                this.wait();
            } catch (InterruptedException e) {
                System.err.println("get err=" + e.getMessage());
            }
        }
        T t = list.removeFirst();
        count--;
        this.notifyAll();
        System.out.println("get count=" + count);
        return t;
    }

    public static void main(String[] args) {
        ReentrantLockTest4<String> t = new ReentrantLockTest4<>();
        // 消費者執行緒
        for (var i = 0; i < 10; i++) {
            new Thread(() -> {
                // i * j = 50，所以總共會消費 50 個元素，如「供不應求」或「供過於求」，執行緒「有可能」會阻塞
                for (var j = 0; j < 50; j++) {
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
                // i * j = 50，所以總共會消費 50 個元素，如「供不應求」或「供過於求」，執行緒「有可能」會阻塞
                for (var j = 0; j < 25; j++) {
                    t.put(Thread.currentThread().getName() + " j=" + j);
                }
            }, "producer" + i).start();
        }
    }
}
