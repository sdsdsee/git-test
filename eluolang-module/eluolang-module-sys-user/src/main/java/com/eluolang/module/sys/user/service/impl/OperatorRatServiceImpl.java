package com.eluolang.module.sys.user.service.impl;

import com.eluolang.module.sys.user.mapper.OperatorRatMapper;
import com.eluolang.module.sys.user.service.OperatorRatService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 组织机构接口
 */
@Service
public class OperatorRatServiceImpl implements OperatorRatService{

    @Resource
    private OperatorRatMapper operatorRatMapper;

	@Override
	public int regOperatorRat(String id,String optId, String module, String rat) {
		return operatorRatMapper.regOperatorRat(id,optId, module, rat);
	}

	@Override
	public int delOperatorRat(String module, String rat) {
		return operatorRatMapper.delOperatorRat(module, rat);
	}
	
}
