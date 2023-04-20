package com.eluolang.module.sys.user.constant;

/**
 * 用户管理控制层
 *
 * @author ZengxiaoQian
 * @createDate 2020-8-25
 */

public class SysUserConstant {
    /**
     * 删除用户成功
     */
    public static final int DELETE_USER_SUCCESS=1;

    /**
     * 删除用户角色失败
     */
    public static final int DELETE_USER_ROLE_FAILURE=-1;

    /**
     * 删除用户失败
     */
    public static final int DELETE_USER_FAILURE=-2;

    /**
     * 邮箱验证码的长度
     */
    public static final int MAIL_CODE_LENGTH=6;



    /**
     * 执行查询、增加、删除、修改数据库中的数据，返回受影响行数的默认值
     */
    public static final int RETURN_AFFECTED_ROWS=0;



    /**
     * 修改用户基本信息成功
     */
    public static final int MODIFY_USER_SUCCESS=1;

    /**
     * 修改用户基本信息失败
     */
    public static final int MODIFY_USER_FAILURE=-1;

    /**
     * 修改用户角色信息成功
     */
    public static final int MODIFY_USER_ROLE_SUCCESS=2;

    /**
     * 修改用户角色信息失败
     */
    public static final int MODIFY_USER_ROLE_FAILURE=-2;

    /**
     * 您没有需要修改的角色信息
     */
    public static final int NO_MODIFY_USER_ROLE=-3;

    /**
     * 删除用户角色失败，修改角色失败
     */
    public static final int DEL_MODIFY_USER_ROLE_FAILURE=-4;


    /**
     * 新增用户成功
     */
    public static final int ADD_USER_SUCCESS=6;

    /**
     * 用户名重复
     */
    public static final int NAME_REPEAT=-99;

    /**
     * 新增用户失败
     */
    public static final int ADD_USER_FAILURE=-6;

    /**
     * 新增用户基本信息失败
     */
    public static final int ADD_USER_BASIC_FAILURE=-7;

    /**
     * 用户状态为已启用
     */
    public static final int AVAILABLE_USER=0;

    /**
     * 用户状态为已禁用
     */
    public static final int DISABLED_USER=1;

}
