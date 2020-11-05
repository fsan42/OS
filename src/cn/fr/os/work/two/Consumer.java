package cn.fr.os.work.two;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class Consumer extends Thread{
    AtomicReference<Boolean> exitStatus;
    final Item item;
    public Consumer(AtomicReference<Boolean> exitStatus, Item item) {
        System.out.println("我是一个线程,线程id是"+ currentThread().getId());
        System.out.println("");

        this.exitStatus = exitStatus;
        this.item = item;
    }

    @Override
    public void run() {
        while (exitStatus.get() ){
            synchronized (this.item){
                if (this.item.count<=0 || !this.item.flag){
                    try {
                        item.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("生产者生产结束*********************\n\n\n\n");
                System.out.println(this.item.consumerWord);

                int cCon = new Random().nextInt(item.count)+1;
                this.item.flag =false;
                for (int i = 1; i <= cCon; i++) {
                    System.out.println("消费者消费的第"+(++item.beCon)+"个商品");
                    System.out.println("当前还有"+(--item.count)+"个商品");
                    System.out.println("缓冲区目前空余"+(++item.buf)+"位");
                    try {
                        Thread.sleep(this.item.millis);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                item.notify();
            }
        }
        try{
            Thread.currentThread().interrupt();
            this.item.end(currentThread().getId(),"消费者");
        }catch (Exception e){
            this.item.end(currentThread().getId(),"消费者");
        }


    }
}
