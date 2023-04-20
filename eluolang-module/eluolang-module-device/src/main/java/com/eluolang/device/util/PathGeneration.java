package com.eluolang.device.util;

import java.io.File;

public class PathGeneration {
    public static void createPath(String path){
        File filepath = new File(path);
        //判断是否有文件路径
        if (!filepath.getParentFile().exists()) {
            //不存在就新建
            filepath.getParentFile().mkdirs();
        }
    }

    public static void main(String[] args) {
//        createPath("Q:\\video\\2.mp4");
        File filepath = new File("Q:\\video\\1.mp4");
        System.out.println(filepath.exists());
    }
}
