package com.eluolang.app.manage.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class PathGeneration {
    public static void createPath(String path) {
        File filepath = new File(path);
        //判断是否有文件路径
        if (!filepath.getParentFile().exists()) {
            //不存在就新建
            filepath.getParentFile().mkdirs();
        }
    }

    public static void main(String[] args) {
        createPath("/home/EllTestImg/");
    }
}
