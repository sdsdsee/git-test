package com.eluolang.platform.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllInformationVo implements Serializable, Cloneable {
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
     * 间隔时间
     */
    private String deviceIds;
    private String text;
    private List<String> fileIds;
}