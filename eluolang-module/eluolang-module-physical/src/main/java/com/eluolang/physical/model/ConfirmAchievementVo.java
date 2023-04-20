package com.eluolang.physical.model;

public class ConfirmAchievementVo {
    /** 确认成绩总人数 */
    private String totalNumber;
    /** 确认成绩女生人数 */
    private String manNumber;
    /** 确认成绩男生人数 */
    private String womanNumber;

    public String getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(String totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getManNumber() {
        return manNumber;
    }

    public void setManNumber(String manNumber) {
        this.manNumber = manNumber;
    }

    public String getWomanNumber() {
        return womanNumber;
    }

    public void setWomanNumber(String womanNumber) {
        this.womanNumber = womanNumber;
    }
}
