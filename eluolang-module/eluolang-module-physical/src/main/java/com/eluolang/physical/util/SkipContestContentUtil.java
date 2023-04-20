package com.eluolang.physical.util;

//比赛模式测试单位
public class SkipContestContentUtil {
    public static String contentUnit(int pattern) {
        String unit = "";
        switch (pattern) {
            case 1:
                unit = "s";
                break;
            case 2:
                unit = "个";
                break;
        }
        return unit;
    }
}
