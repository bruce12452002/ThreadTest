package basic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;

// Collections.synchronizedXxx() 轉換成同步集合
public class ConcurrentTest2 {
    public static void main(String[] args) {
        int threadCount = 100;
        CountDownLatch latch = new CountDownLatch(threadCount);
        Map<Integer, Integer> map =
                // new HashMap<>(); // 會有併發問題
                // new Hashtable<>();
                 new ConcurrentHashMap<>();
                // new ConcurrentSkipListMap<>(); // 高併發且排序

        // 有併發問題的 map 轉換成為併發設計的類別，效能和 Hashtable 差不多
        // 但要注要有些方法如 iterator、listIterator 並沒有 synchronized，要自己加
        // Map<Integer, Integer> map = Collections.synchronizedMap(mapX);

        long startTime = System.currentTimeMillis();
        for (var i = 0; i < threadCount; i++) {
            final int xxx = i + new Random().nextInt();
            new Thread(() -> {
                for (var j = 0; j < 1000; j++) {
                    map.put(xxx + j, j);
                }
                latch.countDown();
            }).start();
        }

        try {
            latch.await();
            System.out.println("size=" + map.size());
            System.out.println(System.currentTimeMillis() - startTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
