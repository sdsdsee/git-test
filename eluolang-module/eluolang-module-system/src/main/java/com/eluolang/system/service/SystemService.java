package com.eluolang.system.service;

import com.eluolang.common.core.pojo.FileMgr;
import com.eluolang.common.core.pojo.OperLog;
import com.eluolang.system.dto.ConfigDto;
import com.eluolang.system.dto.OperLogDto;
import com.eluolang.system.vo.OperLogDetailVo;

import java.util.List;

public interface SystemService {
//    /**
//     * 根据id
//     * @param id
//     * @return
//     */
//    public String getConfigById(Integer id);
//    /**
//     * 更新配置信息
//     * @param configDtoList
//     * @return
//     */
//    public int updateConfig(List<ConfigDto> configDtoList);

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


//    /**
//     *获取ident指令标识
//     */
//    Integer getIdent();

    /**
     * 查询文件信息
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
    Integer getIdent();

}
