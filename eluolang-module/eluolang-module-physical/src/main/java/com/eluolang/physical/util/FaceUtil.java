package com.eluolang.physical.util;


import cn.hutool.core.collection.CollectionUtil;
import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.enums.ImageFormat;
import com.arcsoft.face.toolkit.ImageInfo;
import cn.hutool.extra.spring.SpringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.arcsoft.face.toolkit.ImageFactory.bufferedImage2ImageInfo;

/**
 * 人脸识别
 *
 * @author zfy
 */
@Slf4j
public class FaceUtil {

    /**
     * 解析人脸识别码
     *
     * @return
     */
    /**
     * appid
     */
    //  @Value("${user.hongruan.appId}")
//    public static String appId = "G7ekNBhsgRHw8nj7L8fR6aPAvMHLrneVjtKVcKGK9HFy"; //windows
//    public static String appId = "Dccg3NKGWAqdVC6SpxsVvSSNaxD5sobKi3CU4WvQrDok"; //linux
    /**
     * 密钥
     */
    //   @Value("${user.hongruan.secret}")
//    public static String secret = "65Yun1u1NT5cUFicYbKxzaoQTYPfSbA4nKDzAczKVhqq"; //windows
//    public static String secret = "G7gk458jvUaFeypmLDBcwedauwvvBtNrqF4CcJUGahSV"; //linux

    /**
     * 库文件地址
     */
    // @Value("${user.hongruan.path}")
//    public static String path = "D:\\FaceRecognition\\libs\\WIN64"; //windows
//    public static String path = "/mnt/data/arc_soft/libs/LINUX64"; //linux
    //每次使用都激活一次
    /*public static FaceEngine initFaceEngine() {
        FaceEngine faceEngine = new FaceEngine(path);

        // 激活引擎
        int activeCode = faceEngine.activeOnline(appId, secret);
        if (activeCode != ErrorInfo.MOK.getValue() && activeCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            throw new RuntimeException("引擎激活失败 code:" + activeCode);
        }
        // 引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);

        // 功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(true);
        functionConfiguration.setSupportFace3dAngle(true);
        functionConfiguration.setSupportFaceDetect(true);
        functionConfiguration.setSupportFaceRecognition(true);
        functionConfiguration.setSupportGender(true);
        functionConfiguration.setSupportLiveness(true);
        functionConfiguration.setSupportIRLiveness(true);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);

        // 初始化引擎
        int initCode = faceEngine.init(engineConfiguration);

        if (initCode != ErrorInfo.MOK.getValue()) {
            throw new RuntimeException("初始化引擎失败 code:" + initCode);
        }
        log.info("虹软引擎初始化成功");
        return faceEngine;
    }*/
    public static byte[] resolveFaceCode(InputStream in) {
        FaceEngine faceEngine = SpringUtil.getBean(FaceEngine.class);
//        FaceEngine faceEngine = initFaceEngine();
        // 识别
        // ImageInfo imageInfo = getRGBData(file);
        ImageInfo imageInfo = getRGBData(in);
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        int detectCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
        if (CollectionUtil.isEmpty(faceInfoList)) {
            throw new RuntimeException("图片检测人脸识别失败 code:" + detectCode);
        }
        // 特征提取
        FaceFeature faceFeature = new FaceFeature();
        int extractCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);
        if (extractCode != ErrorInfo.MOK.getValue()) {
            throw new RuntimeException("人脸特征提取失败 code:" + extractCode);
        }
        //faceEngine.unInit();//卸载
        return faceFeature.getFeatureData();
    }

    /**
     * 在人脸特征库中搜索，获取相似度最高者的下标
     *
     * @param faceFeature     人脸特征数据
     * @param faceFeatureList 人脸特征库
     * @return 相似度最高者的下标
     */
    /*
     * public int searchFace(FaceFeature faceFeature, List<FaceFeature>
     * faceFeatureList) { FaceEngine faceEngine = new FaceEngine(""); FaceSimilar
     * faceSimilar = new FaceSimilar(); float maxSimilar = 0; int maxSimilarIndex =
     * -1; for (int i = 0; i < faceFeatureList.size(); i++) { //
     * 同faceEngine.compareFaceFeature(faceFeature, faceFeatureList.get(i),
     * CompareModel.LIFE_PHOTO, faceSimilar); int compareCode =
     * faceEngine.compareFaceFeature(faceFeature, faceFeatureList.get(i),
     * faceSimilar); if (compareCode == ErrorInfo.MOK) { if (faceSimilar.getScore()
     * > maxSimilar) { maxSimilar = faceSimilar.getScore(); maxSimilarIndex = i; } }
     * else { log.info( "compare failed, code is : " + compareCode); } } return
     * maxSimilarIndex; }
     */
    //流
    public static ImageInfo getRGBData(InputStream in) {
        if (null == in) return null;
        ImageInfo imageInfo;
        try {
            // 将图片文件加载到内存缓冲区
            BufferedImage image = ImageIO.read(in);
            imageInfo = bufferedImage2ImageInfo(image);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return imageInfo;
    }

    //字节
    public static ImageInfo getRGBData(byte[] bytes) {
        return bytes == null ? null : getRGBData((InputStream) (new ByteArrayInputStream(bytes)));
    }

    public static void main(String[] args) throws IOException {
        // 读取图片
        BufferedImage bufImage = ImageIO.read(new File("D:\\EllTestImg\\2.jpeg"));

        // 获取图片的宽高
        final int width = bufImage.getWidth();
        final int height = bufImage.getHeight();
        // 读取出图片的所有像素
        int[] rgbs = bufImage.getRGB(0, 0, width, height, null, 0, width);

        BufferedImage bufferedImage = bufImage.getSubimage(96, 120, 290, 330);
        // 把水平镜像后的像素矩阵设置回 bufImage
        /*        bufImage.setRGB(0, 0, width, height, rgbs, 0, width);*/

        // 把修改过的 bufImage 保存到本地
        ImageIO.write(bufferedImage, "JPEG", new File("D:\\EllTestImg\\2.jpeg"));
    }

}
