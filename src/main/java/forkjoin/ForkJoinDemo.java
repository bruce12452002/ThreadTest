package forkjoin;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

public class ForkJoinDemo extends RecursiveTask<BigInteger> {
    public static void main(String[] args) {
        Instant s = Instant.now();

        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<BigInteger> task = new ForkJoinDemo(BigInteger.ONE, BigInteger.valueOf(100L));
        System.out.println(pool.invoke(task));

        Instant e = Instant.now();
        System.out.println(Duration.between(s, e).toMillis());


        // 使用 java 8 的 stream
        LongStream ls = LongStream.range(2L, 5L); // range 2-4；rangeClosed 2-5
//        for (long l : ls.toArray()) {
//            System.out.println(l);
//        }
        System.out.println(ls.parallel().reduce(0L, Long::sum));
    }


    private BigInteger begin;
    private BigInteger end;
    private static final BigInteger THRESHOLE = BigInteger.valueOf(100L);


    public ForkJoinDemo(BigInteger begin, BigInteger end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    protected BigInteger compute() {
        BigInteger leng = end.subtract(begin);

        if (leng.compareTo(THRESHOLE) <= 0) {
            BigInteger sum = BigInteger.ZERO;

            for (BigInteger i = begin; i.compareTo(end) <= 0; i = i.add(BigInteger.ONE)) {
                sum = sum.add(i);
            }
            return sum;
        } else {
            // 拆三份
            BigInteger oneThird = begin.add(end).divide(BigInteger.valueOf(3L));

            // 假設是 1-100
            ForkJoinDemo one = new ForkJoinDemo(begin, oneThird); // 1-33
            one.fork();

            BigInteger twoThird = oneThird.multiply(BigInteger.TWO);
            ForkJoinDemo two = new ForkJoinDemo(oneThird.add(BigInteger.ONE), twoThird); // 34-66
            one.fork();

            ForkJoinDemo three = new ForkJoinDemo(twoThird.add(BigInteger.ONE), end); // 67-100
            one.fork();

            return one.join().add(two.join()).add(three.join());
        }
    }
}
