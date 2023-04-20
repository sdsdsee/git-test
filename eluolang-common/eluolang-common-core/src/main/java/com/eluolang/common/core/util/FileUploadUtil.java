package com.eluolang.common.core.util;

public class FileUploadUtil {
    public static boolean isLinux(){
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }
}
