package com.eluolang.module.socket.server.util;

/**
 * 自定义常量类
 *
 * @author renzhixing
 */
public class Constant {

    /**
     * 基础长度
     */
    public static final Integer BASICS_LENGTH = 10;

    /**
     * 头信息
     */
    public static final byte[] HEAD = new byte[]{(byte) 0xA5, (byte) 0x5A, (byte) 0xAA, (byte) 0xBB};

    /**
     * 成功码
     */
    public static final Integer SUCCESS = 0;

    /**
     * 错误码
     */
    public static final Integer ERROR = 1;


    /**
     * 设备状态 已注册
     */
    public static final Integer REG = 2;

    /**
     * 每包大小
     */
    public static final Integer MAX_PAG_LEN = 1024 * 60;

    /**
     * Ident指令标识
     */
    public static final String IDENT = "Ident:";

    /**
     * 复制一份Ident copykey
     */
    public static final String COPY_FILE_IDENT = "Copy:File:Ident:";

    /**
     * 文件ident标识
     */
    public static final String FILE_IDENT = "File:Ident:";

    /**
     * 记录文件传输每包文件失败的次数
     */
    public static final String FILE_FAIL_COUNT = "File:Fail:count:";

    /**
     * 文件传输失败最大次数
     */
    public static final Integer FILE_FAIL_COUNT_VALUE = 5;

    /**
     * 文件key
     */
    public static final String FILE = "File:";

    /**
     * IDENT指令超时时间
     */
    public static final Integer FILE_IDENT_TIME = 10;

    /**
     * 获取报警信息推送结果
     */
    public static final String ALARM_INFO = "ALARM_INFO";

    /**
     * rabbitmq的消息
     */
    public static final String RABBIT_MSG = "rabbitmq:msg:";

    /**
     * 是否需要升级固件
     */
    public static final Integer NEED_UPD = 1;




}
