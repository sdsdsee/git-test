package com.eluolang.auth.service;

import com.eluolang.common.core.pojo.LoginUser;
import com.eluolang.common.core.pojo.PfMenu;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 *
 * @author suziwei
 * @date 2020/9/5
 */
public interface UserService
{
    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @return 结果
     */
    public LoginUser getUserInfo(String username);

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param pwd 密码
     * @return 结果
     */
    public LoginUser login(String username, String pwd);

    /**
     * 根据操作员ID查询所拥有权限菜单
     * @param optId
     * @return
     */
    public List<PfMenu> getPfMenuByOptId(Integer optId);

    /**
     * 退出登录
     *
     * @param request 请求头
     * @return 结果
     */
    public Boolean logout(HttpServletRequest request);

    /**
     * 刷新token
     *
     * @param request 请求头
     * @return 结果
     */
    public Boolean refresh(HttpServletRequest request);
}
