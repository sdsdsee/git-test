package com.eluolang.physical.model;

public class PlanEachScoreVo {
    /** 10-20分人数 */
    private String oneScore;
    /** 20-40分人数 */
    private String twoScore;
    /** 40-60分人数 */
    private String threeScore;
    /** 60-80分人数 */
    private String fourScore;
    /** 80-100分人数 */
    private String fiveScore;

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

    public String getFourScore() {
        return fourScore;
    }

    public void setFourScore(String fourScore) {
        this.fourScore = fourScore;
    }

    public String getFiveScore() {
        return fiveScore;
    }

    public void setFiveScore(String fiveScore) {
        this.fiveScore = fiveScore;
    }
}
