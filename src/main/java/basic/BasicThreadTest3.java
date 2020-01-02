package basic;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BasicThreadTest3 {
    private Map<String, BigDecimal> map = new HashMap<>();

    private synchronized void set(String name, BigDecimal balance) {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        map.put(name, balance);
    }

    private /*synchronized*/ BigDecimal getBalance(String name) {
        return this.map.get(name);
    }

    public static void main(String[] args) throws InterruptedException {
        // 如果寫加鎖但讀不加鎖，會有不可重複讀的情形
        // 不可重複讀：「同一事務」內，讀取的結果不同
        BasicThreadTest3 t = new BasicThreadTest3();
        new Thread(() -> t.set("小明", BigDecimal.TEN)).start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(t.getBalance("小明"));
        TimeUnit.SECONDS.sleep(2);
        System.out.println(t.getBalance("小明"));
    }
}
