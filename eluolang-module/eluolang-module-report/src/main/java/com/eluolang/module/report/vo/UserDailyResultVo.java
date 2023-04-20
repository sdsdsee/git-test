package com.eluolang.module.report.vo;

public class UserDailyResultVo {
    /** 用户id */
    private String id;
    /** 用户姓名 */
    private String userName;
    /** 左眼裸眼视力 */
    private String leftVision;
    /** 右眼裸眼视力 */
    private String rightVision;
    /** 视力测试结果 */
    private String visionComment;
    /** 身高体重测试结果 */
    private String bmiComment;

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

    public String getLeftVision() {
        return leftVision;
    }

    public void setLeftVision(String leftVision) {
        this.leftVision = leftVision;
    }

    public String getRightVision() {
        return rightVision;
    }

    public void setRightVision(String rightVision) {
        this.rightVision = rightVision;
    }

    public String getVisionComment() {
        return visionComment;
    }

    public void setVisionComment(String visionComment) {
        this.visionComment = visionComment;
    }

    public String getBmiComment() {
        return bmiComment;
    }

    public void setBmiComment(String bmiComment) {
        this.bmiComment = bmiComment;
    }
}
