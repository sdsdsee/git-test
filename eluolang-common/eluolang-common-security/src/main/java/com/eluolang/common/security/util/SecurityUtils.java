package com.eluolang.common.security.util;

import com.eluolang.common.core.constant.CacheConstant;
import com.eluolang.common.core.text.Convert;
import com.eluolang.common.core.util.ServletUtils;

/**
 * 权限获取工具类
 * 
 * @author suziwei
 * @date 2020/9/3
 */
public class SecurityUtils
{
    /**
     * 获取用户名称
     */
    public static String getUsername()
    {
        return ServletUtils.getRequest().getHeader(CacheConstant.DETAILS_USERNAME);
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId()
    {
        return Convert.toLong(ServletUtils.getRequest().getHeader(CacheConstant.DETAILS_USER_ID));
    }

    /**
     * 是否为管理员
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }

}
