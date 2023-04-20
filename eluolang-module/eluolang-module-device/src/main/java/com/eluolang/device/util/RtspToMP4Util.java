package com.eluolang.device.util;

import com.eluolang.device.controller.DeviceController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.InterruptibleChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO:
 *
 * @Author: zhao
 * @create: 2021/8/27 16:11
 */
@Component
@Slf4j
public class RtspToMP4Util  {
    public static void main(String[] args) throws InterruptedException {
        RtspToMP4Util.start start = new RtspToMP4Util.start("ffmpeg", "rtsp://admin:85353507@192.168.3.115:554/ch0/sub/av_stream", "Q:\\video\\grade\\fd2aa06784fd46e8af812d7c8426a615\\b1034b22498e4a8c960de38b59e6f814\\41\\234.MP4", "123");
        Thread startVideo = new Thread(start);
        startVideo.start();
    }


    //启动ffmpeg拉流
    //线程掉线程蛀牙是因为让参数先返回
    public static class start implements Runnable {
        private String ffmpegPath;
        private String streamUrl;
        private String FilePath;

        private String deviceId;


        public start(String ffmpegPath, String streamUrl, String FilePath, String deviceId) {
            this.ffmpegPath = ffmpegPath;
            this.streamUrl = streamUrl;
            this.FilePath = FilePath;
            this.deviceId = deviceId;
        }

        @Override
        public void run() {

            StartRecord(ffmpegPath, streamUrl, FilePath, deviceId);
//            log.info("停止了");
        }
    }

    public static Process StartRecord(String ffmpegPath, String streamUrl, String FilePath, String deviceId) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        //定义命令内容
        List<String> command = new ArrayList<>();
        command.add(ffmpegPath);
        //command.add("-rtsp_transport");
        //command.add("tcp");
        command.add("-i");
        command.add(streamUrl);
//        command.add("\""+streamUrl+"\"");
//        command.add("-tune zerolatency");
//        command.add("-c");
//        command.add("copy");
//        command.add("-f");
//        command.add("mp4");
//        command.add("libx264");
        command.add("-t");
        command.add("00:30:00");
        command.add(FilePath);
        command.add("-y");
        processBuilder.command(command);

        System.out.println("脚本：" + command.toString());
        //将标准输入流和错误输入流合并，通过标准输入流读取信息
        processBuilder.redirectErrorStream(true);
        try {
            //启动进程
            Process processThis = processBuilder.start();
            //把启动的录制的摄像头进行存储
            if (DeviceController.map.get(deviceId) == null) {
                //初始化map
                List<Process> processList = new ArrayList<>();
                processList.add(processThis);
                DeviceController.map.put(deviceId, processList);
            } else {
                DeviceController.map.get(deviceId).add(processThis);
            }
            // System.out.println("开始时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
            //获取输入流
            InputStream inputStream = processThis.getInputStream();
           /* Thread inThread = new Thread(new In(inputStream));
            inThread.start();*/
            //转成字符输入流
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            int len = -1;
            char[] c = new char[1024];
            //读取进程输入流中的内容
            while ((len = inputStreamReader.read(c)) != -1) {
                String s = new String(c, 0, len);
//                System.out.println(len);
//                  System.out.print(s);
//                log.info(String.valueOf(DeviceController.startThread.get(deviceId).isStart()));
//                System.out.println(inputStream.available());

            }
            log.info(deviceId + ":停止了");
            return processThis;
        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println(e);
            log.info(deviceId + ":停止了");
        }
        return null;
    }

    //写流线程
   /* public static class In implements Runnable {
        private InputStream inputStream;

        public In(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            try {
                //转成字符输入流
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                int len = -1;
                char[] c = new char[1024];
                //读取进程输入流中的内容
                while ((len = inputStreamReader.read(c)) != -1) {
                    String s = new String(c, 0, len);
                    //  System.out.print(s);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

    //停止ffmpeg
    public static class stop implements Runnable {
        private Process process;

        public stop(Process process) {
            this.process = process;
        }

        @Override
        public void run() {
            stopRecord(process);
        }
    }

    public static boolean stopRecord(Process process) {
        try {
            //延迟
//            Thread.sleep(500);
            //获取输入流
//            InputStream inputStream = process.getInputStream();
           /* Thread inThread = new Thread(new In(inputStream));
            inThread.start();*/
            //转成字符输入流
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            OutputStream os = process.getOutputStream();
            os.write("q".getBytes());
            // 一定要刷新
            os.flush();
            os.close();
//            inputStreamReader.close();
//            inputStream.close();
            log.info("结束");
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        }
        return true;
    }
}
