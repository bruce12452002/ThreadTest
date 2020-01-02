package advance.example1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 使用 wait 和 notify 來完成 AdvanceThreadTest1 的功能
 */
public class AdvanceThreadTest3<T> {
    private List<T> list = new ArrayList<>();

    private void add(T t) {
        list.add(t);
    }

    private int size() {
        return list.size();
    }

    public static void main(String[] args) {
        final Object o = new Object();
        AdvanceThreadTest3<Integer> t = new AdvanceThreadTest3<>();
        // wait 會釋放鎖，notify|notifyAll 不會釋放鎖

        Thread t1 = new Thread(() -> {
            synchronized (o) {
                for (var i = 0; i < 10; i++) {
                    t.add(i);
                    System.out.println("增加" + i);

                    if (t.size() == 5) {
                        o.notify();

                        try {
                            o.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (o) {
                if (t.size() != 5) {
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("t2 over");
                o.notify();
            }
        }, "t2");

        t2.start(); // t2 必需先執行，因為 t2 必需先 wait
        t1.start();

        // 如果 t1 的 wait 和 t2 的 notify 不寫，
        // 結果會是 t1 執行完後，才會執行 t2，因為 notify 不釋放鎖
    }
}
