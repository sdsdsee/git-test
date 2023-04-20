package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllSkipUser implements Serializable, Cloneable {
    /**
     * id
     */
    private String id;
    /**
     * 微信wx_open_id
     */
    private String wxOpenId;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新人
     */
    private String updateById;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 电话
     */
    private String phone;
    /**
     * 绑定的用户id
     */
    private String userId;
    /**
     * 是否删除
     */
    private int isDelete;
    /**
     * 用户角色1本人2父亲3母亲
     */
    private int userRole;
    /**
     * 是否为游客
     */
    private int isTourist;
    /**
     * 身份证
     */
    private String skipIdCard;
    /**
     * 姓名
     */
    private String userName;
}