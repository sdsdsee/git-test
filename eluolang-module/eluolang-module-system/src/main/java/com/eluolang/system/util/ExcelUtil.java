package com.eluolang.system.util;

public class ExcelUtil {
    public static boolean isLinux(){
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }
    public static boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))){
                return false;  //非数字
            }
        }
        return true;
    }
    public static void main(String args[]){
//        boolean isOK = isNumber("300100110E0001");
//        if (isOK == false) {
//            System.out.println("非数字");
//        }else {
//            System.out.println("数字");
//        }
//        System.out.println(isIDNumber("440303198402131586"));
        System.out.println(System.getProperty("os.name").toLowerCase().contains("linux"));
    }
}
