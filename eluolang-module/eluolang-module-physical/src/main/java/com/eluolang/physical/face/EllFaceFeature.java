/*
package com.eluolang.physical.face;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Base64;


//ell人脸生成
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllFaceFeature {
    public  float[] data;
    public String id = "";
    public double score = 0;

    public EllFaceFeature(float[] data) {
        this.data = data;
    }

    public EllFaceFeature(String id, String base64) {
        this.id = id;
        byte[] bytes = Base64.getDecoder().decode(base64);
        FloatBuffer buf = ByteBuffer.wrap(bytes).asFloatBuffer();
        float[] floatArray = new float[buf.limit()];
        buf.get(floatArray);
        this.data = floatArray;
    }

    public String base64() {
        ByteBuffer buf = ByteBuffer.allocate(Float.SIZE / Byte.SIZE * data.length);
        buf.asFloatBuffer().put(data);
        return Base64.getEncoder().encodeToString(buf.array());
    }

    public double cosineSimilar(EllFaceFeature faceFeature) {
        Mat a = new Mat(data);
        Mat b = new Mat(faceFeature.data);
        return a.dot(b) / (norm(a) * norm(b));
    }
}

*/
