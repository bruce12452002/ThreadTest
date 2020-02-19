package producer_consumer;

class Clerk2 {
    private int book;

    synchronized void get() { // 進貨
        if (book >= 1) { // 改成 1
            System.out.println("書已滿");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(Thread.currentThread().getName() + " ,幾本書=" + ++book);
            this.notifyAll();
        }
    }

    synchronized void sell() {
        if (book <= 0) {
            System.out.println("缺貨");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(Thread.currentThread().getName() + " ,幾本書=" + --book);
            this.notifyAll();
        }
    }
}
