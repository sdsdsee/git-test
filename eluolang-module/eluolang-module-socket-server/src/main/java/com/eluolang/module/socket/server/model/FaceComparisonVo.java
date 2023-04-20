package com.eluolang.module.socket.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2023年02月07日 10:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaceComparisonVo {
    /** 抓拍与底库中人脸比对的相似度 */
    private float searchScore;
    /** 人脸的唯一 ID，用于获取分组中人脸照片 */
    private String faceToken;
}
