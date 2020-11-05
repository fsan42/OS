package cn.fr.os.work.two;

import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static void main(String[] args) {
        AtomicReference<Boolean> exitStatus = new AtomicReference<>(Boolean.TRUE);
        Item item = new Item("商品", 0,10,200);
        Provider provider = new Provider(exitStatus,item);
        Consumer consumer = new Consumer(exitStatus,item);
        System.out.println();


        provider.start();
        consumer.start();

        new Thread(()->{
            try {
                Thread.sleep(4000);
                exitStatus.set(false);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
