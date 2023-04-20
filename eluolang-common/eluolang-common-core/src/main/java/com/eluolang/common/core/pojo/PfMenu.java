package com.eluolang.common.core.pojo;

/**
 * 系统功能表
 *
 * @author dengrunsen
 * @date 2021/2/2
 */
public class PfMenu {
    /** id */
    private Integer id;
    /** 父编号 */
    private Integer parentId;
    /** 全路径 */
    private String path;
    /** 菜单名 */
    private String menuName;
    /** 导航URL */
    private String navigateUrl;
    /** 菜单图标 */
    private String imageUrl;
    /** 结束层 */
    private Integer lastLevel;

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

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getNavigateUrl() {
        return navigateUrl;
    }

    public void setNavigateUrl(String navigateUrl) {
        this.navigateUrl = navigateUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getLastLevel() {
        return lastLevel;
    }

    public void setLastLevel(Integer lastLevel) {
        this.lastLevel = lastLevel;
    }
}
