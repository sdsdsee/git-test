package com.eluolang.platform.util;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
@Slf4j
public class FFmpegUtil extends Thread {
    private String deviceId;
    private String rtsp;
    private String ip;

    public FFmpegUtil(String deviceId, String rtsp, String ip) {
        this.deviceId = deviceId;
        this.rtsp = rtsp;
        this.ip = ip;
    }

    public static void exeCmd(String commandStr) {
        try {
            // 执行命令行
            Process process = Runtime.getRuntime().exec(commandStr);
            /** 关闭 */
            printProcessMsg(process);
            /** cmd执行完后程序继续执行 */
            process.waitFor();
            /** 销毁cmd */
            process.destroy();
            log.info("CameraServiceImpl==cmd==end:" + commandStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // 调用ffmpeg切片rtsp流的命令行
        String commandStr = "ffmpeg -re -rtsp_transport tcp -i " + rtsp + " -f flv -vcodec libx264 -vprofile baseline -acodec aac -strict experimental -ar 44100 -ac 2 -b:a 96k -r 25 -b:v 500k -s 640*480 -f flv -q 10 " + ip + "live/" + deviceId;
        // 调用执行命令方法
        System.out.println(commandStr);
        FFmpegUtil.exeCmd(commandStr);
    }

    /**
     * 处理process输出流和错误流，防止进程阻塞，在process.waitFor();前调用
     *
     * @param exec
     * @throws IOException
     */
    private static void printProcessMsg(Process exec) throws IOException {
        /** 防止ffmpeg进程塞满缓存造成死锁 */
        InputStream error = exec.getErrorStream();
        InputStream is = exec.getInputStream();
        StringBuffer result = new StringBuffer();
        String line = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(error, "GBK"));
            BufferedReader br2 = new BufferedReader(new InputStreamReader(is, "GBK"));
            while ((line = br.readLine()) != null) {
                result.append(line + "\n");
            }
//            log.error("FFMPEG视频转换进程错误信息：" + result.toString());
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            error.close();
            is.close();
        }
    }

    public static void main(String[] args) {
        // 开始线程
        new FFmpegUtil("SSS", "rtsp://admin:ell85353507@192.168.3.78:554/ch0/sub/av_stream", "rtmp://192.168.3.10:9938/").start();
    }

}