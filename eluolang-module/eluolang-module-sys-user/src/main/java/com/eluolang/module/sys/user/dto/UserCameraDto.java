package com.eluolang.module.sys.user.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ZengXiaoQian
 * @createDate 2020-11-02
 */
@Api("用户管理模块-接收前端传回来的数据")
public class UserCameraDto {
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    @ApiModelProperty(value = "摄像头位置")
    private String position;
    @ApiModelProperty(value = "摄像头id")
    private Integer videoId;

    @Override
    public String toString() {
        return "UserCameraDto{" +
                "userId='" + userId + '\'' +
                ", position='" + position + '\'' +
                ", videoId='" + videoId + '\'' +
                '}';
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }
}
