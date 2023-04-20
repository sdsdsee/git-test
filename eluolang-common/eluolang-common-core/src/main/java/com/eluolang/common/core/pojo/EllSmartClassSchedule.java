package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSmartClassSchedule {
    //id
    private String id;
    //课程名称
    private String className;
    //老师id
    private int teacherId;
    //课程时间id
    private String classTimeId;
    //上课具体开始时间
    private String concreteStart;
    //上课具体开始时间蹉
    private Long concreteStampStart;
    //上课具体结束时间
    private String concreteEnd;
    //上课具体结束时间蹉
    private Long concreteStampEnd;
    //学校id
    private int schoolId;
    //班级id
    private int classId;
    //创建时间
    private String createTime;
    //修改时间
    private String updateTime;
    //创建人
    private int createBy;
    //周几
    private Integer week;
    //日期
    private String dateTime;
    //上课地点
    private String location;
    //电话号
    private String phone;
}
