package com.eluolang.common.core.pojo;

/**
 * 数据字典表
 * @author dengrunsen
 *
 * @date 2020/2/22
 */
public class SysDataDictionary {
    /** id */
    private Integer id;
    /** 名称 */
    private String name;
    /** 类型(1.民族, 2.人员类别, 3.岗位, 4.国籍, 5.申请事由,6.部门类型) */
    private Integer type;

    @Override
    public String toString() {
        return "SysDataDictionary{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
