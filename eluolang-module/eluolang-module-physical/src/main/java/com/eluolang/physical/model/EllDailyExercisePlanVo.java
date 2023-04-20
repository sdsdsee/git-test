package com.eluolang.physical.model;

import com.eluolang.common.core.pojo.EllDailyExercisePlan;
import com.eluolang.common.core.pojo.EllDailyTimeQuantum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllDailyExercisePlanVo implements Serializable, Cloneable {
    private String id;
    /**
     * 训练名称
     */
    private String trainName;
    /**
     * 上半年计划月份
     */
    private String firstHalfYear;
    /**
     * 下半年计划月份
     */
    private String secondHalfYear;
    /**
     * 上半年训练里程
     */
    private Double firstTrainMileage;
    /**
     * 下半年训练里程
     */
    private Double secondTrainMileage;
    /**
     * 备注
     */
    private String remarks;
    /**
     *
     */
    private String createTime;
    /**
     * 使用部门
     */
    private String useOrgId;
    /**
     * 模式1.自由2.测试
     */
    private String type;
    /**
     *
     */
    private int isDelete;
    /**
     * 1没过期2过期
     */
    private int status;
    /**
     * 上半年每周里程
     */
    private Double firstWeeks;
    /**
     * 下半年每周里程
     */
    private Double secondWeeks;
    /**
     * 上半年每月里程
     */
    private Double firstMonth;
    /**
     * 下半年每月里程
     */
    private Double secondMonth;
    /**
     * 文本内容
     */
    private String textContent;
    /**
     * 考核的跑步时间是否显示（0:否,1:是）
     */
    private Integer runTimeDisplay;
    /**
     * 时间段
     */
    private List<EllDailyTimeQuantum> dailyTimeList;

}
