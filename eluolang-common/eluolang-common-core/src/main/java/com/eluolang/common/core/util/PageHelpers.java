package com.eluolang.common.core.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

public class PageHelpers {

    public static <E>Page<E> startPage(Integer pageNum,Integer pageSize){
        return PageHelper.startPage(pageNum==null? 1 :pageNum,pageSize==null? 10 :pageSize);
    }

}
