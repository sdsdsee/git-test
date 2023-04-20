package com.eluolang.common.core.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllCourseware {
    //id
    @ApiModelProperty(name = "id",value = "",required = false)
    private String id;
    //课件名称
    @ApiModelProperty(name = "课件名称",value = "课件名称：篮球ppt", required = true)
    private String coursewareName;
    //老师id（账号id）
    @ApiModelProperty(name = "老师id（账号id）",value = "老师id（账号id）：1", required = true)
    private int teacherId;
    //创建时间
    @ApiModelProperty(name = "创建时间",value = "创建时间:2023-02-22", required = false)
    private String createTime;
    //文件地址
    @ApiModelProperty(name = "文件地址",value = "", required = false)
    private String fileUrl;
    //类型
    @ApiModelProperty(name = "类型",value = "类型:1:图片,2:视频,3:excel,4:ppt,5:word,6:其他", required = true)
    private int type;
}
