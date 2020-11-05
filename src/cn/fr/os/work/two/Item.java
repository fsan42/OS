package cn.fr.os.work.two;

public class Item implements Cloneable {
    public String name;
    public int count;
    public int buf;
    public int bePro;
    public int beCon;
    public boolean flag;
    public long millis;
    public String providerWord;
    public String consumerWord;

    @Override
    protected Item clone() throws CloneNotSupportedException {
        return (Item) super.clone();
    }

    public Item(String name, int count, int buf, long millis) {
        this.name = name;
        this.count = count;
        this.buf = buf;
        this.bePro = 0;
        this.beCon = 0;
        this.flag = false;
        this.millis =  millis;
        providerWord = "生产者开始生产商品*********************";
        consumerWord = "消费者开始消费商品*********************";
    }
    public void end(long id,String role){
        System.out.println("*********");
        for (int i = 0; i < 5; i++) {
            System.out.println("2018124068");
        }

        System.out.println("*********");
        System.out.println("线程"+id+role+"线程终止退出");

    }
}
