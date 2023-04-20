package com.eluolang.common.core.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 描述： ${description}
 * @date 2020/3/10
 */
public class CollectionUtil {
    public static List<String> strToList(String str){
        if (StringUtils.isNotBlank(str)) {
            String[] split = str.split(",");
            List<String> strings = Arrays.asList(split);
            return strings;
        }
        return Collections.EMPTY_LIST;
    }
}
