package com.eluolang.system.mapper;

import com.eluolang.common.core.pojo.FileMgr;
import com.eluolang.common.core.pojo.OperLog;
import com.eluolang.system.dto.ConfigDto;
import com.eluolang.system.dto.OperLogDto;
import com.eluolang.system.vo.OperLogDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SystemMapper {
//    /**
//     * 查询所有配置信息
//     * @return
//     */
//    public List<Config> getByConfigAll();
//
//    /**
//     * 根据id查询value值
//     * @param id
//     * @return
//     */
//    public String getConfigById(Integer id);
//
//    /**
//     * 更新配置信息
//     * @param configDtoList
//     * @return
//     */
//    public int updateConfig(@Param("configDtoList") List<ConfigDto> configDtoList);

    /**
     * 保存操作日志
     *
     * @param operLog
     * @return
     */
    public int insertLog(OperLog operLog);

    /**
     * 按条件查询操作日志
     *
     * @param operLogDto
     * @return
     */
    public List<OperLog> getOperLogAll(OperLogDto operLogDto);
//
//    /**
//     *获取ident指令标识
//     */
//    String getIdent();
//
//    /**
//     * 修改指令标识自增
//     * @return
//     */
//    Integer updateIdent(String ident);

    /**
     * 根据文件id查询文件信息
     *
     * @param fileId
     * @return
     */
    FileMgr getFileInfo(String fileId);

    /**
     * 保存上传的文件信息
     *
     * @param fileMgr
     * @return
     */
    Integer saveFileInfo(FileMgr fileMgr);

    /**
     * 获取ident指令标识
     */
    String getIdent();

    /**
     * 修改指令标识自增
     * @return
     */
    Integer updateIdent(String ident);
}
