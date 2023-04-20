package com.eluolang.common.core.pojo;

/**
 * 角色权限分配表
 *
 * @author dengrunsen
 * @date 2021/2/24
 */
public class EllOperatorRat {
    /**
     * id
     */
    private String id;
    /**
     * 操作员编号
     */
    private Integer optId;
    /**
     * pf_menu id = 1,pf_depart id = 2,pf_device id = 3, pf_area id = 4
     */
    private Integer module;
    /**
     * module 对应的ID
     */
    private String rat;

    @Override
    public String toString() {
        return "PfOperatorRat{" +
                "id=" + id +
                ", optId=" + optId +
                ", module=" + module +
                ", rat=" + rat +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getOptId() {
        return optId;
    }

    public void setOptId(Integer optId) {
        this.optId = optId;
    }

    public Integer getModule() {
        return module;
    }

    public void setModule(Integer module) {
        this.module = module;
    }

    public String getRat() {
        return rat;
    }

    public void setRat(String rat) {
        this.rat = rat;
    }
}
