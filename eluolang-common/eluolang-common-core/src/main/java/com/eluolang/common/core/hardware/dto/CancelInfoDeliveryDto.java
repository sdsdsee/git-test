package com.eluolang.common.core.hardware.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author dengrunsen
 * @date 2022年05月25日 15:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelInfoDeliveryDto {
    /**
     * "cmd": 15,
     * "ident": xxxxxx,//指令标识
     * "deviceId": "xxxxxxxxxx",
     * "deliveryId":"xxxxxxxxxx",
     * "fileInfo":  [{
     *    "fileId": "xxxxxxxx",
     *    "fileName": "xxxxxx",
     *    "fileType": x,
     *   },
     *   {
     *    "fileId": "xxxxxxxx",
     *    "fileName": "xxxxxx",
     *    "fileType": x,
     *  }
     * ]
     */

    /** 发送指令 */
    private int cmd;
    /** 指令标识 */
    private int ident;
    /** 设备ID */
    private String deviceId;
    /** 信息发布ID */
    private String deliveryId;
    /** 1:图片,2:视频,3.文本*/
    private Integer type;
    /** 文件信息 */
    private List<CancelInfoDeliveryDto.DataBean> fileInfo;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataBean{
        /** 文件id */
        private String fileId;
        /** 文件名称 */
        private String fileName;
        /** 1:图片(限png,jpg,jpeg)，2:视频(mp4) */
        private Integer fileType;
    }
}
