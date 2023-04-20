package com.eluolang.common.core.hardware.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年11月22日 16:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaceDetectData {
    /** 设备的唯一编号 */
    private String deviceId;
    /** 视频通道号 */
    private Integer videoNo;
    /** 摄像头布点或抓拍机布点名称，就是 videoName 或 pictureName，根据工作模式workMode 设置所决定 */
    private String channelName;
    /** 原始图片唯一 ID，用于获取底库中原始照片 */
    private String imageId;
    /** 视频中的一个人脸或一张抓拍机图片唯一编号，用于调用 captures 接口调取抓拍图片使用 */
    private Integer trackId;
    /** 抓拍与底库中人脸比对的相似度 */
    private float searchScore;
    /** 抓拍图片的活体分数 */
    private float livenessScore;
    /** Unix 时间戳，事件发生的时间，单位为毫秒 */
    private long timestamp;
}
