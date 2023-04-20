package com.eluolang.module.sys.user.dto;

/**
 * 修改密码
 *
 * @author dengrunsen
 * @date 2021/2/28
 */
public class ModifyPwdDto {
    /** 操作员账号 */
    private String optName;
    /** 旧密码 */
    private String oldPwd;
    /** 新密码 */
    private String newPwd;

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
