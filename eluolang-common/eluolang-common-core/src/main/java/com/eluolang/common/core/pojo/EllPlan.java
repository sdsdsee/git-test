package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EllPlan implements Serializable {
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
     * 删除标记
     */
    private int isDelete;
    /**
     * 状态(1待发布2正在进行3已结束
     */
    private int planState;
    /**
     * 创建人路径
     */
    private String orgPath;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 说明
     */
    private String planRemark;
    /**
     * 测试项目
     */
    private String proId;
    /**
     * 使用的规则
     */
    private String useRules;
    /**
     * 是否需要重测1是2否
     */
    private int isResurvey;
    /**
     * 是否需要监考员登陆1是2否
     */
    private int isAuthentication;
    /**
     * 预约总人数
     */
    private int authNum;
    /**
     * 是否需要预约
     */
    private String isSubscribe;
    /**
     * 创建人id
     */
    private String createById;
    /**
     * 创建部门id
     */
    private String orgId;
    /**
     * 是否是测试
     */
    private int isExam;
    /**
     * 自选项目数量
     */
    private int customizeTheNumber;
    /***
     * 小程序是否允许查看成绩
     */
    private int isView;
}
