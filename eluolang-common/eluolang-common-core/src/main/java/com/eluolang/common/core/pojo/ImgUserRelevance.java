package com.eluolang.common.core.pojo;

public class ImgUserRelevance {
    /**
     * 学生id
     */
    private String userId;
    /**
     * 文件id
     */
    private String fileImgId;
    /**
     * 人脸特征值
     */
    private String faceInfo;
    /**
     * ai人脸特征值
     */
    private String ellFace;

    public String getEllFace() {
        return ellFace;
    }

    public void setEllFace(String ellFace) {
        this.ellFace = ellFace;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFileImgId() {
        return fileImgId;
    }

    public void setFileImgId(String fileImgId) {
        this.fileImgId = fileImgId;
    }

    public String getFaceInfo() {
        return faceInfo;
    }

    public void setFaceInfo(String faceInfo) {
        this.faceInfo = faceInfo;
    }
}
