package com.eluolang.app.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSkipUserVo implements Serializable, Cloneable {
    /**
     * id
     */
    private String id;
    /**
     * 微信wx_open_id
     */
    private String wxOpenId;
    /**
     * 创建人
     */
    private String createdById;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 电话
     */
    private String phone;
    /**
     * 绑定的用户id
     */
    private String userId;
    /**
     * 用户角色1本人2父亲3母亲
     */
    private int userRole;
    /**
     * 验证码
     */
    private String code;
    /**
     * 身份证
     */
    private String idCard;
    /**
     * 是否为游客
     */
    private int isTourist;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 头像id
     */
    private String imgId;
}