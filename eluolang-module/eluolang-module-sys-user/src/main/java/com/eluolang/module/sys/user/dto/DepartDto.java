package com.eluolang.module.sys.user.dto;

/**
 * 组织机构
 * @author Tencell006
 *
 */
public class DepartDto {
    /** 编号 */
    private String id ;
    /** 父节点 */
    private String parentId ;
    /** 名称 */
    private String deptName ;
    /** 结束层 */
    private String lastLevel ;
    /** 全路径 */
    private  String path ;
    /** 创建人 */
    private String createdBy;
    /** 创建时间 */
    private  String createdTime ;
    /** 更新人 */
    private String updatedBy;
    /** 更新时间 */
    private  String updatedTime ;
    /** 操作用户id */
    private  String optId ;
    /** 操作用户名称 */
    private  String optName ;
	/** 标志 */
	private String sig;
	/** 是否为学校级(0:否,1:是) */
	private Integer isSchool;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getLastLevel() {
		return lastLevel;
	}
	public void setLastLevel(String lastLevel) {
		this.lastLevel = lastLevel;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
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
	public String getOptId() {
		return optId;
	}
	public void setOptId(String optId) {
		this.optId = optId;
	}
	public String getOptName() {
		return optName;
	}
	public void setOptName(String optName) {
		this.optName = optName;
	}

	public String getSig() {
		return sig;
	}

	public void setSig(String sig) {
		this.sig = sig;
	}

	public Integer getIsSchool() {
		return isSchool;
	}

	public void setIsSchool(Integer isSchool) {
		this.isSchool = isSchool;
	}
}
