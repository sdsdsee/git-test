package com.eluolang.app.manage.util;

import java.io.File;

public class PathGenerationUtil {
    public static void createPath(String path) {
        File filepath = new File(path);
        //判断是否有文件路径
        if (!filepath.getParentFile().exists()) {
            //不存在就新建
            filepath.getParentFile().mkdirs();
        }
    }
}