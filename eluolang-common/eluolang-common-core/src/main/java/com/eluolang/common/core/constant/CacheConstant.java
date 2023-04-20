package com.eluolang.common.core.constant;

/**
 * 缓存的key 常量
 *
 * @author dengurnsen
 * @date 2022/04/26
 */
public class CacheConstant {
    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 设备信息 redis key
     */
    public static final String DEVICE_INFO = "device:info:";

    /**
     * 设备状态 redis key
     */
    public static final String DEVICE_STATE = "device:state:";

    /**
     * 读头设备状态 redis key
     */
    public static final String READ_HEAD_STATE = "read:head:state:";

    /**
     * 令牌自定义标识
     */
    public static final String HEADER = "Authorization";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 登录token前缀
     */
    public final static String LOGIN_TOKEN_KEY = "login_token:";

    /**
     * 用户ID字段
     */
    public static final String DETAILS_USER_ID = "user_id";

    /**
     * 用户名字段
     */
    public static final String DETAILS_USERNAME = "username";

    /**
     * 登录id前缀
     */
    public final static String LOGIN_ID_KEY = "login_id:";

    /**
     * 现在删除的用户的token
     */
    public final static String CURRENT_DELETE_TOKEN = "current_delete_token:";

    /**
     * 现在禁用的用户token
     */
    public final static String CURRENT_DISABLED_TOKEN = "current_disabled_token:";

    /**
     * 手环数据前缀
     */
    public final static String WRISTBAND_KEY = "wristband:";


    /**
     * 人脸识别数据前缀
     */
    public final static String FACE_KEY = "face:";

    /**
     * 自由跑
     */
    public final static String RUNNING_FREE_KEY = "running_free:";

    /**
     * 测试跑
     */
    public final static String RUNNING_TEST_KEY = "running_test:";

    /**
     * 测试跑按钮状态
     */
    public final static String RUNNING_TEST_BUTTON_KEY = "running_test_button:";

    /**
     * 测试跑检录
     */
    public final static String RUNNING_CALL_KEY = "running_call:";

    /**
     * 跑步模式
     */
    public final static String RUNNING_MODE_KEY = "running_mode";

    /**
     * 跑步模式
     */
    public final static String LARGE_SCREEN_DEVICE = "large_screen:device:";

    /**
     * 跑步模式
     */
    public final static String NOW_RUNNING_TIME = "now_running_time:";
//
//    /**
//     * 优先解码指令标识
//     */
//    public final static String UNPAK_HIGH_PRI = "unpak_high_pri";
    /**
     * 停车位信息
     */
    public final static String PARKING_SPACE = "LED:trucSpace:240305004110343";

    /**
     * 设备信息 redis key
     */
    public static final String DEVICE_ENVIRONMENT_INFO = "device:info:";

    /**
     * 所有人员累计跑步时间
     */
    public static final String All_USER_RUNNING_TIME = "all_user:running";
}
