package advance.example1;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用 join 來完成 AdvanceThreadTest1 的功能
 */
public class AdvanceThreadTest2<T> {
    private volatile List<T> list = new ArrayList<>();

    private void add(T t) {
        list.add(t);
    }

    private int size() {
        return list.size();
    }

    public static void main(String[] args) {
        AdvanceThreadTest2<Integer> t = new AdvanceThreadTest2<>();

        Thread t2 = new Thread(() -> {
            for (; ; ) {
                if (t.size() == 5) {
                    break;
                }
            }
            System.out.println("t2 over");
        }, "t2");


        Thread t1 = new Thread(() -> {
            for (var i = 0; i < 10; i++) {
                t.add(i);
                System.out.println("增加" + i);

                if (t.size() == 5) {
                    try {
                        t2.join(); // t2 先執行完再繼續執行下去，所以不需要 sleep
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "t1");

        t1.start();
        t2.start();
    }
}
