package threadpool;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolTest5 {
    public static void main(String[] args) {
        Runnable run = () -> System.out.println(Thread.currentThread().getName());

        // daemon 執行緒
        ExecutorService es = Executors.newWorkStealingPool();
        es.execute(run);
        es.execute(run);
        es.execute(run);

        try {
            System.in.read(); // 因為是 daemon 執行緒，所以必需阻塞才看的到輸出
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
