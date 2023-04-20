package com.eluolang.platform.vo;

import com.eluolang.common.core.pojo.EllInfoPublishTarget;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSelInfoVo {
    /**
     * id
     */
    private String id;
    /**
     * 信息标题
     */
    private String infoTitle;
    /**
     * 信息类型1.图片2.视频3.文字
     */
    private Integer infoType;
    /**
     * 发布状态
     */
    private Integer state;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 是否删除1是2否
     */
    private Integer isDelete;
    /**
     * 间隔时间
     */
    private Integer intervalTime;
    /**
     * 信息为文字时候的内容
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)//为null或者‘’ 不返回给前端
    private String text;
    /**
     * 文件
     */
    private List<EllFIleVo> fileUrl;
    /**
     * 设备id
     */
    private List<EllInfoPublishTarget> devices;
}
