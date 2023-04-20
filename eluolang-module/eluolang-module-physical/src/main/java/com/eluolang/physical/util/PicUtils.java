package com.eluolang.physical.util;

import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 图片压缩Utils
 *
 * @author worstEzreal
 * @version V1.1.0
 * @date 2018/3/12
 */
public class PicUtils {


    private static Logger logger = LoggerFactory.getLogger(PicUtils.class);

    public static void main(String[] args) throws Exception {
        File file = new File("C:\\Users\\Administrator\\Desktop\\26f5e2901edee5693d19d6122206b62.jpg");

        byte[] bytes= compressPicForScale(file, 200, file.getName());
        ByteToFile(bytes,file);
    }

    /**
     * 根据指定大小压缩图片
     *
     * @param imgFile     图片文件
     * @param desFileSize 指定图片大小，单位kb
     * @param imageId     影像编号
     * @return 压缩质量后的图片字节数组
     */
    public static byte[] compressPicForScale(File imgFile, long desFileSize, String imageId) throws Exception {
        //把文件转换为Bytes
        byte[] imageBytes = fileToByte(imgFile);
        if (imageBytes == null || imageBytes.length <= 0 || imageBytes.length < desFileSize * 1024) {
            return imageBytes;
        }
        ByteArrayInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        long srcSize = imageBytes.length;
        double accuracy = getAccuracy(srcSize / 1024);
        try {
            while (imageBytes.length > desFileSize * 1024) {
                inputStream = new ByteArrayInputStream(imageBytes);
                outputStream = new ByteArrayOutputStream(imageBytes.length);
                Thumbnails.of(inputStream).scale(accuracy).outputQuality(accuracy).toOutputStream(outputStream);
                imageBytes = outputStream.toByteArray();
            }
            inputStream.close();
            outputStream.close();
            logger.info("【图片压缩】imageId={} | 图片原大小={}kb | 压缩后大小={}kb", imageId, srcSize / 1024, imageBytes.length / 1024);
        } catch (Exception e) {
            logger.error("【图片压缩】msg=图片压缩失败!", e);
        }
        //把Bytes转换为文件
        ByteToFile(imageBytes, imgFile);
        return imageBytes;
    }

    /**
     * 自动调节精度(经验数值)
     *
     * @param size 源图片大小
     * @return 图片压缩质量比
     */
    private static double getAccuracy(long size) {
        double accuracy;
        if (size < 900) {
            accuracy = 0.85;
        } else if (size < 2047) {
            accuracy = 0.6;
        } else if (size < 3275) {
            accuracy = 0.44;
        } else {
            accuracy = 0.4;
        }
        return accuracy;
    }

    //图片转byte[]
    public static byte[] fileToByte(File img) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            BufferedImage bi;
            bi = ImageIO.read(img);
            ImageIO.write(bi, "png", baos);//不管输出什么格式图片，此处不需改动
            byte[] bytes = baos.toByteArray();
            System.err.println(bytes.length);
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            baos.close();

        }
        return null;
    }

    //byte[]转图片
    static void ByteToFile(byte[] bytes, File imgFile) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        BufferedImage bi1 = ImageIO.read(bais);
        try {
            ImageIO.write(bi1, "png", imgFile);//不管输出什么格式图片，此处不需改动
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bais.close();
        }
    }
}