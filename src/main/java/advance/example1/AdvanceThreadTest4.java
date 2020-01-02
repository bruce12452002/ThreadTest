package advance.example1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 使用 CountDownLatch 來完成 AdvanceThreadTest1 的功能
 */
public class AdvanceThreadTest4<T> {
    private List<T> list = new ArrayList<>();

    private void add(T t) {
        list.add(t);
    }

    private int size() {
        return list.size();
    }

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(1);
        AdvanceThreadTest4<Integer> t = new AdvanceThreadTest4<>();

        new Thread(() -> {
            for (var i = 0; i < 10; i++) {
                t.add(i);
                System.out.println("增加" + i);

                if (t.size() == 5) {
                    latch.countDown();

                    try {
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "t1").start();

        new Thread(() -> {
            if (t.size() != 5) {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }
            System.out.println("t2 over");
        }, "t2").start();
    }
}
