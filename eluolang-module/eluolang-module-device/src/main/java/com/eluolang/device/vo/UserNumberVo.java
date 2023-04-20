package com.eluolang.device.vo;

public class UserNumberVo {

    /**
     * 总人数
     */
    private Integer totalNumber;
    /**
     * 男生人数
     */
    private Integer manNumber;
    /**
     * 女生人数
     */
    private Integer womanNumber;

    public Integer getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public Integer getManNumber() {
        return manNumber;
    }

    public void setManNumber(Integer manNumber) {
        this.manNumber = manNumber;
    }

    public Integer getWomanNumber() {
        return womanNumber;
    }

    public void setWomanNumber(Integer womanNumber) {
        this.womanNumber = womanNumber;
    }
}
