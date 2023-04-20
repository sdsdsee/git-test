package com.eluolang.physical.util;

import com.eluolang.common.core.util.StringUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Test4 {


    public static void main(String[] args) {
       /* int[] arr = new int[30];
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * 100);
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        quickSort(arr, 0, arr.length - 1);
        System.out.println("\n排序后数组如下：");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }*/
        int a = 17, b = 189;
        double s = a / Double.parseDouble(b + "") * 100;
        //格式化数字为两位
        DecimalFormat sdf = new DecimalFormat("###.00");
        s = Double.parseDouble(sdf.format(s));
        System.out.println(s);

    }

    public static void quickSort(int arr[], int left, int right) {
        if (left > right) return;
        int tmp = arr[left]; //基准值(一般为首个）
        int i = left;
        int j = right;
        while (i != j) { //当i与j 相遇时，循环结束
            while (arr[j] >= tmp && j > i) //先从右边向左走，如果遇到小于基准值的数，则j不动
                j--;
            while (arr[i] <= tmp && j > i) //i向前走，遇到大于基准值的数，i不动
                i++;
            if (j > i) { //当i与j都找到相应的数后，i与j指向的数值互换
                int t = arr[i];
                arr[i] = arr[j];
                arr[j] = t;
            }
        }
        /*当i=j时，说明i与j相遇
         * 此时将a[i]与一开始定义的基准值交换
         * 从而保证了 基准值的右边都比基准值大，左边都比基准值小
         * */
        arr[left] = arr[i];
        arr[i] = tmp;

        quickSort(arr, left, i - 1);//对左边进行排序
        quickSort(arr, i + 1, right);//右边
    }


    public static String toHexString(String s) {

        String str = "";

        for (int i = 0; i < s.length(); i++) {

            int ch = (int) s.charAt(i);

            String s4 = Integer.toHexString(ch);

            str = str + s4;

        }

        return str;

    }

    public static void getFileName() throws IOException {
        String path = "D:\\EllTestImg\\新建文件夹"; // 路径
        File f = new File(path);//获取路径  F:\测试目录
        if (!f.exists()) {
            System.out.println(path + " not exists");//不存在就输出
            return;
        }

        //接收文件
        List<File> files = Arrays.asList(f.listFiles());
        for (int i = 0; i < files.size(); i++) {//循环遍历
            File fs = files.get(i);//获取数组中的第i个
            if (fs.isDirectory()) {
                System.out.println(fs.getName() + " [目录]");//如果是目录就输出
            } else {
                System.out.println(fs.getPath());//否则直接输出
                System.out.println();
                String A = fs.getName();
                String a = "http://192.168.3.3/D:\\EllTestImg\\Batch\\0ef70f37-2dc4-4abd-b10b-e721c2b7d24f\\新建文件夹\\8a28f03d-649d-4eab-8405-072548f424b4.jpeg.zip";
                System.out.println(a.substring(a.indexOf("\\")));
                System.out.println(fs.getName().substring(fs.getName().lastIndexOf(".") + 1));
            }
        }
    }
}
