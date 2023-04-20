package com.eluolang.common.core.pojo;

/**
 * @author dengrunsen
 * @date 2022年06月22日 16:01
 */
public class ToiletEvaluate {
    /** 主键id */
    private String id;
    /** 设备id */
    private String deviceId;
    /** 评价星级 */
    private Integer evaluateLevel;
    /** 创建时间 */
    private String createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getEvaluateLevel() {
        return evaluateLevel;
    }

    public void setEvaluateLevel(Integer evaluateLevel) {
        this.evaluateLevel = evaluateLevel;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
