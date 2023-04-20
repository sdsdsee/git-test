package com.eluolang.module.report.vo;

public class PersonalVisionReportVo {
    /** 学生姓名 */
    private String userName;
    /** 性别 */
    private Integer userSex;
    /** 学校名称 */
    private String schoolName;
    /** 班级名称 */
    private String className;
    /** 头像路径 */
    private String fileUrl;

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

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
