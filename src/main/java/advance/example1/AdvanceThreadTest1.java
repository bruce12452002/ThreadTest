package advance.example1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 實現一個 container，有 add 和 size方法
 * 寫兩個執行緒，第一個執行緒加 10 個元素，加到第 5 個時
 * 第二個執行緒印出東西並結束第二個執行緒
 */
public class AdvanceThreadTest1<T> {
    private volatile List<T> list = new ArrayList<>();
    // 如果不加 volatile，會有內存可見性的問題，有可能加到 5 的時候，第二個執行緒抓到的不是 5

    private void add(T t) {
        list.add(t);
    }

    private int size() {
        return list.size();
    }

    public static void main(String[] args) {
        AdvanceThreadTest1<Integer> t = new AdvanceThreadTest1<>();

        new Thread(() -> {
            for (var i = 0; i < 10; i++) {
                t.add(i);
                System.out.println("增加" + i);

                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }
        }, "t1").start();

        new Thread(() -> {
            for (; ; ) {
                if (t.size() == 5) { // 一直監聽看看是不是變成 5 了，所以效能較差
                    break;
                }
            }
            System.out.println("t2 over");
        }, "t2").start();
    }
}
