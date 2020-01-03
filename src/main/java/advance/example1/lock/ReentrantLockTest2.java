package advance.example1.advance.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest2 {
    private Lock lock = new ReentrantLock();  // 可加參數 true 為公平鎖(效率較非公平鎖低)，不寫相當於 false

    private void m() {
        for (var i = 0; i < 100; i++) {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName());
                // 如果使用公平鎖，那會看到很多 t0 t1 t0 t1；如沒使用會看到整排的 t0 和 t1
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        ReentrantLockTest2 r = new ReentrantLockTest2();

        List<Thread> list = new ArrayList<>();
        for (var i = 0; i < 2; i++) {
            list.add(new Thread(r::m, "t" + i));
        }

        list.forEach(Thread::start);
    }
}
