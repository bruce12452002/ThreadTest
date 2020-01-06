package threadpool;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest4 {
    public static void main(String[] args) {
        ScheduledExecutorService es = Executors.newScheduledThreadPool(2);

        // 只跑一次，2 秒後執行
        // es.schedule(() -> System.out.println(new Date()), 2, TimeUnit.SECONDS);

        // 2 秒後，每 1 秒執行一次(「不會」加上任務執行時間)
        /*
        es.scheduleAtFixedRate(() -> {
            System.out.print(Thread.currentThread().getName() + ":");
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println(new Date());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 2, 1, TimeUnit.SECONDS);
        */

        // 2 秒後，每 1 秒執行一次(會加上任務執行時間)
        es.scheduleWithFixedDelay(() -> {
            System.out.print(Thread.currentThread().getName() + ":");
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println(new Date());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(new Date());
        }, 2, 1, TimeUnit.SECONDS);
    }
}
