package cn.fr.os.work.two;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class Provider extends Thread{
    AtomicReference<Boolean> exitStatus;
    final Item item;
    public Provider(AtomicReference<Boolean> exitStatus, Item item) {
        System.out.println("我是一个线程,线程id是"+ exitStatus);
        this.exitStatus = exitStatus;
        this.item = item;
    }
    @Override
    public void run() {
        while (exitStatus.get()){
            synchronized (this.item){
                if (this.item.buf<=0 || this.item.flag){
                    try {
                        item.wait();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (this.item.bePro != 0){
                    System.out.println("消费者消费结束*********************\n\n\n\n");
                }
                System.out.println(this.item.providerWord);

                int cPro = new Random().nextInt(this.item.buf)+1;
                this.item.flag = true;
                for (int i = 1; i <= cPro; i++) {
                    System.out.println("生产者生产的第"+(++item.bePro)+"个商品");
                    System.out.println("当前还有"+(++item.count)+"个商品");
                    System.out.println("缓冲区目前空余"+(--item.buf)+"位");
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
            this.item.end(currentThread().getId(),"生产者");
        }catch (Exception e){
            this.item.end(currentThread().getId(),"生产者");
        }


    }
}
