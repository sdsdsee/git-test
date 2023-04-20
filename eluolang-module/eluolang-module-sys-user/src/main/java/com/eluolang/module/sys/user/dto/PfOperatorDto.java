package com.eluolang.module.sys.user.dto;

import com.eluolang.common.core.pojo.PfOperatorRat;

import java.util.List;

public class PfOperatorDto {
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
    /** 状态0-正常 1-冻结 */
    private Integer status;
    /** logo图片文件id */
    private String fileId;
    /** 管理员类型，0:普通管理员，1:教师管理员 */
    private Integer optType;
    private List<PfOperatorRat> pfOperatorRatList;

    @Override
    public String toString() {
        return "PfOperatorDto{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", optName='" + optName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", psw='" + psw + '\'' +
                ", path='" + path + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", validStart='" + validStart + '\'' +
                ", validEnd='" + validEnd + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", updatedTime='" + updatedTime + '\'' +
                ", lastLogin='" + lastLogin + '\'' +
                ", status=" + status +
                ", pfOperatorRatList=" + pfOperatorRatList +
                '}';
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Integer getOptType() {
        return optType;
    }

    public void setOptType(Integer optType) {
        this.optType = optType;
    }

    public List<PfOperatorRat> getPfOperatorRatList() {
        return pfOperatorRatList;
    }

    public void setPfOperatorRatList(List<PfOperatorRat> pfOperatorRatList) {
        this.pfOperatorRatList = pfOperatorRatList;
    }
}
