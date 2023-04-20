package com.eluolang.module.socket.server.util;

import java.io.File;

/**
 * 上传文件工具类
 * @author renzhixing
 */
public class UploadUtils {

    private final static String FILE_PATH = "/upload/files";

    public static File getImgDirFile(){
        String fileDirPath = new String(FILE_PATH);
        File fileDir = new File(fileDirPath);
        if (!fileDir.exists()){
            //递归生成文件夹
            fileDir.mkdirs();
        }
        return fileDir;
    }


}
