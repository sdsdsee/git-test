package com.eluolang.system.service.impl;

import com.eluolang.common.core.pojo.FileMgr;
import com.eluolang.common.core.pojo.OperLog;
import com.eluolang.system.dto.EllSportConfigDto;
import com.eluolang.system.dto.EllSportConfigRuleDto;
import com.eluolang.system.dto.OperLogDto;
import com.eluolang.system.dto.SportRuleDto;
import com.eluolang.system.mapper.SportConfigMapper;
import com.eluolang.system.mapper.SystemMapper;
import com.eluolang.system.service.SportConfigService;
import com.eluolang.system.service.SystemService;
import com.eluolang.system.vo.EllSportConfigRuleVo;
import com.eluolang.system.vo.EllSportConfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SportConfigServiceImpl implements SportConfigService {
    @Autowired
    SportConfigMapper sportConfigMapper;

    @Override
    public int insertSportConfig(EllSportConfigDto ellSportConfigDto) {
        return sportConfigMapper.insertSportConfig(ellSportConfigDto);
    }

    @Override
    public int deleteSportConfig(String id) {
        return sportConfigMapper.deleteSportConfig(id);
    }

    @Override
    public int updateSportConfig(EllSportConfigDto ellSportConfigDto) {
        return sportConfigMapper.updateSportConfig(ellSportConfigDto);
    }

    @Override
    public List<EllSportConfigVo> selAllSportConfig() {
        return sportConfigMapper.selAllSportConfig();
    }

    @Override
    public int insertSportConfigRule(EllSportConfigRuleDto sportConfigRuleDto) {
        return sportConfigMapper.insertSportConfigRule(sportConfigRuleDto);
    }

    @Override
    public int deleteSportConfigRule(String id) {
        return sportConfigMapper.deleteSportConfigRule(id);
    }

    @Override
    public int updateSportConfigRule(EllSportConfigRuleDto sportConfigRuleDto) {
        return sportConfigMapper.updateSportConfigRule(sportConfigRuleDto);
    }

    @Override
    public List<EllSportConfigRuleVo> selSportConfigRule(SportRuleDto sportRuleDto) {
        return sportConfigMapper.selSportConfigRule(sportRuleDto);
    }

    @Override
    public EllSportConfigRuleVo selSportConfigRuleById(String id) {
        return sportConfigMapper.selSportConfigRuleById(id);
    }
}
