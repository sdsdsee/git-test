package com.eluolang.common.core.exception;

/**
 * 全局异常类
 *
 * @author suziwei
 * @date 2020/8/31
 */
public class BaseException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * 错误信息
     */
    private String message;

    public BaseException(String message)
    {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
