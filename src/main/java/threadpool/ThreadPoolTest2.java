package threadpool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest2 {
    public static void main(String[] args) {
        // execute 沒有回傳值；submit 有回傳值
        futureTaskTest(); // get 方法會阻塞，所以會執行完這個方法後，才會往下執行

        ExecutorService es = Executors.newFixedThreadPool(3);
        Future<String> future = es.submit(() -> { // submit 也類似 start()，submit 裡放 Runnable 或 Callable，而類別實作 Executor 就不行了
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println("future=" + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "haha";
        });

        System.out.println(es);
        try {
            System.out.println("done before=" + future.isDone());
            System.out.println(future.get());
            System.out.println("done after=" + future.isDone());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void futureTaskTest() {
        FutureTask<String> futureTask = new FutureTask<>(() -> { // FutureTask 為 Future 的實作類別
            try {
                TimeUnit.SECONDS.sleep(8);
                System.out.println("futureTask=" + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "##########";
        });

        new Thread(futureTask).start();
        try {
            System.out.println(futureTask.get()); // get 方法會阻塞
        } catch (InterruptedException | ExecutionException e) {
            System.err.println(e.getMessage());
        }
    }
}
