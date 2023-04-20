package com.eluolang.module.sys.user.dto;

import com.eluolang.common.core.util.MD5Uitls;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ZengXiaoQian
 * @createDate 2020-10-10
 */
@Api("用户管理模块-接收前端传回来的数据")
public class ModifyUserDto {
    @ApiModelProperty(value = "用户姓名")
    private String realName;
    @ApiModelProperty(value = "用户手机号")
    private String phone;
    @ApiModelProperty(value = "用户邮箱")
    private String email;
    @ApiModelProperty(value = "用户角色")
    private Integer roleId;
    @ApiModelProperty(value = "用户id")
    private Integer id;
    @ApiModelProperty(value = "用户名")
    private String name;
    @ApiModelProperty(value = "用户密码")
    private String pwd;
    @ApiModelProperty(value = "用户状态")
    private Integer state;
    @ApiModelProperty(value = "背景图片路径")
    private String backgroundUrl;
    @ApiModelProperty(value = "部分背景图片路径")
    private String partBackgroundUrl;

    @Override
    public String toString() {
        return "ModifyUserDto{" +
                "realName='" + realName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", roleId=" + roleId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", state=" + state +
                ", backgroundUrl='" + backgroundUrl + '\'' +
                ", partBackgroundUrl='" + partBackgroundUrl + '\'' +
                '}';
    }

    public String getPartBackgroundUrl() {
        return partBackgroundUrl;
    }

    public void setPartBackgroundUrl(String partBackgroundUrl) {
        this.partBackgroundUrl = partBackgroundUrl;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = MD5Uitls.MD5Lower(pwd, name);
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
