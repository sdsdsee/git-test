package com.eluolang.module.sys.user.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import java.util.Arrays;

/**
 * @author ZengXiaoQian
 * @createDate 2020-10-12
 */
@Api("用户管理模块-接收前端传回来的数据")
public class DelUserDto {
    @ApiModelProperty(value = "用户id数组(String类型)")
    private String id;

    @ApiModelProperty(value = "不删除的用户id数组")
    private String[] noId;

    @Override
    public String toString() {
        return "DelUserDto{" +
                "id='" + id + '\'' +
                ", noId=" + Arrays.toString(noId) +
                '}';
    }

    public String[] getNoId() {
        return noId;
    }

    public void setNoId(String[] noId) {
        this.noId = noId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
