package com.eluolang.module.report.vo;

import java.util.List;

public class VisionDailyVo {
    /** 左眼裸眼视力 */
    private List<DataBean> leftVision;
    /** 右眼裸眼视力 */
    private List<DataBean> rightVision;
    /** 左眼屈光度(球镜度) */
    private List<DataBean> leftDiopterSph;
    /** 右眼屈光度(球镜度) */
    private List<DataBean> rightDiopterSph;
    /** 左眼屈光度(柱镜度) */
    private List<DataBean> leftDiopterCyl;
    /** 右眼屈光度(柱镜度) */
    private List<DataBean> rightDiopterCyl;
    /** 左眼屈光度(轴位) */
    private List<DataBean> leftDiopterAx;
    /** 右眼屈光度(轴位) */
    private List<DataBean> rightDiopterAx;
    /** 瞳距 */
    private List<DataBean> pupilDistance;
    /** 身高 */
    private List<DataBean> height;
    /** 体重 */
    private List<DataBean> weight;
    /** BMI值 */
    private List<DataBean> bmi;

    public List<DataBean> getLeftVision() {
        return leftVision;
    }

    public void setLeftVision(List<DataBean> leftVision) {
        this.leftVision = leftVision;
    }

    public List<DataBean> getRightVision() {
        return rightVision;
    }

    public void setRightVision(List<DataBean> rightVision) {
        this.rightVision = rightVision;
    }

    public List<DataBean> getLeftDiopterSph() {
        return leftDiopterSph;
    }

    public void setLeftDiopterSph(List<DataBean> leftDiopterSph) {
        this.leftDiopterSph = leftDiopterSph;
    }

    public List<DataBean> getRightDiopterSph() {
        return rightDiopterSph;
    }

    public void setRightDiopterSph(List<DataBean> rightDiopterSph) {
        this.rightDiopterSph = rightDiopterSph;
    }

    public List<DataBean> getLeftDiopterCyl() {
        return leftDiopterCyl;
    }

    public void setLeftDiopterCyl(List<DataBean> leftDiopterCyl) {
        this.leftDiopterCyl = leftDiopterCyl;
    }

    public List<DataBean> getRightDiopterCyl() {
        return rightDiopterCyl;
    }

    public void setRightDiopterCyl(List<DataBean> rightDiopterCyl) {
        this.rightDiopterCyl = rightDiopterCyl;
    }

    public List<DataBean> getLeftDiopterAx() {
        return leftDiopterAx;
    }

    public void setLeftDiopterAx(List<DataBean> leftDiopterAx) {
        this.leftDiopterAx = leftDiopterAx;
    }

    public List<DataBean> getRightDiopterAx() {
        return rightDiopterAx;
    }

    public void setRightDiopterAx(List<DataBean> rightDiopterAx) {
        this.rightDiopterAx = rightDiopterAx;
    }

    public List<DataBean> getPupilDistance() {
        return pupilDistance;
    }

    public void setPupilDistance(List<DataBean> pupilDistance) {
        this.pupilDistance = pupilDistance;
    }

    public List<DataBean> getHeight() {
        return height;
    }

    public void setHeight(List<DataBean> height) {
        this.height = height;
    }

    public List<DataBean> getWeight() {
        return weight;
    }

    public void setWeight(List<DataBean> weight) {
        this.weight = weight;
    }

    public List<DataBean> getBmi() {
        return bmi;
    }

    public void setBmi(List<DataBean> bmi) {
        this.bmi = bmi;
    }

    public static class DataBean{
        /** 日期 */
        private String dateTime;
        /** 数值 */
        private String num;

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }
}
