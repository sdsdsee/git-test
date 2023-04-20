package com.eluolang.playground.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSmartClassScheduleUpDataVo {
    @ApiModelProperty(name = "id", value = "", required = true)
    private String id;
    @ApiModelProperty(name = "课程名称", value = "", required = true)
    //课程名称
    private String className;
    //老师id
    @ApiModelProperty(name = "老师id", value = "老师账号id", required = true)
    private int teacherId;
    //课程时间id
    @ApiModelProperty(name = "课程时间id", value = "课程时间id", required = true)
    private String classTimeId;
    //上课开始日期
    @ApiModelProperty(name = "上课开始日期", value = "上课开始日期：2023-02-22", required = true)
    private String concreteStartDate;
    //学校id
    @ApiModelProperty(name = "学校id", value = "学校id：216", required = false)
    private int schoolId;
    //班级id
    @ApiModelProperty(name = "班级id", value = "班级id：8763", required = true)
    private int classId;
    //创建人
    @ApiModelProperty(name = "创建人", notes = "创建人", value = "账号id", required = true)
    private int createBy;
    //周几
    @ApiModelProperty(name = "周几", notes = "周几", value = "周几：1/2/3/4/5/6/7", required = true)
    private Integer week;
    //上课地点
    @ApiModelProperty(name = "上课地点", notes = "上课地点", value = "", required = true)
    private String location;
    //电话号
    @ApiModelProperty(name = "电话号", notes = "电话号", value = "", required = true)
    private String phone;
}
