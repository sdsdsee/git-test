package com.eluolang.common.core.util;


import com.alibaba.fastjson.JSON;
import com.eluolang.common.core.constant.Constants;
import com.eluolang.common.core.constant.FileConstants;
import com.eluolang.common.core.constant.SystemStatus;
import com.eluolang.common.core.exception.BaseException;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

//import org.bytedeco.javacv.FFmpegFrameGrabber;
//import org.bytedeco.javacv.Java2DFrameConverter;

/**
 * 文件处理工具类
 *
 * @author suziwei
 * @date 2020/9/4
 */
public class FileUtils {
    /**
     * 将文件转换成Byte数组
     *
     * @param pathStr 文件路径
     * @return byte
     */
    public static byte[] getBytesByFile(String pathStr) {
        File file = new File(pathStr);
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Byte数组转换成文件
     *
     * @param bytes
     * @param fileName
     * @param filePath
     */
    public static void getFileByBytes(byte[] bytes, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            /** 判断文件目录是否存在 */
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 将Byte数组转换成文件
     *
     * @param bytes
     * @param fileName
     * @param filePath
     */
    public static void getFileByBytesUnix(byte[] bytes, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            /** 判断文件目录是否存在 */
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            bos.write("\n".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 字符串转图片
     *
     * @param imgStr         图片字符串
     * @param outPutFilePath 图片名
     * @return boolean
     * @throws IOException IO Exception
     */
    public static boolean generateImage(final String imgStr, final String outPutFilePath) throws IOException {
        if (imgStr == null) {
            return false;
        }
        OutputStream out = null;
        try {
            final Base64.Decoder decoder = Base64.getDecoder();
            //解码
            byte[] b = decoder.decode(imgStr);
            out = new FileOutputStream(outPutFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 图片转字符串.
     *
     * @param stream 文件路径
     * @return String
     */
    public static String getImageStr(final InputStream stream) {
        if (stream == null) {
            return SystemStatus.ERROR;
        }
        InputStream inputStream = stream;
        byte[] data = null;
        try {
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Base64.Encoder encoder = Base64.getEncoder();
        //编码
        final String encodedText = encoder.encodeToString(data);
        System.out.println(encodedText);
        return encodedText;
    }

    /**
     * 流转字符串.
     *
     * @param inputStream 流
     * @return String
     */
    public static String intputStreamString2(InputStream inputStream) {
        if (inputStream == null) {
            return SystemStatus.ERROR;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        try {
            while ((i = inputStream.read()) != -1) {
                baos.write(i);
            }
            return baos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param fi 原始图片
     * @param fo 压缩后的图片
     * @param nw 压缩后的长度
     */
    public static void compress(File fi, File fo, int nw) {
        if (fi == null || fo == null || nw == 0) {
            return;
        }
        try {
            /*
            AffineTransform 类表示 2D 仿射变换，它执行从 2D 坐标到其他 2D
            坐标的线性映射，保留了线的“直线性”和“平行性”。可以使用一系
            列平移、缩放、翻转、旋转和剪切来构造仿射变换。
            */
            AffineTransform transform = new AffineTransform();
            //读取图片
            BufferedImage bis = ImageIO.read(fi);
            int w = bis.getWidth();
            int h = bis.getHeight();
            //double scale = (double)w/h;
            int nh = (nw * h) / w;
            double sx = (double) nw / w;
            double sy = (double) nh / h;
            //setToScale(double sx, double sy) 将此变换设置为缩放变换。
            transform.setToScale(sx, sy);
            /*
             * AffineTransformOp类使用仿射转换来执行从源图像或 Raster 中 2D 坐标到目标图像或
             *  Raster 中 2D 坐标的线性映射。所使用的插值类型由构造方法通过
             *  一个 RenderingHints 对象或通过此类中定义的整数插值类型之一来指定。
            如果在构造方法中指定了 RenderingHints 对象，则使用插值提示和呈现
            的质量提示为此操作设置插值类型。要求进行颜色转换时，可以使用颜色
            呈现提示和抖动提示。 注意，务必要满足以下约束：源图像与目标图像
            必须不同。 对于 Raster 对象，源图像中的 band 数必须等于目标图像中
            的 band 数。
            */
            AffineTransformOp ato = new AffineTransformOp(transform, null);
            BufferedImage bid = new BufferedImage(nw, nh, BufferedImage.TYPE_3BYTE_BGR);
            /*
             * TYPE_3BYTE_BGR 表示一个具有 8 位 RGB 颜色分量的图像，
             * 对应于 Windows 风格的 BGR 颜色模型，具有用 3 字节存
             * 储的 Blue、Green 和 Red 三种颜色。
             */
            ato.filter(bis, bid);
            ImageIO.write(bid, "jpeg", fo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  根据路径删除指定的目录或文件，无论存在与否
     *@param sPath  要删除的目录或文件
     *@return 删除成功返回 true，否则返回 false。
     */
    public static boolean DeleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   sPath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0;i<files.length;i++){        //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
//    /**
//     * 截取视频指定帧生成gif
//     *
//     * @param videofile  视频文件
//     * @param startFrame 开始帧
//     * @param frameCount 截取帧数
//     * @param frameRate  帧频率（默认：3）
//     * @param margin     每截取一次跳过多少帧（默认：3）
//     * @throws IOException 截取的长度超过视频长度
//     */
//    public static void buildGif(String videofile, int startFrame, int frameCount, Integer frameRate, Integer margin) throws IOException {
//        if (videofile == null) {
//            return;
//        }
//        if (margin == null) {
//            margin = 3;
//        }
//        if (frameRate == null) {
//            frameRate = 3;
//        }
//        FileOutputStream targetFile = new FileOutputStream(videofile.substring(0, videofile.lastIndexOf(".")) + ".gif");
//        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videofile);
//        Java2DFrameConverter converter = new Java2DFrameConverter();
//        ff.start();
//        try {
//            if (startFrame > ff.getLengthInFrames() & (startFrame + frameCount) > ff.getLengthInFrames()) {
//                throw new RuntimeException("视频太短了");
//            }
//            ff.setFrameNumber(startFrame);
//            AnimatedGifEncoder en = new AnimatedGifEncoder();
//            en.setFrameRate(frameRate);
//            en.start(targetFile);
//            for (int i = 0; i < frameCount; i++) {
//                en.addFrame(converter.convert(ff.grab()));
//                ff.setFrameNumber(ff.getFrameNumber() + margin);
//            }
//            en.finish();
//        } finally {
//            ff.stop();
//            ff.close();
//        }
//    }


    /**
     * @param videofilePath 视频文件路径带文件名
     * @return base64
     */
    public static String videoToBase64(File videofilePath) {
        long size = videofilePath.length();
        byte[] imageByte = new byte[(int) size];
        FileInputStream fs = null;
        BufferedInputStream bis = null;
        try {
            fs = new FileInputStream(videofilePath);
            bis = new BufferedInputStream(fs);
            bis.read(imageByte);
        } catch (FileNotFoundException e) {
            throw new BaseException("文件" + videofilePath.getName() + "不能被找到：{}");
        } catch (IOException e) {
            throw new BaseException("byte转换BASE64出错：" + e.getMessage());
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    throw new BaseException("关闭输入流出错：" + e.getMessage());
                }
            }
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    throw new BaseException("关闭输入流出错：" + e.getMessage());
                }
            }
        }
        return (new BASE64Encoder()).encode(imageByte);
    }

    /**
     * base64 转 视频
     * videoFilePath  输出视频文件路径带文件名
     */
    public static void base64ToVideo(String base64, String videoFilePath) {
        try {
            //base解密
            byte[] videoByte = new sun.misc.BASE64Decoder().decodeBuffer(base64);
            File videoFile = new File(videoFilePath);
            //检测上传的文件目录是否存在
            if (!videoFile.getParentFile().exists()) {
                //不存在则新建文件夹
                videoFile.getParentFile().mkdirs();
            }
            //输入视频文件
            FileOutputStream fos = new FileOutputStream(videoFile);
            fos.write(videoByte, 0, videoByte.length);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            throw new BaseException("base64转换为视频异常");
        }
    }

    /**
     * base64 转 文件
     * videoFilePath  输出文件路径带文件名
     */
    public static File base64ToFile(String base64, String videoFilePath) {
        File videoFile;
        try {
            //base解密
            byte[] videoByte = new sun.misc.BASE64Decoder().decodeBuffer(base64);
            videoFile = new File(videoFilePath);
            //检测上传的文件目录是否存在
            if (!videoFile.getParentFile().exists()) {
                //不存在则新建文件夹
                videoFile.getParentFile().mkdirs();
            }
            //输入视频文件
            FileOutputStream fos = new FileOutputStream(videoFile);
            fos.write(videoByte, 0, videoByte.length);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            throw new BaseException("base64转换为文件异常");
        }
        return videoFile;
    }

    public static String getBaseImg(String imgPath) {
        File file = new File(imgPath);
        //检测上传的文件目录是否存在
        if (!file.exists()) {
            return "";
        }
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    /**
     * String 转 Base64
     *
     * @param str
     * @return
     */
    public static String strConvertBase(String str) {
        if (null != str) {
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(str.getBytes());
        }
        return null;
    }

    public static String baseConvertStr(String str) {
        if (null != str) {
            Base64.Decoder decoder = Base64.getDecoder();
            try {
                return new String(decoder.decode(str.getBytes()), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
        return null;
    }

//    public static boolean pngToJpg(String pngUrl, String fileId) {
//        BufferedImage bufferedImage;
//        boolean flag = true;
//        try {
//            // read image file
//            bufferedImage = ImageIO.read(new File(pngUrl));
//            // create a blank, RGB, same width and height, and a white
//            // background
//            BufferedImage newBufferedImage = new BufferedImage(
//                    bufferedImage.getWidth(), bufferedImage.getHeight(),
//                    BufferedImage.TYPE_INT_RGB);
//            // TYPE_INT_RGB:创建一个RBG图像，24位深度，成功将32位图转化成24位
//            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
//                    Color.WHITE, null);
//            // write to jpeg file
//            ImageIO.write(newBufferedImage, "jpg", new File(Constants.HOUSEHOLD_FACE_ADDRESS + "/" + fileId + FileConstants.IMAGE_JPG));
//        } catch (IOException e) {
//            flag = false;
//            e.printStackTrace();
//        }
//        return flag;
//    }

    /**
     * 按顺序查找数组中的元素下标
     * @param array
     * @param key
     * @return
     */
    public static int findNum(String[] array,String key){
        for (int i = 0; i <array.length ; i++) {
            if(array[i].equals(key)){
                return i;
            }
        }
        return -1;
    }
    public static void main(String[] args) {
        String[] aa = {"650","550","679","978"};
        int index1 = findNum(aa,"679");
    System.out.println(JSON.toJSONString(index1));
    }
}
