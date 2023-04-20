package com.eluolang.common.core.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSmartClassTime {
    //ID
    @ApiModelProperty(name = "id", required = false)
    private String id;
    //第几节课
    @ApiModelProperty(name = "第几节课", value = "第几节课:1", required = true)
    private String classNum;
    //上课开始时间
    @ApiModelProperty(name = "上课开始时间", value = "上课开始时间", required = true)
    private String timeStart;
    //上课开始时间蹉
    @ApiModelProperty(notes = "上课开始时间蹉", value = "上课开始时间", required = false)
    private Long timeStampStart;
    //上课结束时间
    @ApiModelProperty(name = "上课结束时间", value = "上课结束时间", required = true)
    private String timeEnd;
    //上课结束时间蹉
    @ApiModelProperty(notes = "上课结束时间蹉", value = "上课结束时间", required = false)
    private Long timeStampEnd;
    //学校id
    @ApiModelProperty(name = "学校id", value = "学校id", required = true)
    private String schoolId;
    //创建时间
    @ApiModelProperty(name = "创建时间", value = "创建时间", required = false)
    private String createTime;
    //修改时间
    @ApiModelProperty(name = "修改时间", value = "修改时间", required = false)
    private String updateTime;
    //创建人
    @ApiModelProperty(name = "创建人", value = "创建人", required = false)
    private String createBy;
}
