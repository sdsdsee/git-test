package com.eluolang.platform.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author dengrunsen
 * @date 2022年05月31日 15:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoDeliveryFileVo {
    /**
     * 信息发布id
     */
    private String infoId;
    /**
     * 信息标题
     */
    private String infoTitle;
    /**
     * 信息类型1.图片2.视频3.文字
     */
    private Integer infoType;
    /**
     * 间隔时间
     */
    private Integer intervalTime;
    /**
     * 版本号
     */
    private String version;
    /**
     * 信息为文字时候的内容
     */
    private String text;
    /**
     * 设备发布信息时间
     */
    private String releaseTime;
    /**
     * 发布等级(值越小优先级越高)
     */
    private Integer releaseOrder;
    /**
     * 文件信息
     */
    private List<DeliveryFileVo> fileInfo;
}
