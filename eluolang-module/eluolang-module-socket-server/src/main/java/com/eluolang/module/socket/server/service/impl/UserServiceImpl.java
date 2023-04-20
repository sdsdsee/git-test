package com.eluolang.module.socket.server.service.impl;

import com.eluolang.common.core.pojo.EllDailyExercisePlan;
import com.eluolang.common.core.pojo.EllUser;
import com.eluolang.common.core.pojo.PfDepart;
import com.eluolang.module.socket.server.mapper.UserMapper;
import com.eluolang.module.socket.server.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 设备业务逻辑层
 *
 * @author dengrunsen
 */
@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public EllUser selSignUserByCode(String imgId) {
        return userMapper.selSignUserByCode(imgId);
    }

    @Override
    public EllDailyExercisePlan selPlanDataById(Integer deptId) {
        return userMapper.selPlanDataById(deptId);
    }
}

