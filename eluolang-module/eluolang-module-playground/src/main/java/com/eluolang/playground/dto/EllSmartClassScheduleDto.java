package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSmartClassScheduleDto {
    //id
    private String id;
    //课程名称
    private String name;
    //老师id
    private int teacherId;
    //老师名称
    private String teacherName;
    //上课具体开始时间
    private String concreteStart;
    //上课具体开始时间蹉
    private Long beginAt;
    //上课具体结束时间
    private String concreteEnd;
    //上课具体结束时间蹉
    private Long endAt;
    //学校id
    private int schoolId;
    //班级id
    private int classId;
    //班级名称
    private String className;
    //周几
    private Integer week;
    //节数
    private int Index;
    //上课地点
    private String address;
}
