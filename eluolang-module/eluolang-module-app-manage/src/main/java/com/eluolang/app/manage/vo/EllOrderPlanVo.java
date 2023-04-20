package com.eluolang.app.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EllOrderPlanVo implements Serializable {
    private String id;
    /**
     * 计划标题
     */
    private String planTitle;
    /**
     * 计划实行地址
     */
    private String planAddr;
    /**
     * 开始时间
     */
    private String dateBegin;
    /**
     * 结束时间
     */
    private String dateEnd;
    /**
     * 说明
     */
    private String planRemark;
    /**
     * 预约人数
     */
    private int authNum;
    /**
     * 已预约人数
     */
    private int num;
    /**
     * 预约状态
     */
    private int state;
    /**
     * 免测状态
     */
    private int avoidState;
}
