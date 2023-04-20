package com.eluolang.physical.mapper;

import com.eluolang.common.core.pojo.*;
import com.eluolang.physical.dto.EllExaminerDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface ExaminerMapper {

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
    EllDevice examinerLoginDevice(@Param("deviceId") String deviceId,@Param("deptId") Integer deptId);

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
