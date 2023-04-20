package com.eluolang.module.socket.server.mapper;

import com.eluolang.common.core.pojo.*;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定义数据访问层设备接口方法
 *
 * @author dengrunsen
 */
@Mapper
public interface UserMapper {
    /**
     * 根据旷视人脸底库照片id查询学生信息
     *
     * @param imgId
     * @return
     */
    EllUser selSignUserByCode(String imgId);

    /**
     * 根据部门id查询所属学校信息
     *
     * @param deptId
     * @return
     */
    EllDailyExercisePlan selPlanDataById(Integer deptId);
}
