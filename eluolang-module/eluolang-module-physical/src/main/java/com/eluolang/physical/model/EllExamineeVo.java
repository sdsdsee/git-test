package com.eluolang.physical.model;

public class EllExamineeVo {
    /** id */
    private String id;
    /** 考生分组id */
    private String groupId;
    /** 学生id */
    private String stuId;
    /** 考号 */
    private String testNumber;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 性别:1男2女
     */
    private Integer userSex;
    /**
     * 密码
     */
    private String userPwd;
    /**
     * 微信的openId
     */
    private String wxOpenId;
    /**
     * 所属部门标识
     */
    private String orgId;
    /**
     * 删除标识0未删除1已删除
     */
    private String isDelete;
    /**
     * 出生日期
     */
    private String userBirth;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 家庭地址
     */
    private String homeAddress;
    /**
     * 创建人ID
     */
    private String createBy;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 入学时间
     */
    private String enTime;
    /**
     * 电话号
     */
    private String phone;
    /**
     * 名族
     */
    private String ethnic;
    /**
     * 是否已认证
     */
    private String isAuthentication;
    /**
     * 入学年级
     */
    private String enGrade;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(String testNumber) {
        this.testNumber = testNumber;
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

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
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
}
