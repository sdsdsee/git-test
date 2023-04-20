package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllScoreVerify implements Serializable {
    /**
     * id
     */
    private String id;
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
     * 人脸照片
     */
    private String faceImageId;
    /**
     * 签字周浦
     */
    private String signatureImageId;

}