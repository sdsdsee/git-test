package com.eluolang.platform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllLedInformationVo implements Serializable, Cloneable {
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
     * 发布状态1未发部2已发布3已结束
     */
    private Integer state;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 内容（文字/图片）
     */
    private String content;
    /**
     * 发布图片的id
     */
    private String imageId;
    /**
     * fileUrl
     */
    private String fileUrl;
    private int page;
}