package com.eluolang.device.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllGradeVideoVo implements Serializable, Cloneable {
    /**
     * 租户号
     */
    private String id;
    /**
     * 计划id
     */
    private String planId;
    /**
     * 学生id
     */
    private String userIds;
    /**
     * 项目id
     */
    private int projectId;
    /**
     * 文件id
     */
    private String fileId;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 开始时间
     */
    private String beginTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 是否为重测视频
     */
    private String isRetest;
    /**
     * fps
     */
    private String fps;

    @Override
    public Object clone() {
        EllGradeVideoVo ellGradeVideoVo = null;
        try {
            ellGradeVideoVo = (EllGradeVideoVo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return ellGradeVideoVo;
    }
}