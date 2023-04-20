package com.eluolang.common.core.hardware.vo;

/**
 * 组织机构
 * 
 * @author Tencell006
 *
 */
public class PfDepartVo {
	/** 编号 */
	private int id;
	/** 父节点 */
	private int parentId;
	/** 名称 */
	private String deptName;
	/** 结束层 */
	private String lastLevel;
	/** 全路径 */
	private String path;
	/** 创建人 */
	private String createdBy;
	/** 创建时间 */
	private String createdTime;
	/** 更新人 */
	private String updatedBy;
	/** 更新时间 */
	private String updatedTime;
	/** 操作员id */
	private String optId;
	/** 操作员name */
	private String optName;
	/** 标志 */
	private String sig;
	private String sigFmt;
	/** 删除标识（0:未删除，1:已删除） */
	private Integer isDelete;
	/** 是否为学校级(0:否,1:是) */
	private String isSchool;
	/** 学校总人数 */
	private Integer total;
	/** 学校考试人数 */
	private Integer number;
	/** 学校考试完成人数 */
	private Integer testSuccessNum;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
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

	public String getSigFmt() {
		return sigFmt;
	}

	public void setSigFmt(String sigFmt) {
		this.sigFmt = sigFmt;
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

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getTestSuccessNum() {
		return testSuccessNum;
	}

	public void setTestSuccessNum(Integer testSuccessNum) {
		this.testSuccessNum = testSuccessNum;
	}
}
