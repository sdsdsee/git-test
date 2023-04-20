package com.eluolang.physical.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import cn.hutool.core.codec.Base64;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

public class PackageDecompression {
    public  List<File> decompression(String decompressionPath, String dest) {
        //存放压缩文件的地方
        List<File> files = null;
        // 首先创建ZipFile指向磁盘上的.zip文件
        try {
            ZipFile zFile = new ZipFile(decompressionPath);

            zFile.setFileNameCharset("GBK");
            File destDir = new File(dest);// 解压目录
            // 如果解压需要密码
    /*    if (zFile.isEncrypted()) {
            zFile.setPassword("");  // 设置密码
        }*/
            if (!zFile.isValidZipFile()) {
                throw new ZipException("压缩文件格式不正确请使用zip格式的压缩文件或文件已经损坏！");
            }
            zFile.extractAll(dest);
            //读取压缩包中的文件
            List<net.lingala.zip4j.model.FileHeader > headerList = zFile.getFileHeaders();
            files= new ArrayList<File>();
            //读取Zip中的文件并且把地址放入list中
            for(FileHeader fileHeader : headerList) {
                //判断是否文件夹
                if (!fileHeader.isDirectory()) {
                    files.add(new File(destDir,fileHeader.getFileName()));
                }else {
                    System.out.println(fileHeader.getFileName());
                }
            }

        } catch (ZipException e) {
            e.printStackTrace();
        }
        return files;
    }
    public static void copyFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File file = new File(newPath);
        FileInputStream in = new FileInputStream(oldFile);
        FileOutputStream out = new FileOutputStream(file);;
        byte[] buffer=new byte[2097152];
        int readByte = 0;
        while((readByte = in.read(buffer)) != -1){
            out.write(buffer, 0, readByte);
        }
        in.close();
        out.close();
    }
}
