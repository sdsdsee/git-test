package com.eluolang.platform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年05月31日 15:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryFileVo {
    /**
     * 文件id
     */
    private String fileId;
    /**
     * 文件路径
     */
    private String fileUrl;
    /**
     * 间隔时间
     */
    private Integer intervalTime;
}
