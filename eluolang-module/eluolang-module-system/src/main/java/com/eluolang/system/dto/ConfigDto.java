package com.eluolang.system.dto;

public class ConfigDto {
    /** id */
    private Integer id ;
    /** 配置列名 */
    private String name ;
    /** 配置的值 */
    private String value ;

    public ConfigDto() {
    }

    public ConfigDto(Integer id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "ConfigDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
