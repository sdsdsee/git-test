package com.eluolang.common.core.util;

import java.util.Random;

/**
 * @author suziwei
 * @date 2020/9/23
 */
public class NumberUtils {
    public static boolean rangeInDefined(int current, int min, int max) {
        return Math.max(min, current) == Math.min(current, max);
    }

    /**
     * 生成6位随机数
     */
    public static int randomNumber() {
        int number = 100000+new Random().nextInt(899999);
        return number;
    }
}
