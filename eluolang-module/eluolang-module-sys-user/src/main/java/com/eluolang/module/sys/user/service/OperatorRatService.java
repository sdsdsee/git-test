package com.eluolang.module.sys.user.service;

/**
 * 组织机构接口
 */
public interface OperatorRatService {

	/**
	 * 添加关系
	 * @Param id 主键
	 * @param optId 操作员id
	 * @param module 模型
	 * @param rat 关系id
	 * @return
	 */
	int regOperatorRat(String id,String optId, String module, String rat);

	/**
	 * 删除关系
	 * @param module 模型
	 * @param rat 关系id
	 * @return
	 */
	int delOperatorRat(String module, String rat);
}
