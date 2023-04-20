package com.eluolang.physical.model;

public class RetestNumberVo {
    /** 项目id */
    private Integer proId;
    /** 项目名称 */
    private String proName;
    /** 重测人数 */
    private String retestNumber;

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

    public String getRetestNumber() {
        return retestNumber;
    }

    public void setRetestNumber(String retestNumber) {
        this.retestNumber = retestNumber;
    }
}
