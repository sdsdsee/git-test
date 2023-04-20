/*
package com.eluolang.physical.face;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OrtSession.Result;
import ai.onnxruntime.OrtSession.SessionOptions;
import ai.onnxruntime.OrtSession.SessionOptions.OptLevel;
import org.bytedeco.javacpp.indexer.UByteRawIndexer;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

import javax.validation.constraints.Max;
import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.*;

//ell人脸生成
public class FaceHelper {

    private final CascadeClassifier faceCascade;
    private final OrtEnvironment env;
    private final OrtSession session;

    private static FaceHelper instance;

    public static FaceHelper getInstance(String haarPath, String modelPath) throws Exception {
        if (instance == null) {
            instance = new FaceHelper(haarPath, modelPath);
        }
        return instance;
    }

    private FaceHelper(String haarPath, String modelPath) throws Exception {

        if (!new File(haarPath).exists()) {
            throw new Exception("Not Exist " + haarPath);
        }

        if (!new File(modelPath).exists()) {
            throw new Exception("Not Exist " + modelPath);
        }

        faceCascade = new CascadeClassifier(haarPath);

        env = OrtEnvironment.getEnvironment();
        OrtSession.SessionOptions opts = new SessionOptions();
        opts.setOptimizationLevel(OptLevel.BASIC_OPT);
        session = env.createSession(modelPath, opts);
    }

    */
/**
     * 将mat转为float数组
     *//*

    private static float[][][][] blobFromImageWithNCHW(Mat mat) {
        int h = mat.arrayHeight();
        int w = mat.arrayWidth();
        float[][][][] blob = new float[1][3][h][w];
        UByteRawIndexer indexer = mat.createIndexer();

        float std = 127.5f;
        float mean = 127.5f;

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                blob[0][0][i][j] = (indexer.get(i, j, 0) - mean) / std;
                blob[0][1][i][j] = (indexer.get(i, j, 1) - mean) / std;
                blob[0][2][i][j] = (indexer.get(i, j, 2) - mean) / std;
            }
        }
        return blob;
    }

    */
/**
     * 通过头像特征码
     *
     * @param imagePath 提取出的头像图片
     *//*

    EllFaceFeature getFeature(String imagePath) throws OrtException {
        return getFeature(imread(imagePath));
    }

    */
/**
     * 通过头像特征码
     *
     * @param mat 提取出的头像图片
     *//*

    EllFaceFeature getFeature(Mat mat) throws OrtException {

        Mat resizeMat = new Mat();
        resize(mat, resizeMat, new Size(112, 112));

        String inputName = session.getInputNames().iterator().next();
        try (OnnxTensor inputTensor = OnnxTensor.createTensor(env, blobFromImageWithNCHW(resizeMat));
             Result output = session.run(Collections.singletonMap(inputName, inputTensor))) {
            float[][] features = (float[][]) output.get(0).getValue();
            return new EllFaceFeature(features[0]);
        }
    }

    */
/**
     * 检测人脸并获取特征码
     *
     * @param imageFile 任意（包含人的）图片
     *//*

    public EllFaceFeature detectFaceAndGetFeature(String imageFile) throws Exception {

        Mat mat = imread(imageFile);
        Mat gray = new Mat();
        cvtColor(mat, gray, COLOR_BGRA2GRAY); //The camera is a color image, so first grayscale
        equalizeHist(gray, gray); //equalize histogram

        RectVector faces = new RectVector();
        faceCascade.detectMultiScale(gray, faces);

        int index = -1;
        int maxArea = 0;
        for (int i = 0; i < faces.size(); i++) {
            int area = faces.get(i).area();
            if (maxArea < area) {
                index = i;
                maxArea = area;
            }
        }
        if (index == -1) {
            throw new Exception("无法检测到人脸 " + index);
        }
        Rect rect = faces.get(index);
        Mat avatar = new Mat(mat, rect);

//         rectangle(mat, rect, new Scalar(0, 255, 0, 1));
//         imwrite(imageFile.replace(".jpg", "_out.jpg"), avatar);

        return getFeature(avatar);
    }

    */
/**
     * 查找特征码
     *//*

    public void find(EllFaceFeature faceFeature, EllFaceFeature[] featuresInDb) {

        double maxDistance = 0;
        int index = 0;
        for (int i = 0; i < featuresInDb.length; i++) {
            EllFaceFeature f = featuresInDb[i];
            double distance = faceFeature.cosineSimilar(f);// 余弦相似距离
            if (maxDistance < distance) {
                index = i;
                maxDistance = distance;
            }
        }
        if (maxDistance > 0.3) {
            faceFeature.id = featuresInDb[index].id;
            faceFeature.score = maxDistance;
        }
    }

    public EllFaceFeature find(EllFaceFeature faceFeature, List<EllFaceFeature> featuresInDb) {

        double maxDistance = 0;
        int index = 0;
        for (int i = 0; i < featuresInDb.size(); i++) {
            EllFaceFeature f = featuresInDb.get(i);
            double distance = faceFeature.cosineSimilar(f);// 余弦相似距离
            if (maxDistance < distance) {
                index = i;
                maxDistance = distance;
            }
        }
        if (maxDistance > 0.3) {
            faceFeature.id = featuresInDb.get(index).id;
            faceFeature.score = maxDistance;
        }
        return faceFeature;
    }
}
*/
