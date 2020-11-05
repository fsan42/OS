package cn.fr.os.work.two;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {
    static int[] t = new int[2];
    public static void main(String[] args) {
        ArrayList<int[]> list1 = new ArrayList<>();
        ArrayList<int[]> list2 = new ArrayList<>();
        int[] nums = {1, 1, 1};
        list1.add(nums);
        System.out.println(list1);
        for (int[] num : list1) {
            list2.add(num.clone());
        }
        System.out.println(list2);


    }
}
