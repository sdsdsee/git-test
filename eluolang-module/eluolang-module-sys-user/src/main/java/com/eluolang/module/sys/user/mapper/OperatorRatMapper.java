package com.eluolang.module.sys.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 组织机构接口
 */
@Mapper
public interface OperatorRatMapper {

	/**
	 * 添加关系
	 * *@Param id 主键
	 * @param optId 操作员id
	 * @param module 模型
	 * @param rat 关系id
	 * @return
	 */
	int regOperatorRat(@Param("id") String id,@Param("optId") String optId, @Param("module") String module, @Param("rat") String rat);

	/**
	 * 删除关系
	 * @param module 模型
	 * @param rat 关系id
	 * @return
	 */
	int delOperatorRat(@Param("module") String module, @Param("rat") String rat);


}
