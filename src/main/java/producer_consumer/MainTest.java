package producer_consumer;

import java.util.concurrent.TimeUnit;

public class MainTest {
    public static void main(String[] args) {
//        Clerk1 clerk = new Clerk1(); // 已經知道缺貨或書已滿了，還一直重覆跑，浪費效能
//        Clerk2 clerk = new Clerk2(); // 連線有可能不會關閉，因為最後一次的 wait 後，沒人喚醒(else 有問題)
//        Clerk3 clerk = new Clerk3(); // 執行緒多一點資料會出錯，因為有假喚醒的問題
//        Clerk4 clerk = new Clerk4(); // 有考慮假喚醒(if 換 while)
        Clerk5 clerk = new Clerk5(); // 使用 ReentrantLock

        Runnable pro = () -> {
            for (var i = 0; i < 20; i++) {
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                    clerk.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable csm = () -> {
            for (var i = 0; i < 20; i++) {
                clerk.sell();
            }
        };

        new Thread(pro, "p1").start();
        new Thread(csm, "c1").start();
        new Thread(pro, "p2").start(); // 測 Clerk3 打開
        new Thread(csm, "c2").start(); // 測 Clerk3 打開
    }
}
