package com.eluolang.common.core.constant;

/**
 * 通用常量信息
 *
 * @author dengurnsen
 * @date 2022/04/26
 */
public class Constants
{

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 成功标记
     */
    public static final Integer SUCCESS = 200;
    /**
     * 失败标记
     */
    public static final Integer FAIL = 500;

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 令牌有效期（分钟）
     */
    public final static Integer TOKEN_EXPIRE = 30;

    /**
     * 请求头用户名称
     */
    public final static String LOGIN_USER = "username";

    /**
     * 系统包名前缀
     */
    public final static String PACKAGE_PREFIX = "com.eluolang";

    /**
     * 人脸识别
     */
    public static final Integer FACE=1;

    /**
     * 二维码
     */
    public static final Integer CODE=2;

    /**
     * 手机蓝牙
     */
    public static final Integer BLUETOOTH=3;
//
//    /**
//     * 对讲
//     */
//    public static final Integer TALKBACK=4;
//
//    /**
//     * 指纹
//     */
//    public static final Integer FINGER=5;
//
//    /**
//     * 卡片
//     */
//    public static final Integer CARD=6;

    /**
     * 身份证
     */
    public static final Integer ID=7;

    /**
     * 密码
     */
    public static final Integer PASSWORD=8;


    /**
     * 执行查询、增加、删除、修改数据库中的数据，返回受影响行数的默认值
     */
    public static final int RETURN_AFFECTED_ROWS=0;

    /**
     * 上传文件大小
     */
    public static final Long UPLOAFFILE_SIZE=209715200L;

    /**
     * 保存设备上传文件路径
     */
    public static final String FILE_ADDRESS = "/home/file";

    /**
     * 保存用户上传的文件路径
     */
    public static final String USER_FILE_ADDRESS = "/home/upload";
//
//    /**
//     * 住户录入人脸的路径
//     */
//    public static final String HOUSEHOLD_FACE_ADDRESS = "/home/face/face/Knowned";

    /**
     * 人员管理的上传头像linux路径
     */
    public static final String DEPART_LINUX_PHOTO_ADDRESS = "/mnt/depart/file";

    /**
     * 人员管理的上传头像windows路径
     */
    public static final String DEPART_WINDOWS_PHOTO_ADDRESS = "D:/eluolang/depart";
//
//    /**
//     * 人脸权限存放文件夹WINDOWS路径
//     */
//    public static final String FILE_PATH = "D:/sftp";
//
//    /**
//     * 人脸权限存放文件夹LINUX路径
//     */
//    public static final String FILE_LINUX_PATH = "/mnt/sdc/sftp";
//
//    /**
//     * sftp文件传输最后一级路径
//     */
//    public static final String FILE_LAST_LEVEL = "send";
//
//    /**
//     * 人员权限存放文件名称
//     */
//    public static final String JSON_FILE_NAME = "householdId";
//
//    /**
//     * 人员权限存放文件压缩包名称(人员)
//     */
//    public static final String COMPRESS_FILE_EMP_NAME = "householdId";
//
//    /**
//     * 人员权限存放文件压缩包名称(设备)
//     */
//    public static final String COMPRESS_FILE_DEV_NAME = "device";
//
//    /**
//     * 人员权限存放文件名称
//     */
//    public static final String SFTP_COMPRESS_PATH = "D:/ibp/sftp-files";
//
//    /**
//     * 人脸权限存放文件夹LINUX路径
//     */
//    public static final String SFTP_COMPRESS_LINUX_PATH = "/mnt/sdc/sftp-file";
//
//    /**
//     * ident的配置id
//     */
//    public static final Integer IDENT_ID= 13;
//
//    /**
//     * 预警id
//     */
//    public static final Integer CONFIG_DISK_ALERT_ID= 14;
//
//    /***
//     * 服务器ip地址
//     */
//    public static final String SERVER_ADDRESS  = "http://192.168.110.235:9898/";
//
//    /***
//     * 服务器ip地址
//     */
//    public static final String SFTP_SERVER_ADDRESS  = "124.71.9.82";
//
//    /**
//     * 体温状态（正常）
//     */
//    public static final Integer NORMAL= 1;
//
//    /**
//     * 体温状态（异常 ）
//     */
//    public static final Integer ABNORMAL= 2;

    /**
     * 男
     */
    public static final Integer MALE=1;

    /**
     * 女
     */
    public static final Integer FEMALE=2;

    /**
     * 已发布
     */
    public static final Integer RELEASED=1;

    /**
     * 已取消
     */
    public static final Integer CANCEL=2;

    /**
     * 已过期
     */
    public static final Integer EXPIRED=3;
//
//    /**
//     * sftp账号
//     */
//    public static final Integer SFTP_USER_ID=17;
//
//    /**
//     * sftp密码
//     */
//    public static final Integer SFTP_PASSWORD_ID=18;

    /**
     * 信息状态为未发布
     */
    public static final Integer STATE_NOT_DELIVERY= 0;

    /**
     * 信息状态为已发布
     */
    public static final Integer STATE_YES_DELIVERY= 1;
}
