package com.eluolang.physical.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CmdUtil implements Runnable {

    @Override
    public void run() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        //定义命令内容
        List<String> command = new ArrayList<>();
        command.add("bash");
        command.add("/home/eluolang/server/jar/doEllFace.sh");
        processBuilder.command(command);
        System.out.println("脚本：" + command.toString());
        //将标准输入流和错误输入流合并，通过标准输入流读取信息
        processBuilder.redirectErrorStream(true);
        //启动进程
        try {
           processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
