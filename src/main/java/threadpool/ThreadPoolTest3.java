package threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// 一個執行緒做一件事和多個執行緒做一件事的效能比較
public class ThreadPoolTest3 {
    public static void main(String[] args) {
        long curr = System.currentTimeMillis();
        List<Integer> list = new ArrayList<>();
        for (var i = 1; i <= 400000; i++) {
            if (i % 2 == 0) {
                list.add(i);
            }
        }
        System.out.println(System.currentTimeMillis() - curr);

        System.out.println("----------------- 數量大到一定的程度會比較快 ----------------");
        MyCallable mc1 = new ThreadPoolTest3().new MyCallable(1, 100000);
        MyCallable mc2 = new ThreadPoolTest3().new MyCallable(100001, 200000);
        MyCallable mc3 = new ThreadPoolTest3().new MyCallable(200001, 300000);
        MyCallable mc4 = new ThreadPoolTest3().new MyCallable(300001, 400000);

        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); // CPU 核心數
        Future<List<Integer>> s1 = es.submit(mc1);
        Future<List<Integer>> s2 = es.submit(mc2);
        Future<List<Integer>> s3 = es.submit(mc3);
        Future<List<Integer>> s4 = es.submit(mc4);

        try {
            long l1 = System.currentTimeMillis();
            s1.get();
            s2.get();
            s3.get();
            s4.get();
            System.out.println(System.currentTimeMillis() - l1);
            es.shutdown();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private class MyCallable implements Callable<List<Integer>> {
        private int start;
        private int end;

        private MyCallable(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public List<Integer> call() throws Exception {
            List<Integer> list = new ArrayList<>();
            for (var i = start; i <= end; i++) {
                if (i % 2 == 0) {
                    list.add(i);
                }
            }
            return list;
        }
    }
}
