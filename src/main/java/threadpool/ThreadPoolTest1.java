package threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest1 {
    // execute 沒有回傳值；submit 有回傳值
    public static void main(String[] args) {
        // ※ThreadPoolExecutor：以下 5 個都是用這個類別
        // 1.Executors.newFixedThreadPool(); // 固定幾個執行緒
        // 2.Executors.newCachedThreadPool(); // 最多 Integer.MAX_VALUE 個執行緒，每個執行緒 60 秒沒人連就自動關閉
        // 3.Executors.newSingleThreadExecutor(); // 固定 1 個執行緒

        // ※ScheduledThreadPoolExecutor：排程用的執行緒，此類也是繼承 ThreadPoolExecutor
        // 4.Executors.newScheduledThreadPool(); // 排程用的執行緒
        // 5.Executors.newSingleThreadScheduledExecutor(); // 固定 1 個可排程的執行緒

        // ※ForkJoinPool：為抽象類別，fork 分開執行，join 為合併，所以是分開執行後再合併在一起
        // 6.Executors.newWorkStealingPool();

        ExecutorService es = Executors.newFixedThreadPool(3);
        for (var i = 0; i < 5; i++) {
            es.execute(() -> { // execute 類似 start()，execute 裡放 Runnable，類別實作 Executor 也是一樣
                try {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        System.out.println(es);
        es.shutdown(); // 如果用 shutdownNow，還沒執行完的執行緒會報錯

        System.out.println(es.isTerminated());
        System.out.println(es.isShutdown());
        System.out.println(es);

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(es.isTerminated());
        System.out.println(es.isShutdown());
        System.out.println(es);
    }
}
