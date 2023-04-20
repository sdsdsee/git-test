package com.eluolang.module.socket.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author dengrunsen
 * @date 2022年11月22日 14:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaceDetectVo {
    /** 设备的唯一编号 */
    private String deviceId;
    /** 视频通道号 */
    private Integer videoNo;
    /** 摄像头布点或抓拍机布点名称，就是 videoName 或 pictureName，根据工作模式workMode 设置所决定 */
    private String channelName;
    /** 通道检测类型：0：未知，1：纯人脸；2：脸人；3：结构化 */
    private Integer chnDetectType;
    /** 报警人脸分组列表，分组信息，可能会有多个 groupName */
    private List<FaceDetectVo.AlertGroup> alertGroup;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AlertGroup{
        /** 当 searchScore 在底库人脸所在的分组的阈值范围内，添加此人脸分组名 */
        private String groupName;
    }

    /** 分组信息，可能会有多个 groupName */
    private List<FaceDetectVo.Group> group;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Group{
        /** 布点绑定的人脸分组名称，如该布点绑定了多个分组，将有多个 groupName */
        private String groupName;
    }

    /** 人脸的唯一 ID，用于获取分组中人脸照片 */
    private String faceToken;
    /** 原始图片唯一 ID，用于获取底库中原始照片 */
    private String imageId;
    /** 图片数据，受 pushImageSW 开关控制，如关闭数据为空 */
    private Object images;
//
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    private static class Images{
//        /** 抓拍人脸图数据长度 */
//        private Integer captureFace;
//        /** 全景图数据长度 */
//        private Integer fullImage;
//        /** 底库分最高人脸图数据长度 */
//        private Integer Top1;
//    }
    /** 视频中的一个人脸或一张抓拍机图片唯一编号，用于调用 captures 接口调取抓拍图片使用 */
    private Integer trackId;
    /** 上述同一 trackId 出现的次数 */
    private Integer trackIdTimes;
    /** 绑定的目标信息 */
    private List<FaceDetectVo.BindingInfo> bindingInfo;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BindingInfo{
        /** 绑定目标的类型：1-人脸，2-人体，3-机动车，4-非机动车，5-车牌 */
        private Integer bindingType;
        /** 绑定目标的 trackid */
        private Integer bindingTrackId;
    }
    /** 行进状态：0-未知，1-静止，2-运动。注：人体、机动车、非机动车目标类型有此项，其他目标类型，此项为 0 */
    private Integer movingStatus;
    /** 行进方向：0-未知，1-上，2-下，3-左，4-右，5-左上，6-左下，7-右上，8-右下。注：人体、机动车、非机动车目标类型有此项，其他目标类型，此项为 0 */
    private Integer movingDirection;
    /** 抓拍与底库中人脸比对的相似度 */
    private float searchScore;
    /** 抓拍图片的活体分数 */
    private float livenessScore;
    /** 描述，预留字段，UTF-8 编码 */
    private String description;
    /** Unix 时间戳，事件发生的时间，单位为毫秒 */
    private long timestamp;
    /** 目标类型：人脸："face" */
    private String objType;
    /** 人脸属性 */
    private List<FaceDetectVo.FaceAttr> faceAttr;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FaceAttr{
        /** 年龄 */
        private Integer age;
        /** 性别 0：男 1：女，2：未知 */
        private Integer gender;
        /** 0：光头 1：少量头发（包含秃顶）2：短发 3：长发 4：未知 */
        private Integer hair;
        /** 0：无胡子或者胡子不明显 1：嘴唇上面的胡子 2：络腮胡 3：未知 */
        private Integer beard;
        /** 0：无帽子 1：安全帽 2：厨师帽 3：学生帽 4：头盔 5：01 小白帽 6：头巾 7：其他帽子 8：未知 */
        private Integer hat;
        /** 0：无口罩 1：医用口罩 2：雾霾口罩 3：普通口罩 4：厨房用”透明口罩“5：未知 */
        private Integer respirator;
        /** 0：无眼镜 1：深色框透明眼镜 2：普通透明眼镜 3：墨镜 4：未知 */
        private Integer glasses;
    }
    /** 小图基于背景图的坐标 */
    private Object detectionResult;
    /** 目标基于小图的坐标 */
    private Object targetResult;
}
