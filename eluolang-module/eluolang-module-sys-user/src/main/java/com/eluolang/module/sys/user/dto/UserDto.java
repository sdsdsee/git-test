package com.eluolang.module.sys.user.dto;

import java.util.Map;

/**
 * 创建系统用户的返回、接收实体类
 *
 *@author  ZengXiaoQian
 *@CreateDate  2020/8/25
 */
public class UserDto {

    /** 一键全选获取的数据 */
    private Map<String,Object> ids;

    /**用户名/姓名*/
    private String realUserName;

    /**用户角色*/
    private Integer roleId;

    @Override
    public String toString() {
        return "UserDto{" +
                "ids=" + ids +
                ", realUserName='" + realUserName + '\'' +
                ", roleId=" + roleId +
                '}';
    }

    public Map<String, Object> getIds() {
        return ids;
    }

    public void setIds(Map<String, Object> ids) {
        this.ids = ids;
    }

    public String getRealUserName() {
        return realUserName;
    }

    public void setRealUserName(String realUserName) {
        this.realUserName = realUserName;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
