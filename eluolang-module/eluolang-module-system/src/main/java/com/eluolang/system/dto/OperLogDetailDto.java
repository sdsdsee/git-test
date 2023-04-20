package com.eluolang.system.dto;

public class OperLogDetailDto {
    /** id */
    private Integer id;
    /** 页码 */
    private Integer pageNum;
    /** 每页显示条数 */
    private Integer pageSize;
    /** 指令标识 */
    private Integer ident;

    public Integer getIdent() {
        return ident;
    }

    public void setIdent(Integer ident) {
        this.ident = ident;
    }

    @Override
    public String toString() {
        return "OperLogDetailDto{" +
                "id=" + id +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
