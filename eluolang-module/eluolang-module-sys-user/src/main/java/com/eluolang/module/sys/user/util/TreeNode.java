package com.eluolang.module.sys.user.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.models.auth.In;

import java.util.ArrayList;
import java.util.List;

/**
 * 树
 */
public class TreeNode {
    private Integer id;//自己的id
    @JsonProperty("parentId") //返回的json的名称 parentId ,为了确定层级关系
    private Integer pid;
    private String title;//名称
    private String icon;
    private String href;
    /** 结束层 */
    private Integer lastLevel;
    private Boolean spread = true;//是否展开
    private List<TreeNode> children = new ArrayList<TreeNode>();

    /**
     * 0为不选中  1为选中
     */
    private String checkArr="0";    /**
     * 区域dtree的构造器，我们只用到了这四个
     * @param id id
     * @param pid 父亲parentId
     * @param title 名称
     */
    public TreeNode(Integer id, Integer pid, String title, Integer lastLevel) {
        this.id = id;
        this.pid = pid;
        this.title = title;
        this.lastLevel = lastLevel;
    }

    public TreeNode(Integer id, Integer pid, String title) {
        this.id = id;
        this.pid = pid;
        this.title = title;
    }

    public TreeNode() {
    }

    public TreeNode(Integer id, Integer pid, String title, String icon, String href, Boolean spread, List<TreeNode> children, String checkArr) {
        this.id = id;
        this.pid = pid;
        this.title = title;
        this.icon = icon;
        this.href = href;
        this.spread = spread;
        this.children = children;
        this.checkArr = checkArr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Boolean getSpread() {
        return spread;
    }

    public void setSpread(Boolean spread) {
        this.spread = spread;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public String getCheckArr() {
        return checkArr;
    }

    public void setCheckArr(String checkArr) {
        this.checkArr = checkArr;
    }

    public Integer getLastLevel() {
        return lastLevel;
    }

    public void setLastLevel(Integer lastLevel) {
        this.lastLevel = lastLevel;
    }
}
