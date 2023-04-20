package com.eluolang.platform.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllInfoFileVo {
    /**
     * id
     */
    private String id;
    /**
     * 文件id
     */
    private String fileId;
    /**
     * 信息id
     */
    private String infoId;
    /**
     * 间隔时间
     */
    private Integer intervalTime;
    /**
     * 文件地址
     */
    private String fileUrl;
}
