package com.eluolang.common.core.hardware.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 信息发布
 *
 * @author dengrunsen
 * @date 2020/9/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoDeliveryDto {
    /**
     * "cmd": 10,
     * "ident": xxxxxx,//指令标识
     * "deviceId": "xxxxxxxxxx",
     * "deliveryId":"xxxxxxxxxx",
     * "deliveryType": x,
     * "fileInfo":  [{
     *    "fileId": "xxxxxxxx",
     *    "fileName": "xxxxxx",
     *    "fileType": x,
     *    "timeInterval”: x,
     *    "pri", x
     *   },
     *   {
     *    "fileId": "xxxxxxxx",
     *    "fileName": "xxxxxx",
     *    "fileType": x,
     *    "timeInterval": x
     *    "pri", x
     *  }
     * ],
     * "text": "xxxxxxxx",
     * "startTime":xxxxxxxxxxxxxxx,
     * "endTime":xxxxxxxxxxxxxxx,
     * "startTimeEveryDay":"20:08:09",
     * "endTimeEveryDay":"22:08:09"
     */

    /** 发送指令 */
    private int cmd;
    /** 指令标识 */
    private int ident;
    /** 设备ID */
    private String deviceId;
    /** 信息发布ID */
    private String deliveryId;
    /** 1:轮播， 2:静态(只能传入一张图片或视频)，-1:不传 */
    private Integer deliveryType;
    /** 文件信息 */
    private List<DataBean> fileInfo;
    /** 发布的文字信息(没有则为"") */
    private String text;
    /** 开始时间 */
    private long startTime;
    /** 结束时间 */
    private long endTime;
    /** 每天开始时间 */
    private String startTimeEveryDay;
    /** 每天结束时间 */
    private String endTimeEveryDay;
    /** 播放优先级(值越小优先级越大) */
    private Integer infoPri;
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
        /** 图片播放时间单位s,为视频则为-1 */
        private Integer timeInterval;
        /** 播放优先级(值越小优先级越大) */
        private Integer pri;
    }
}
