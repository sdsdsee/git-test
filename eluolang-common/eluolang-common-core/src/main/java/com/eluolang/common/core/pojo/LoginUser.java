package com.eluolang.common.core.pojo;


import com.eluolang.common.core.hardware.vo.PfDepartVo;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息
 *
 * @author suziwei
 * @date 2020/9/2
 */
public class LoginUser implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    private String token;

    /** 编号自增 1-system 2-admin编号默认 1=system 2=admin 其余为自定义;<3显示pf_menu所有项,>2显示pf_operator_rat字段module=0的项 */
    private Integer id;
    /** 父节点 */
    private Integer parentId;
    /** 登录账号 */
    private String optName;
    /** 昵称 */
    private String nickname;
    /** 登录密码 */
    private String psw;
    /** 全路径 */
    private String path;
    /** 操作员手机号 */
    private String phone;
    /** 操作员邮箱 */
    private String email;
    /** 账号有效开始 */
    private String validStart;
    /** 账号有效结束 */
    private String validEnd;
    /** 创建时间 */
    private String createdTime;
    /** 更新时间 */
    private String updatedTime;
    /** 最近登录时间 */
    private String lastLogin;
    /** 登陆时间 */
    private Long loginTime;
    /** 过期时间 */
    private Long expireTime;
    /** 状态0-正常 1-冻结 */
    private Integer status;
    /** 账号类型（0普通账号，1教师账号） */
    private Integer optType;
    /** 菜单权限 */
    private List<PfMenu> pfMenuList;
    /** 部门权限 */
    private List<PfDepartVo> pfDepartList;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getValidStart() {
        return validStart;
    }

    public void setValidStart(String validStart) {
        this.validStart = validStart;
    }

    public String getValidEnd() {
        return validEnd;
    }

    public void setValidEnd(String validEnd) {
        this.validEnd = validEnd;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOptType() {
        return optType;
    }

    public void setOptType(Integer optType) {
        this.optType = optType;
    }

    public List<PfMenu> getPfMenuList() {
        return pfMenuList;
    }

    public void setPfMenuList(List<PfMenu> pfMenuList) {
        this.pfMenuList = pfMenuList;
    }

    public List<PfDepartVo> getPfDepartList() {
        return pfDepartList;
    }

    public void setPfDepartList(List<PfDepartVo> pfDepartList) {
        this.pfDepartList = pfDepartList;
    }
}
