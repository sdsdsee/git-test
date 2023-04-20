package com.eluolang.common.core.constant;

/**
 * 状态码
 *
 * @author dengurnsen
 * @date 2022/04/26
 */
public class HttpStatus {
    /**
     * 操作成功
     */
    public static final int SUCCESS = 200;

    /**
     * 对象创建成功
     */
    public static final int CREATED = 201;

    /**
     * 请求已经被接受
     */
    public static final int ACCEPTED = 202;

    /**
     * 操作已经执行成功，但是没有返回数据
     */
    public static final int NO_CONTENT = 204;

    /**
     * 资源已被移除
     */
    public static final int MOVED_PERM = 301;

    /**
     * 重定向
     */
    public static final int SEE_OTHER = 303;

    /**
     * 资源没有被修改
     */
    public static final int NOT_MODIFIED = 304;

    /**
     * 参数列表错误（缺少，格式不匹配）
     */
    public static final int BAD_REQUEST = 400;

    /**
     * 未授权
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 访问受限，授权过期
     */
    public static final int FORBIDDEN = 403;

    /**
     * 资源，服务未找到
     */
    public static final int NOT_FOUND = 404;

    /**
     * 不允许的http方法
     */
    public static final int BAD_METHOD = 405;

    /**
     * 资源冲突，或者资源被锁
     */
    public static final int CONFLICT = 409;

    /**
     * 不支持的数据，媒体类型
     */
    public static final int UNSUPPORTED_TYPE = 415;

    /**
     * 数据已存在
     */
    public static final int ALREADY_EXISTS = 416;

    /**
     * 数据交替
     */
    public static final int ALTERNATE_ERROR = 417;
    /**
     * 系统内部错误
     */
    public static final int ERROR = 500;

    /**
     * 接口未实现
     */
    public static final int NOT_IMPLEMENTED = 501;
    /**
     * 身份证错误
     */
    public static final int ID_CARD_ERROR = 1960;
    /**
     * 已经被别人使用/获取
     */
    public static final int HAS_BEEN_OBTAINED = 2180;
    /**
     * 已经被别人使用/获取/存在
     */
    public static final int PARAMETER_ERROR = 1860;
    /**
     * 身份证已经存在
     */
    public static final int ID_CARD_EXIST = 2280;
    /**
     * 学号已经存在
     */
    public static final int STUDY_NUMBER_EXIST = 2380;
    /**
     * 部门ID不存在或无权限
     */
    public static final int DEPT_ID_NOT_EXIST = 2480;
    /**
     * 部分数据出错
     */
    public static final int PARTIAL_DATA_ERROR = 2580;
    /**
     * 计划没有设置完成
     */
    public static final int PLAN_NOT_SET = 2680;
    /**
     * 参数不够
     */
    public static final int INSUFFICIENT_DEFICIENCY = 2780;
    /**
     * 解密错误
     */
    public static final int DECODE_ERROR = 2880;
    /**
     * 非活体
     */
    public static final int NON_LIVING_BODY = 3080;
    /**
     * 没有匹配成功
     */
    public static final int MATCHING_FAILURE = 3180;
    /**
     * 不允许修改
     */
    public static final int NO_MODIFICATION_ALLOWED = 3190;
    /**
     * 验证码错误
     */
    public static final int CODE_ERROR = 535;
    /**
     * 验证码已发送
     */
    public static final int CODE_REPETITION_SEND = 3210;
    /**
     * 验证码发送失败
     */
    public static final int CODE_SEND_ERROR = 3211;
    /**
     * 旷视接口调用成功码
     */
    public static final int CODE_SUCCESS_KS = 0;
    /**
     * 上传的信息没有内容
     */
    public static final int INFO_NOT_CONTENT = 1360;
    /**
     * 超出
     */
    public static final int OUT_SIZE = 1370;
    /**
     * 日期不纯在
     */
    public static final int DATE_NOT_FOUNT = 1380;
    /**
     * 占用
     */
    public static final int DEVICE_OCCUPY = 1390;
}
