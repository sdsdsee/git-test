package com.eluolang.physical.service;

import com.eluolang.common.core.pojo.EllTestProject;
import com.eluolang.physical.model.AddProjectVo;
import com.eluolang.physical.model.FindProjectVo;

import java.text.ParseException;
import java.util.List;

public interface ProjectService {
    /**
     * 添加项目
     */
    int addProject(AddProjectVo addProjectVo);

    /**
     * 修改项目
     */
    int updataProject(AddProjectVo addProjectVo) throws ParseException;

    /**
     * 管理员查看项目
     */
    List<EllTestProject> findProjectSys();

    /**
     * 用户查看项目
     */
    List<FindProjectVo> findProject();

    /**
     * 用户删除项目逻辑删
     */
    int deleteProject(int id);

    /**
     * 用户删除项目逻辑删
     */
    int deleteProjectSys(int id);

}
