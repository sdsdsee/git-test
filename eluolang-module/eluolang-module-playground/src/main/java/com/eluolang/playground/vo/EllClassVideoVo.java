package com.eluolang.playground.vo;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllClassVideoVo {
    //id
    @ApiModelProperty(notes = "id", required = false)
    private String id;
    //学生id
    @ApiModelProperty(notes = "学生id", required = true)
    private String userIds;
    //课程id
    @ApiModelProperty(notes = "课程id", required = false)
    private String courseId;
    //项目id
    @ApiModelProperty(notes = "项目id", required = true)
    private int projectId;
    //视频类型
    @ApiModelProperty(notes = "视频类型1锻炼模式视频2课堂模式视频", required = true)
    private int mode;
    //视频地址
    @ApiModelProperty(notes = "视频访问地址", required = false)
    private String videoPath;
    //上传日期
    @ApiModelProperty(notes = "上传日期", required = false)
    private String createTime;

}
