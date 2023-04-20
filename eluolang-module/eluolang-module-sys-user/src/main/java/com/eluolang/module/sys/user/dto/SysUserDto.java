package com.eluolang.module.sys.user.dto;

import com.eluolang.common.core.util.MD5Uitls;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 创建系统用户的返回、接收实体类
 *
 *@author  ZengXiaoQian
 *@CreateDate  2020/8/25
 */
@Api("用户管理模块-接收前端传回来的数据")
public class SysUserDto implements Serializable {
    @ApiModelProperty(value = "用户名")
    private String name;
    @ApiModelProperty(value = "用户姓名")
    private String realName;
    @ApiModelProperty(value = "用户名/姓名")
    private String realUserName;
    @ApiModelProperty(value = "用户邮箱")
    private String email;
    @ApiModelProperty(value = "全路径")
    private String path;
    @ApiModelProperty(value = "用户手机号")
    private String phone;
    @ApiModelProperty(value = "用户邮箱验证码")
    private String code;
    @ApiModelProperty(value = "用户新密码")
    private String newPwd;
    @ApiModelProperty(value = "用户原密码")
    private String oldPwd;
    @ApiModelProperty(value = "用户角色")
    private Integer roleId;
    @ApiModelProperty(value = "用户密码")
    private String pwd;
    @ApiModelProperty(value = "用户id")
    private Integer id;
    @ApiModelProperty(value = "用户角色关联数组（增加、修改用户角色）")
    private Integer[] sysUserRoleId;
    @ApiModelProperty(value = "用户id数组（删除用户）")
    private Integer[] userId;
    @ApiModelProperty(value = "用户状态")
    private Integer state;
    @ApiModelProperty(value = "用户注销时间")
    private String logoutTime;
    @ApiModelProperty(value = "页码")
    private Integer pageNum;
    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public SysUserDto() {
    }

    @Override
    public String toString() {
        return "SysUserDto{" +
                "name='" + name + '\'' +
                ", realName='" + realName + '\'' +
                ", realUserName='" + realUserName + '\'' +
                ", email='" + email + '\'' +
                ", path='" + path + '\'' +
                ", phone='" + phone + '\'' +
                ", code='" + code + '\'' +
                ", newPwd='" + newPwd + '\'' +
                ", oldPwd='" + oldPwd + '\'' +
                ", roleId=" + roleId +
                ", pwd='" + pwd + '\'' +
                ", id=" + id +
                ", sysUserRoleId=" + Arrays.toString(sysUserRoleId) +
                ", userId=" + Arrays.toString(userId) +
                ", state=" + state +
                ", logoutTime='" + logoutTime + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }

    public String getRealUserName() {
        return realUserName;
    }

    public void setRealUserName(String realUserName) {
        this.realUserName = realUserName;
    }

    public String getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(String logoutTime) {
        this.logoutTime = logoutTime;
    }

    public Integer[] getUserId() {
        return userId;
    }

    public void setUserId(Integer[] userId) {
        this.userId = userId;
    }

    public Integer[] getSysUserRoleId() {
        return sysUserRoleId;
    }

    public void setSysUserRoleId(Integer[] sysUserRoleId) {
        this.sysUserRoleId = sysUserRoleId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = MD5Uitls.MD5Lower(pwd, name);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd =newPwd;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
