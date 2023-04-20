package com.eluolang.physical.service;


import com.eluolang.common.core.pojo.EllDevice;
import com.eluolang.common.core.pojo.EllExaminer;
import com.eluolang.common.core.pojo.ProjectRules;
import com.eluolang.physical.dto.EllExaminerDto;
import com.eluolang.physical.model.RulesVo;
import org.apache.ibatis.annotations.Param;

import java.text.ParseException;
import java.util.List;

public interface ExaminerService {

    /**
     * 通过监考员验证码查询监考员是否存在
     * @param code
     * @return
     */
    EllExaminer selExaminerByLoginCode(String code);

    /**
     * 监考官登录设备
     * @param deviceId
     * @param deptId
     * @return
     */
    EllDevice examinerLoginDevice(String deviceId,Integer deptId);

    /**
     * 添加监考员
     * @param ellExaminer
     * @return
     */
    int insertExaminer(EllExaminer ellExaminer);

    /**
     * 根据id删除监考员
     * @param id
     * @return
     */
    int deleteExaminer(String id);

    /**
     * 根据id修改监考员信息
     * @param ellExaminer
     * @return
     */
    int updateExaminer(EllExaminer ellExaminer);

    /**
     * 按条件查询监考员信息
     * @param ellExaminerDto
     * @return
     */
    List<EllExaminer> selectExaminer(EllExaminerDto ellExaminerDto);
}
