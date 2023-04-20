package com.eluolang.module.sys.user.util;

import com.eluolang.module.sys.user.constant.SysUserConstant;

/**
 * 生成6位数随机邮箱验证码工具类
 *
 * @author ZengxiaoQian
 * @createDate 2020/8/25
 */
public class EmailCodeUtils {
    /**
     * 生成6位随机验证码
     *
     * @return String
     */
    public static String getNumber() {
        String str = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String code = "";
        for (int i = 0; i < SysUserConstant.MAIL_CODE_LENGTH; i++) {
            int index = (int) (Math.random() * str.length());
            code += str.charAt(index);
        }
        return code;
    }
}
