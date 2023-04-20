package com.eluolang.common.core.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllInformation implements Serializable, Cloneable {
    /**
     * id
     */
    @ApiModelProperty(name = "id", notes = "")
    private String id;
    /**
     * 信息标题
     */
    @ApiModelProperty(name = "信息标题", notes = "")
    private String infoTitle;
    /**
     * 信息类型1.图片2.视频3.文字
     */
    @ApiModelProperty(name = "信息类型1.图片2.视频3.文字", notes = "")
    private Integer infoType;
    /**
     * 发布状态
     */
    @ApiModelProperty(name = "发布状态", notes = "")
    private Integer state;
    /**
     * 间隔时间
     */
    @ApiModelProperty(name = "间隔时间", notes = "")
    private Integer intervalTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "创建时间", notes = "")
    private String createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(name = "修改时间", notes = "")
    private String updateTime;
    /**
     * 版本号
     */
    @ApiModelProperty(name = "版本号", notes = "")
    private String version;
    /**
     * 创建人
     */
    @ApiModelProperty(name = "创建人", notes = "")
    private int createBy;
    /**
     * 信息为文字时候的内容
     */
    @ApiModelProperty(name = "信息为文字时候的内容", notes = "")
    private String text;
    /**
     * 是否删除1是2否
     */
    @ApiModelProperty(name = "是否删除1是2否", notes = "")
    private Integer isDelete;
}