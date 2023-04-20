package com.eluolang.common.core.pojo;

/**
 * 考官表
 *
 * @author dengrunsen
 * @Date 2022/1/10
 */
public class EllExaminer {
    /**
     * id
     */
    private String id;
    /** 登录验证码 */
    private String loginCode;
    /** 考官姓名 */
    private String name;
    /** 考官电话 */
    private String phone;
    /** 部门id */
    private Integer deptId;
    /** 管理员id */
    private Integer optId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public Integer getOptId() {
        return optId;
    }

    public void setOptId(Integer optId) {
        this.optId = optId;
    }
}
