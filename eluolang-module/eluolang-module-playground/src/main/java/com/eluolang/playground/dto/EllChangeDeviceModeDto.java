package com.eluolang.playground.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EllChangeDeviceModeDto {
    @ApiModelProperty(name = "设备ID", value = "设备ids", required = true)
    //所控制的设备Id，逗号分割
    private String toDevices;
    @ApiModelProperty(name = "模式", value = "1:日常锻炼模式;2:课堂模式;3:体考模式", required = true)
    //模式；1:日常锻炼模式;2:课堂模式;3:体考模式
    private int mode;
    //模式名
    @ApiModelProperty(name = "模式名", value = "模式名", required = true)
    private String modeName;
    //部门id[课堂模式]
    @ApiModelProperty(name = "部门id", value = "部门id[课堂模式]", required = false)
    private String orgId;
    //部门名称[课堂模式]
    @ApiModelProperty(name = "部门名称", value = "部门名称[课堂模式]", required = false)
    private String orgName;
    //老师id[课堂模式]
    @ApiModelProperty(name = "老师id", value = "老师id[课堂模式]", required = false)
    private String teacherId;
    //老师姓名[课堂模式]
    @ApiModelProperty(name = "老师姓名", value = "老师姓名[课堂模式]", required = false)
    private String teacherName;
    //课程id[课堂模式]
    @ApiModelProperty(name = "课程id", value = "课程id[课堂模式]", required = false)
    private String courseId;
    //类型1自由跑测试2课堂测试[课堂模式]
    @ApiModelProperty(name = "课堂类型", value = "1自由跑测试2课堂测试[课堂模式]", required = false)
    private int courseType;
    //计划id[体考/体测模式]
    @ApiModelProperty(name = "计划id", value = "计划id[体考/体测模式]", required = false)
    private String planId;
    //计划标题[体考/体测模式]
    @ApiModelProperty(name = "计划标题", value = "计划标题[体考/体测模式]", required = false)
    private String planTitle;

}
