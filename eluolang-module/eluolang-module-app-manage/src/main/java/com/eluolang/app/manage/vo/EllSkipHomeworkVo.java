package com.eluolang.app.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSkipHomeworkVo implements Serializable, Cloneable {
    /**
     * id
     */
    private String id;
    /**
     * 创建者id
     */
    private String createBy;
    /**
     * 家庭作业名称
     */
    private String workName;
    /**
     * 完成地点
     */
    private String workPlace;
    /**
     * 完成形式
     */
    private String finishModality;
    /**
     * 作业模式
     */
    private int workPattern;
    /**
     * 作业内容
     */
    private String workContent;
    /**
     * 作业设置如多少个数多少时间
     */
    private int workSetting;
    /**
     * 老师名字
     */
    private String teacherName;
    /**
     * 使用部门id
     */
    private String useOrgId;
    /**
     * 状态1未发布2发布中3结束
     */
    private int state;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 页码
     */
    private int page;
    /**
     * 是否完成
     */
    private int isFinish;
}