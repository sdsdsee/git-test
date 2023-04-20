package com.eluolang.common.core.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * PageHelper工具类
 *
 * @author suziwei
 * @date 2020/8/21
 */
public class PageHelperUtil
{
    /**
     * 分页空值判断
     *
     * @return String
     */
    public static <E> Page<E> startPage(Integer pageNum, Integer pageSize)
    {
        return PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
    }
}
