package com.eluolang.physical.model;

public class EachProjectScoreVo {
    /** 项目id */
    private Integer proId;
    /** 项目名称 */
    private String proName;
    /** 10-30分人数 */
    private String oneScore;
    /** 30-60分人数 */
    private String twoScore;
    /** 60-100分人数 */
    private String threeScore;

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getOneScore() {
        return oneScore;
    }

    public void setOneScore(String oneScore) {
        this.oneScore = oneScore;
    }

    public String getTwoScore() {
        return twoScore;
    }

    public void setTwoScore(String twoScore) {
        this.twoScore = twoScore;
    }

    public String getThreeScore() {
        return threeScore;
    }

    public void setThreeScore(String threeScore) {
        this.threeScore = threeScore;
    }
}
