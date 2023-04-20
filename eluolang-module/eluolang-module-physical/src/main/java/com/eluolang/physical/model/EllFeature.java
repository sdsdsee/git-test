package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllFeature {
    //数据库人脸特征
    private String faceInfo;
    //用户id
    private String userId;
}
