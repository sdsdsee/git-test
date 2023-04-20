package com.eluolang.physical.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 组织机构
 * 
 * @author Tencell006
 *
 */
public class DepartTreeDto {
	/** 编号 */
	private int id;
	/** 父节点 */
	private int parentId;
	/** 名称 */
	private String deptName;
	/** 结束层 */
	private String lastLevel;
	private String lastLevelFmt;
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
	/** 是否为叶子节点 */
	private boolean isLeaf = false;
	/** 只读 */
	private boolean isReadOnly = false;
	/** 标志 */
	private String sig;
	private String sigFmt;
	private Integer isSchool;

	private List<DepartTreeDto> treeList = new ArrayList<DepartTreeDto>();

	/**
	 * 通过递归生成树
	 * 
	 * @param src
	 *            所有元素列表
	 * @param dest
	 *            生成树列表
	 */
	public static void genTreeByDg(List<DepartTreeDto> src, List<DepartTreeDto> dest) {

		List<DepartTreeDto> pfdChild = new ArrayList<DepartTreeDto>();
		for (DepartTreeDto pfdd : dest) {
			for (DepartTreeDto pfds : src) {
				// 输入的操作人id为所有数据中的父操作id
				if (pfdd.getId() == pfds.getParentId()) {
					pfdd.getTreeList().add(pfds);
					pfdChild.add(pfds);
				}
			}
		}

		if (!pfdChild.isEmpty()) {
			genTreeByDg(src, pfdChild);
		}

	}
	/**
	 * 通过递归生成树
	 *
	 * @param src
	 *            所有元素列表
	 * @param dest
	 *            生成树列表
	 */
	public static void genTreeByDgList(List<DepartTreeDto> src, List<DepartTreeDto> dest) {

		List<DepartTreeDto> pfdChild = new ArrayList<DepartTreeDto>();
		for (DepartTreeDto pfdd : dest) {
			for (DepartTreeDto pfds : src) {
				// 输入的操作人id为所有数据中的父操作id
				if (pfdd.getId() == pfds.getParentId()) {
					pfdd.getTreeList().add(pfds);
					pfdChild.add(pfds);
				}
			}
		}

		if (!pfdChild.isEmpty()) {
			genTreeByDg(src, pfdChild);
		}

	}
	/**
	 * 通过递归生成树
	 * 
	 * @param src
	 *            所有元素列表
	 * @param dest
	 *            生成树列表
	 */
	public static void genTreeByDg(List<DepartTreeDto> src, List<DepartTreeDto> dest,Map<Integer,DepartTreeDto> leafMap) {
		
		List<DepartTreeDto> dptChild = new ArrayList<DepartTreeDto>();
		for (DepartTreeDto dptd : dest) {
			for (DepartTreeDto dpts : src) {
				// 输入的操作人id为所有数据中的父操作id
				int pfdId = dptd.getId();
				if (pfdId == dpts.getParentId()) {
					dptd.getTreeList().add(dpts);
					dptChild.add(dpts);
					// 移出叶子节点
					if(leafMap.containsKey(pfdId)){
						leafMap.remove(pfdId);
					}
					// 把最后面这个放入叶子节点
					leafMap.put(dpts.getId(), dpts);
				}
			}
		}
		
		if (!dptChild.isEmpty()) {
			genTreeByDg(src, dptChild,leafMap);
		}
		
	}

	public String getLastLevelFmt() {
		return lastLevelFmt;
	}

	public void setLastLevelFmt(String lastLevelFmt) {
		this.lastLevelFmt = lastLevelFmt;
	}

	public List<DepartTreeDto> getTreeList() {
		return treeList;
	}

	public void setTreeList(List<DepartTreeDto> treeList) {
		this.treeList = treeList;
	}

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

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
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

	public Integer getIsSchool() {
		return isSchool;
	}

	public void setIsSchool(Integer isSchool) {
		this.isSchool = isSchool;
	}
}
