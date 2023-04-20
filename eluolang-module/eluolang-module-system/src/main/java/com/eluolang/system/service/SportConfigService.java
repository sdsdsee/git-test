package com.eluolang.system.service;

import com.eluolang.system.dto.EllSportConfigDto;
import com.eluolang.system.dto.EllSportConfigRuleDto;
import com.eluolang.system.dto.SportRuleDto;
import com.eluolang.system.vo.EllSportConfigRuleVo;
import com.eluolang.system.vo.EllSportConfigVo;

import java.util.List;

public interface SportConfigService {
    /**
     * 新增运动指南配置
     * @param ellSportConfigDto
     * @return
     */
    int insertSportConfig(EllSportConfigDto ellSportConfigDto);

    /**
     * 删除运动指南配置
     * @param id
     * @return
     */
    int deleteSportConfig(String id);

    /**
     * 修改运动指南配置
     * @param ellSportConfigDto
     * @return
     */
    int updateSportConfig(EllSportConfigDto ellSportConfigDto);

    /**
     * 查询所有运动指南配置
     * @return
     */
    List<EllSportConfigVo> selAllSportConfig();

    /**
     * 新增运动指南配置规则
     * @param sportConfigRuleDto
     * @return
     */
    int insertSportConfigRule(EllSportConfigRuleDto sportConfigRuleDto);

    /**
     * 删除运动指南配置规则
     * @param id
     * @return
     */
    int deleteSportConfigRule(String id);

    /**
     * 修改运动指南配置规则
     * @param sportConfigRuleDto
     * @return
     */
    int updateSportConfigRule(EllSportConfigRuleDto sportConfigRuleDto);

    /**
     * 按条件查询运动指南配置规则
     * @param sportRuleDto
     * @return
     */
    List<EllSportConfigRuleVo> selSportConfigRule(SportRuleDto sportRuleDto);

    /**
     * 根据id查询运动指南配置规则
     * @return
     */
    EllSportConfigRuleVo selSportConfigRuleById(String id);
}
