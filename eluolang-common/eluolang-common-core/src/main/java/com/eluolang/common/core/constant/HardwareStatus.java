package com.eluolang.common.core.constant;

/**
 * 软硬件对接命令码
 *
 * @author suziwei
 * @date 2020/8/31
 */
public class HardwareStatus
{

    /**
     * ACK反馈
     */
    public static final int ACK_FEEDBACK = 0;

    /**
     * 读取开门记录
     */
    public static final int READ_OPEN_DOOR_RECORD = 1;

    /**
     * 配置门开时长
     */
    public static final int CONFIG_DOOR_OPEN_TIME = 2;

    /**
     * 获取门开时长
     */
    public static final int GET_DOOR_OPEN_TIME = 3;

    /**
     * 校正时钟
     */
    public static final int CORRECT_CLOCK = 4;

    /**
     * 获取设备时钟
     */
    public static final int GET_DEVICE_CLOCK = 5;

    /**
     * 重启设备
     */
    public static final int REBOOT_DEVICE = 6;

    /**
     * 版本查询
     */
    public static final int VERSION_QUERY = 7;

    /**
     * 配置单个设备信息
     */
    public static final int CONFIG_DEVICE_INFO = 8;

    /**
     * 获取单个设备信息
     */
    public static final int GET_DEVICE_INFO = 9;

    /**
     * 住户授权访客
     */
    public static final int AUTH_HOUSEHOLD_VISITORS = 11;

    /**
     * 配置开门拍照、拍视频规则
     */
    public static final int CONFIG_RULES_FOR_TAKING_PHOTOS_AND_VIDEOS_FOR_OPEN_DOOR = 12;

    /**
     * 配置报警拍照、拍视频规则
     */
    public static final int CONFIG_RULES_FOR_TAKING_PHOTOS_AND_VIDEOS_FOR_ALARM = 13;

    /**
     * 配置人脸识别服务器
     */
    public static final int CONFIG_FACE_RECOGNITION_SERVER = 14;

    /**
     * 批量配置信息
     */
    public static final int BATCH_CONFIG_INFO = 15;

    /**
     * 下发文件信息
     */
    public static final int DOWNLOAD_FILE_INFO = 16;

    /**
     * 升级固件
     */
    public static final int UPDATE_FIRMWARE = 17;

    /**
     * 下发有权限的管理账号
     */
    public static final int DOWNLOAD_AUTHORIZED_MGR_ACCOUNT = 18;

    /**
     * 信息发布
     */
    public static final int INFO_DELIVERY = 10;

    /**
     * 取消信息发布
     */
    public static final int CANCEL_INFO_DELIVERY = 15;

    /**
     * 处理报警
     */
    public static final int DISPOSE_ALARM = 21;
    /**
     * 开/关机设备
     */
    public static final int ON_OFF_DEVICE = 20;
    /**
     * 删除设备
     */
    public static final int DELETE_DEVICE = 25;

    /**
     * 异常反馈
     */
    public static final int ERROR_FEEDBACK = 100;

    /**
     * 实时上传开门记录
     */
    public static final int UPLOAD_OPEN_DOOR__RECORD_NOW = 99;

    /**
     * 心跳设备状态
     */
    public static final int HEARTBEAT_DEVICE_STATUS = 98;

    /**
     * 获取当前时间
     */
    public static final int GET_CURRENT_TIME = 97;

    /**
     * 上传告警信息
     */
    public static final int UPLOAD_WARN_INFO = 96;

    /**
     * 上传报警信息
     */
    public static final int UPLOAD_ALARM_INFO = 95;

    /**
     * 上传设备信息
     */
    public static final int UPLOAD_DEVICE_INFO = 94;

    /**
     * 上传文件信息
     */
    public static final int UPLOAD_FILE_INFO = 93;

    /**
     * 设备上报环境参数
     */
    public static final int DEVICE_ENVIRONMENT_PARAM = 92;

    /**
     * 一键求助
     */
    public static final int ONE_CLICK_HELP = 91;

    /**
     * 上传设备用电量
     */
    public static final int UPLOAD_DEVICE_USE_ELECTRICITY_AMOUNT = 90;


    /**
     * 上传设备心态状态
     */
    public static final int UPLOAD_HEARTBEAT_DEVICE_STATUS = 200;
    /**
     * 上传设备信息
     */
    public static final int UPLOAD_HEARTBEAT_INFORMATION = 201;
    /**
     * 上传流量信息
     */
    public static final int UPLOAD_TRAFFIC_INFORMATION = 202;
    /**
     * 接收查询实时流量命令
     */
    public static final int RECEIVE_QUERY_REALTIME_TRAFFIC = 203;
    /**
     * 接收套餐配置
     */
    public static final int RECEIVE_PACKAGE_CONFIGURATION = 204;
    /**
     * 上传断网报警信息
     */
    public static final int UPLOAD_DISCONNECTION_ALARM_INFORMATION = 205;
    /**
     * 接收断网（WIFI上网功能）指令
     */
    public static final int UPLOAD_DISCONNECTION_INFORMATION = 206;
    /**
     * 接收加油包命令
     */
    public static final int UPLOAD_REFUELING_PACKET_INFORMATION = 207;
    /**
     * 配置上传
     */
    public static final int UPLOAD_CONFIGURATION = 208;
    /**
     * 接收平台下发配置信息
     */
        public static final int ISSUED_BY_THE_CONFIGURATION = 209;
    /**
     * 接收平台下发设备重启命令
     */
    public static final int ISSUED_TO_RESTART = 210;
    /**
     * 固件升级
     */
    public static final int FIRMWARE_UPGRADE = 211;
    /**
     * 互锁规则配置
     */
    public static final int MUTEX_RULE_CFG = 24;
    /**
     * 组合开门配置
     */
    public static final int COMB_OPEN_CFG = 25;
    /**
     * 远程开/关门
     */
    public static final int REMOTE_OPEN_DOOR = 26;
    /**
     * 删除用户
     */
    public static final int DEL_OPEN_AUTH = 27;

    /**
     * 按时间段上传设备事件
     */
    public static final int UPLOAD_DEVICE_EVENT = 89;
}
