package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllFaceVo {
    private String imageId;
    private String faceInfo;
    private String userId;
    private String ellFace;
    private String megviiImageId;
}
