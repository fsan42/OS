package cn.fr.os.work.two;

import java.nio.file.Watchable;
import java.util.*;

/**
 * (BankerAlgorithm)银行家问题
 *
 * @author fr
 * @since 2020年11月1日23:27:57
 */

public class BankerAlgorithm {
    static int M = 3;//元素种类
    static int K = 10;//当前资源
    static int N = 5;//进程对象

    static int ERROR = 0;
    static int SUCCESS = 1;
    static int WAIT = 2;

    static int[] Available = new int[M];//当前可分配资源
    static int[][] Max = new int[N][M];//最大需求资源
    static int[][] Allocation = new int[N][M];//已分配数组矩阵
    static int[][] Need = new int[N][M];//还差多少数组分配矩阵
    static ArrayList<Request> queue = new ArrayList<>();//请求队列

    public static void main(String[] args) {

        Allocation = new int[][]{{0, 1, 0}, {2, 0, 0}, {3, 0, 2}, {2, 1, 1}, {0, 0, 2}};
        Need = new int[][]{{7, 4, 3}, {1, 2, 2}, {6, 0, 0}, {0, 1, 1}, {4, 3, 1}};
        Available = new int[]{3, 3, 2};
        Request request = new Request();
        request.id = 0;
        request.num = new int[]{0, 2, 0};
        Scanner scanner = new Scanner(System.in);
        queue.add(request);
        while (queue.size() > 0) {
            if (startSecurityProbe(new int[N][],new int[M],new int[N][M])) {
                switch (startAllocationProbe()){
                    case 0:
                        System.out.println("ERROR!!!");
                        return;
                    case 1:
                        System.out.println("SUCCESS!!!");
                        startAllocation();
                        queue.remove(0);
                        break;
                    case 2:
                        System.out.println("等待其他进程请求加入");
                        String input = scanner.nextLine();
                        String[] split = input.split(",");
                        request.num = new int[split.length];
                        for (int i = 0; i < M; i++) {

                            request.num[i] = Integer.parseInt(split[i]);
                        }
                        request.id = Integer.parseInt(split[M]);
                        queue.add(request);


                }
            }
        }


    }

    private static void startAllocation() {
        allow(M, Available, queue, Allocation, Need);
    }

    private static void allow(int m, int[] available, List<Request> queue, int[][] allocation, int[][] need) {
        for (int k = 0; k < m; k++) {//资源种类
            available[k] = available[k] - queue.get(0).num[k];//开始分配
            allocation[0][k] = allocation[0][k] + queue.get(0).num[k];//分配得到资源增加
            need[0][k] = need[0][k] - queue.get(0).num[k];//当前需求减少
        }
    }

    private static int startAllocationProbe() {
        System.out.println("开始试分配");
        //临时变量组
        int[][] needTemp = new int[N][M];
        int[] availableTemp = new int[M];
        int[][] allocationTemp = new int[N][M];
        //临时变量赋值
        copy(needTemp, availableTemp, allocationTemp,Need,Available,Allocation);

        for (int j = 0; j < M; j++) {//资源种类
            if (queue.get(0).num[j] <= needTemp[queue.get(0).id][j]) {//请求对象与当前剩余需求比较
                if (queue.get(0).num[j] <= availableTemp[j]) {//请求对象与可分配对象比较
                    if (j == M - 1) {
                        System.out.println("当前可分配");
                        System.out.println(Arrays.toString(availableTemp));
                        System.out.println("当前请求");
                        System.out.println(Arrays.toString(queue.get(0).num));

                        allow(j, availableTemp, queue, allocationTemp, needTemp);
                        System.out.println("当前可分配");
                        System.out.println(Arrays.toString(availableTemp));
                        System.out.println("当前资源分布");
                        for (int i = 0; i < N; i++) {
                            System.out.println(Arrays.toString(allocationTemp[i]));
                        }

                        System.out.println("试分配结束,开始安全探测");
                        if (startSecurityProbe(needTemp,availableTemp,allocationTemp)){
                            return SUCCESS;
                        }else {
                            return WAIT;
                        }

                    }
                }else return WAIT;

            } else return ERROR;
        }
        return ERROR;
    }

    private static boolean startSecurityProbe( int[][] needTemp ,int[] availableTemp, int[][] allocationTemp ) {
        System.out.println("\n\n\n*****开始安全分析*****\n\n\n");
        boolean[] Finish = new boolean[N];
        //临时变量组

        //临时变量赋值
        copy(needTemp, availableTemp, allocationTemp, Need,Available,Allocation);

        boolean flag = true;


        while (flag) {
            flag = false;

            for (int i = 0; i < N; i++) {//遍历进程对象
                if (!Finish[i]) {
                    for (int j = 0; j < M; j++) {//遍历资源种类
                        if (needTemp[i][j] <= availableTemp[j]) {
                            if (j == M - 1) {
                                for (int l = 0; l <= j; l++) {
                                    availableTemp[l] = availableTemp[l] + allocationTemp[i][l];//开始分配资源
                                    //allocationTemp[i][l] = 0;表示已经获得资源并且执行完成返回资源
                                }
                                System.out.println(i+"号进程获得资源");
                                Finish[i] = true;//表示资源已经分配
                                flag = true;//表示发生重新分配值,多一次循环探测机会
                            }
                        } else {
                            System.out.println(i+"号进程资源不符合,资源本次循环未分配");
                            break;
                        }
                    }

                }
                System.out.println("当前各进程分配资源");
                for (int j = 0; j < N; j++) {
                    System.out.print(Arrays.toString(allocationTemp[j]));
                    if (j==i){
                        System.out.print("***"+Arrays.toString(availableTemp));
                    }
                    System.out.println();
                }

            }

        }
        System.out.println("\n\n\n*****安全分析结束*****\n\n\n");

        for (boolean b : Finish) {
            if (!b) {
                return false;
            }
        }

        return true;
    }

    private static void copy(int[][] needTemp, int[] availableTemp, int[][] allocationTemp,int[][] needSrc, int[] availableSrc, int[][] allocationSrc) {

        for (int i = 0; i < needSrc.length; i++) {
            needTemp[i] = needSrc[i].clone();
        }
        System.arraycopy(availableSrc, 0, availableTemp, 0, availableSrc.length);
        for (int i = 0; i < allocationSrc.length; i++) {
            allocationTemp[i] = allocationSrc[i].clone();
        }

    }
}
class Request{
    int id;
    int[] num;
}