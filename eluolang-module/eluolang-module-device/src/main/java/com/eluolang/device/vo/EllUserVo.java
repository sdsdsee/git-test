package com.eluolang.device.vo;

public class EllUserVo {
    private String id;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 性别:1男2女
     */
    private Integer userSex;
    /**
     * 微信的openId
     */
    private String wxOpenId;
    /**
     * 所属部门标识
     */
    private String orgId;
    /**
     * 部门名称
     */
    private String orgName;
    /**
     * 删除标识0未删除1已删除
     */
    private String isDelete;
    /**
     * 出生日期
     */
    private String userBirth;
    /**
     * 学号
     */
    private String studentId;
    /**
     * 学籍号
     */
    private String studentCode;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 家庭地址
     */
    private String homeAddress;
    /**
     * 入学时间
     */
    private String enTime;
    /**
     * 电话号
     */
    private String phone;
    /**
     * 是否已认证
     */
    private String isAuthentication;
    /**
     * 入学年级
     */
    private String enGrade;
    /**
     * 文件id
     */
    private String fileImgId;
    /**
     * 人脸特征值
     */
    private String faceInfo;
    /**
     * 上传时间
     */
    private String uploadTime;
    /**
     * 文件类型
     */
    private Integer type;
    /**
     * 文件地址
     */
    private String fileUrl;
    /**
     * 文件大小
     */
    private long size;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 对称加密
     */
    private String idCardSymmetry;
    /*
    * ell人脸
    * */
    public String ellFace;

    public String getEllFace() {
        return ellFace;
    }

    public void setEllFace(String ellFace) {
        this.ellFace = ellFace;
    }

    public String getIdCardSymmetry() {
        return idCardSymmetry;
    }

    public void setIdCardSymmetry(String idCardSymmetry) {
        this.idCardSymmetry = idCardSymmetry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserSex() {
        return userSex;
    }

    public void setUserSex(Integer userSex) {
        this.userSex = userSex;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(String userBirth) {
        this.userBirth = userBirth;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getEnTime() {
        return enTime;
    }

    public void setEnTime(String enTime) {
        this.enTime = enTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIsAuthentication() {
        return isAuthentication;
    }

    public void setIsAuthentication(String isAuthentication) {
        this.isAuthentication = isAuthentication;
    }

    public String getEnGrade() {
        return enGrade;
    }

    public void setEnGrade(String enGrade) {
        this.enGrade = enGrade;
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

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
