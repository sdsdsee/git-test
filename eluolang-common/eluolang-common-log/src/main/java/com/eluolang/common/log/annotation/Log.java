package com.eluolang.common.log.annotation;

import com.eluolang.common.log.constant.OperObj;
import com.eluolang.common.log.constant.OperType;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 * @author ZengXiaoQian
 * @createDate 2020-9-10
 */

@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 模块（系统管理）
     */
    public String title() default "";

    /**
     * 操作内容（具体做了什么事情，比如新增xxx）
     */
    public String content() default "";

    /**
     * 操作类别（增加、删除、修改）
     */
    public OperType operType() default OperType.OTHER;

    /**
     * 操作角色（用户、设备）
     */
    public OperObj operObj() default OperObj.SYSTEM;

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;
}
