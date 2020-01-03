package advance.example1.threadlocal;

import java.util.concurrent.TimeUnit;

public class AdvanceThreadTest2 {
    private static ThreadLocal<Xxx> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(threadLocal.get()); // null，因為用了 ThreadLocal，所以取不到其他執行緒的物件
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            Xxx xxx = new Xxx();
            xxx.ooo = "qoo";
            threadLocal.set(xxx);
        }).start();
    }
}
