package com.eluolang.common.core.enums;

/**
 * 用户状态
 * 
 * @author suziwei
 */
public enum UserStatus
{
    /** 用户状态 */
    OK(0, "正常"), LOGOUT(1, "冻结");

    private final int code;
    private final String info;

    UserStatus(int code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public int getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
