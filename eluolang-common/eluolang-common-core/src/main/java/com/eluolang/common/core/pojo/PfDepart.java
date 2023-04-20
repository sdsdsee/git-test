package com.eluolang.common.core.pojo;

/**
 * 部门表
 * @author dengrunsen
 * @date 2021/3/4
 */
public class PfDepart {
    /** id */
    private Integer id;
    /** 父节点 */
    private Integer parentId;
    /** 全路径 */
    private String path;
    /** 名称 */
    private String deptName;
    /** 结束层 */
    private Integer lastLevel;
    /** 创建人 */
    private String createdBy;
    /** 创建时间 */
    private String createdTime;
    /** 更新人 */
    private String updatedBy;
    /** 更新时间 */
    private String updatedTime;
    /** 标志 */
    private String sig;
    /** 删除标识（0:未删除，1:已删除） */
    private Integer isDelete;
    /** 是否为学校级(0:否,1:是) */
    private String isSchool;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getLastLevel() {
        return lastLevel;
    }

    public void setLastLevel(Integer lastLevel) {
        this.lastLevel = lastLevel;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getIsSchool() {
        return isSchool;
    }

    public void setIsSchool(String isSchool) {
        this.isSchool = isSchool;
    }
}
