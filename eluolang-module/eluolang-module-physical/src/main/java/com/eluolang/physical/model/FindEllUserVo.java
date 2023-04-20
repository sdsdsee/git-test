package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Param;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindEllUserVo {
    /**
     * 账号的id
     */
    private int accountOrgId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 学生号
     */
    private String studentId;
    /**
     * id
     */
    private String id;
    /**
     * 部门id
     */
    private String orgId;
    /**
     * 身份证
     */
    private String idCard;
    /**
     * 名族
     */
    private String ethnic;
    /**
     * 页码
     */
    private int page;
    /**
     * 是否注册
     */
    private String isRegister;
}
