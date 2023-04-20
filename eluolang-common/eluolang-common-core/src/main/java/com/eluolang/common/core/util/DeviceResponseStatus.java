package com.eluolang.common.core.util;

/**
 * 定义设备枚举类型
 *
 * @author renzhixing
 */
public enum DeviceResponseStatus
{
    RESPONSE_SUCCESS(0, "正常"), RESPONSE_ERROR(1, "异常");

    private final Integer code;
    private final String info;

    DeviceResponseStatus(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
