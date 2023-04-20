package com.eluolang.module.sys.user.util;

public class FileUploadUtil {
    public static boolean isLinux(){
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }
}
