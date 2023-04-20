package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllScoreVerifyVo implements Serializable {
    /**
     * 计划id
     */
    private String planId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 是否核实
     */
    private int isVerify;
    /**
     * 是否为考试
     */
    private int isExam;
    /**
     * 人脸照片
     */
    private String faceImageId;
    /**
     * 签名截屏文件id
     */
    private String signatureImageId;

}