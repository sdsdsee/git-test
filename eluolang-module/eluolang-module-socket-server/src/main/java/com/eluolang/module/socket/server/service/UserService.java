package com.eluolang.module.socket.server.service;

import com.eluolang.common.core.pojo.*;

/**
 * 定义业务层的设备接口方法
 *
 * @author dengrunsen
 */
public interface UserService {
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
