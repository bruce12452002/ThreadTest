package advance.example1.threadlocal;

import java.util.concurrent.TimeUnit;

class Xxx {
    String ooo = "hehehe";
}

// 兩個執行緒裡面都有個物件 Xxx，其中一個執行緒改了裡面的屬性，那另一個執行緒就能讀到了
// 但有時候不想要讓別的執行緒知道，只要自己的執行緒有改就好了，這時可以使用 ThreadLocal，看下一個例子
public class AdvanceThreadTest1 {
    private static volatile Xxx xxx = new Xxx();

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(xxx.ooo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> xxx.ooo = "qoo").start();
    }
}
