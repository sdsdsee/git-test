package com.eluolang.playground.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllClassHistoryVo {   //id
    @ApiModelProperty(notes = "id", required = false)
    private String id;
    //人员id
    @ApiModelProperty(notes = "人员id", required = true)
    private String userId;
    //课程id
    @ApiModelProperty(notes = "课程id", required = false)
    private String courseId;
    //设备编号
    @ApiModelProperty(notes = "设备编号", required = true)
    private String createByDevice;
    //老师账号id
    @ApiModelProperty(notes = "老师账号id", required = true)
    private String teacherId;
    //创建时间
    @ApiModelProperty(notes = "创建时间", required = false)
    private String createTime;
    //修改人
    @ApiModelProperty(notes = "修改人", required = false)
    private int updateBy;
    //修改时间
    @ApiModelProperty(notes = "修改时间", required = false)
    private String updateTime;
    //成绩，表示项目是否有多个成绩组成一个成绩比如：身高体重有身高和体重
    @ApiModelProperty(notes = "成绩", required = true)
    private List<EllChancesVo> chances;
    //性别
    @ApiModelProperty(notes = "性别", required = true)
    private int userSex;
    //年级
    @ApiModelProperty(notes = "年级", required = false)
    private String grade;
    //部门
    @ApiModelProperty(notes = "部门", required = true)
    private String orgId;
    //项目id
    @ApiModelProperty(notes = "项目id", required = true)
    private int proId;
    //项目名称
    @ApiModelProperty(notes = "项目名称", required = true)
    private String proName;
    //重测
    @ApiModelProperty(notes = "id", required = false)
    private int isRetest;
    //成绩类型
    @ApiModelProperty(notes = "成绩类型1锻炼模式2课堂模式", required = true)
    private int type;
    //拓展数据
    @ApiModelProperty(notes = "拓展数据", required = false)
    private String extraData;
    /**
     * 检录时间戳
     */
    //检录时间戳
    @ApiModelProperty(notes = "检录时间戳", required = true)
    private Long checkInTimestamp;
    /**
     * 结束时间蹉
     */
    @ApiModelProperty(notes = "结束时间蹉", required = true)
    private Long endTimestamp;
    /**
     * 用时
     */
    private Long timeSpent;
}
