package basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

// CopyOnWriteArrayList 複製一份出來，然後指針指到新的地方，讀多寫少可用；寫多雖然也能用，但效能太差
// Collections.synchronizedXxx() 轉換成同步集合
public class ConcurrentTest1 {
    public static void main(String[] args) {
        int threadCount = 100;
        CountDownLatch latch = new CountDownLatch(threadCount);
        List<Integer> list =
                // new ArrayList<>(); // 會有併發問題
                 new Vector<>();
                // new CopyOnWriteArrayList<>(); // 此例會大量更改 list，所以用這個不適合，太慢了

        // 有併發問題的 list 轉換成為併發設計的類別，效能和 Vector 差不多
        // 但要注要有些方法如 iterator、listIterator 並沒有 synchronized，要自己加
        // List<Integer> list = Collections.synchronizedList(listX);

        long startTime = System.currentTimeMillis();
        for (var i = 0; i < threadCount; i++) {
            new Thread(() -> {
                for (var j = 0; j < 1000; j++) {
                    list.add(j);
                }
                latch.countDown();
            }).start();
        }

        try {
            latch.await();
            System.out.println("size=" + list.size());
            System.out.println(System.currentTimeMillis() - startTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
