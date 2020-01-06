package threadpool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Stream;

public class ThreadPoolTest6 extends RecursiveTask<Long> { // RecursiveTask 繼承 ForkJoinTask
    private long begin;
    private long end;

    private ThreadPoolTest6(long begin, long end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long range = end - begin;
        long sum = 0;

        if (range >= 5000_0000L) { // 如果有拆分會一直遞迴 compute 方法
            long mid = (begin + end) / 2;
            var fjt1 = new ThreadPoolTest6(begin, mid);
            var fjt2 = new ThreadPoolTest6(mid + 1, end);
            fjt1.fork();
            fjt2.fork();
            sum += fjt1.join();
            sum += fjt2.join();
        } else {
            for (; begin <= end; begin++) {
                sum += begin;
            }
        }
        return sum;
    }

    public static void main(String[] args) throws Exception {
        long s = System.currentTimeMillis();
        ThreadPoolTest6 mfjt = new ThreadPoolTest6(1, 1_0000_0000L);

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Long> submit = forkJoinPool.submit(mfjt);// 可以放 ForkJoinTask、Runnable、Callable
        System.out.println(submit.get());
        System.out.println(System.currentTimeMillis() - s);
    }
}
