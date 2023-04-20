package com.eluolang.auth.form;

import io.swagger.annotations.ApiModel;

/**
 * 用户登录对象
 * 
 * @author suziwei
 */
@ApiModel(value = "用户登录对象")
public class LoginBody
{
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

}
