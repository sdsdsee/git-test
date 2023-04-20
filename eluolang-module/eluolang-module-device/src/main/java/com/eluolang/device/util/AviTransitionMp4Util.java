package com.eluolang.device.util;

import com.eluolang.device.vo.SysScreenRecord;

import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.InputFormatException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.MultimediaInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AviTransitionMp4Util {
    public static void main(String[] args) {
        aviToMp4("C:\\Users\\Administrator\\Desktop\\opencv_jar_so\\1655311518804.avi","Q:\\video\\1.mp4");
    }
    /**
     * 将avi视频转换为mp4格式
     * @param oldPath 原视频位置
     * @param newPath mp4视频位置
     */
    public static void aviToMp4(String oldPath, String newPath) {
//		File source = new File("/Users/Desktop/aa/111.avi");
//	    File target = new File("/Users/Desktop/aa/111.mp4");
        File source = new File(oldPath);
        File target = new File(newPath);
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame"); //音频编码格式
        audio.setBitRate(new Integer(800000));
        audio.setChannels(new Integer(1));
        //audio.setSamplingRate(new Integer(22050));
        VideoAttributes video = new VideoAttributes();
        video.setCodec("libx264");//视频编码格式
        video.setBitRate(new Integer(3200000));
        video.setFrameRate(new Integer(5));//数字设置小了，视频会卡顿
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp4");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
        Encoder encoder = new Encoder();
        MultimediaObject multimediaObject = new MultimediaObject(source);
        try {
            System.out.println("avi转MP4 --- 转换开始:"+new Date());
            try {
                encoder.encode(multimediaObject, target, attrs);
            } catch (EncoderException e) {
                e.printStackTrace();
            }
            // 删除avi文件
            source.delete();
            System.out.println("avi转MP4 --- 转换结束:"+new Date());
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }/**
     * 获取视频文件时长
     * @param source
     * @return
     */
    private SysScreenRecord getVideoTime(File source, String fileName, String hostIp , String remoteAddr, String recordTime, String userName) {
        SysScreenRecord sysScreenRecord = new SysScreenRecord();
        sysScreenRecord.setUuid(new Date().getTime()+"");
        sysScreenRecord.setRecordFileName(fileName);
        sysScreenRecord.setRecordTime(recordTime);
        sysScreenRecord.setUserName(userName);
        sysScreenRecord.setHostIp(hostIp);
        sysScreenRecord.setRecordFileAddr(remoteAddr);
        sysScreenRecord.setCreateUser("sss");
        sysScreenRecord.setCreateDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        FileChannel fc = null;
        try {
            MultimediaObject multimediaObject = new MultimediaObject(source);
            MultimediaInfo m = multimediaObject.getInfo();
            FileInputStream fis = new FileInputStream(source);
            fc = fis.getChannel();
            long millisecond = m.getDuration();//毫秒数
            System.out.println(millisecond);
            // 小时
            int hour = (int) millisecond / (60 * 60 * 1000);
            // 分钟
            int minute = (int) (millisecond % (60 * 60 * 1000)) / 60000;
            // 秒
            int second = (int) ((millisecond % (60 * 60 * 1000)) % 60000) / 1000;
            // 视频大小
            sysScreenRecord.setRecordFileSize(new BigDecimal(fc.size()).divide(new BigDecimal(1048576), 2, RoundingMode.HALF_UP));
            //        size = fileSize.divide(new BigDecimal(1048576), 2, RoundingMode.HALF_UP) + "MB";
            // 视频格式
            sysScreenRecord.setRecordFileFormat(m.getFormat());
            sysScreenRecord.setRecordDuration(hour + ":" + minute + ":" + second);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fc) {
                try {
                    fc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return sysScreenRecord;
    }


}
