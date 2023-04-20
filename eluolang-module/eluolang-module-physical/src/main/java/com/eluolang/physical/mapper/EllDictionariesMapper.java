package com.eluolang.physical.mapper;


import org.apache.ibatis.annotations.Param;

public interface EllDictionariesMapper {
    int addDictionaries(@Param("name") String name, @Param("type") String type);
}
