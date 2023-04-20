package com.eluolang.system.service.impl;

import com.eluolang.common.core.pojo.FileMgr;
import com.eluolang.common.core.pojo.OperLog;
import com.eluolang.system.dto.ConfigDto;
import com.eluolang.system.dto.OperLogDto;
import com.eluolang.system.mapper.SystemMapper;
import com.eluolang.system.service.SystemService;
import com.eluolang.system.vo.OperLogDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SystemServiceImpl implements SystemService {
    @Autowired
    SystemMapper systemMapper;
//    @Override
//    public String getConfigById(Integer id) {
//        return systemMapper.getConfigById(id);
//    }
private static final int MAX_IDENT = 1000000000;

    @Override
    public int insertLog(OperLog operLog) {
        return systemMapper.insertLog(operLog);
    }

    @Override
    public List<OperLog> getOperLogAll(OperLogDto operLogDto) {
        return systemMapper.getOperLogAll(operLogDto);
    }


//    private static final int MAX_IDENT = 1000000000;
//
//    @Override
//    public Integer getIdent() {
//        Integer ident = 0;
//        String value = systemMapper.getIdent();
//        if (value == null || value == "") {
//            value = "0";
//        }
//        ident = Integer.parseInt(value);
//        if (ident == null) {
//            ident = 0;
//        }
//        if (ident > MAX_IDENT) {
//            ident = 0;
//        }
//        systemMapper.updateIdent(String.valueOf(ident + 1));
//        return ident;
//    }


    @Override
    public FileMgr getFileInfo(String fileId) {
        return systemMapper.getFileInfo(fileId);
    }

    @Override
    public Integer saveFileInfo(FileMgr fileMgr) {
        return systemMapper.saveFileInfo(fileMgr);
    }

    @Override
    public Integer getIdent() {
        Integer ident = 0;
        String value = systemMapper.getIdent();
        if (value == null || value == "") {
            value = "0";
        }
        ident = Integer.parseInt(value);
        if (ident == null) {
            ident = 0;
        }
        if (ident > MAX_IDENT) {
            ident = 0;
        }
        systemMapper.updateIdent(String.valueOf(ident + 1));
        return ident;
    }
}
