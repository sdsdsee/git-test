package com.eluolang.physical.mapper;

import com.eluolang.common.core.pojo.EllTestProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectMapper {
    int addProject(@Param("testProject") EllTestProject testProject);

    int updataProject(@Param("updateTestProject") EllTestProject updateTestProject);

    List<EllTestProject> findProject();

    /**
     * 通过id查询
     * @param id
     * @return
     */
    EllTestProject findProjectById(String id);
    int deleteProject(int id);

    int deleteProjectSys(int id);
}
