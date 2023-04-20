package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchFaceVo {
    //身份证
    private String idCard;
    //人脸图片地址
    private String faceImagePath;
}
