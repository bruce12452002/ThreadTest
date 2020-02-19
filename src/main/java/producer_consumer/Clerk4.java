package producer_consumer;

class Clerk4 {
    private int book;

    synchronized void get() { // 進貨
        while (book >= 1) {
            System.out.println("書已滿");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " ,幾本書=" + ++book);
        this.notifyAll();

    }

    synchronized void sell() {
        while (book <= 0) {
            System.out.println("缺貨");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " ,幾本書=" + --book);
        this.notifyAll();
    }
}
