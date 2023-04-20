package com.eluolang.physical.service.impl;

import com.eluolang.common.core.pojo.EllTestProject;
import com.eluolang.physical.mapper.ProjectMapper;
import com.eluolang.physical.model.AddProjectVo;
import com.eluolang.physical.model.FindProjectVo;
import com.eluolang.physical.service.ProjectService;
import com.eluolang.physical.util.CreateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public int addProject(AddProjectVo addProjectVo) {
        EllTestProject testProject = new EllTestProject();
        testProject.setProIcon(addProjectVo.getProIcon());
        testProject.setCreateById(addProjectVo.getCreateBy());
        testProject.setProName(addProjectVo.getProName());
        testProject.setProUnit(addProjectVo.getProUnit());
        testProject.setProNameAbbreviation(addProjectVo.getProNameAbbreviation());
        testProject.setCreateTime(CreateTime.getTime());
        return projectMapper.addProject(testProject);
    }

    @Override
    public int updataProject(AddProjectVo addProjectVo) throws ParseException {
        EllTestProject testProject = new EllTestProject();
        testProject.setId(addProjectVo.getId());
        testProject.setProIcon(addProjectVo.getProIcon());
        testProject.setUpdateById(addProjectVo.getCreateBy());
        testProject.setProName(addProjectVo.getProName());
        testProject.setProUnit(addProjectVo.getProUnit());
        testProject.setProNameAbbreviation(addProjectVo.getProNameAbbreviation());
        testProject.setUpdateTime(CreateTime.getTime());
        return projectMapper.updataProject(testProject);
    }

    @Override
    public List<EllTestProject> findProjectSys() {
        List<EllTestProject> list = projectMapper.findProject();

        return list;
    }

    @Override
    public List<FindProjectVo> findProject() {
        List<EllTestProject> testProjectList = projectMapper.findProject();
      /*  if (testProjectList.size()==0){
            return null;
        }*/
        List<FindProjectVo> findProjectVos = testProjectList.stream().map(testProject -> {
            FindProjectVo findProjectVo = new FindProjectVo();
            findProjectVo.setId(testProject.getId());
            findProjectVo.setProIcon(testProject.getProIcon());
            findProjectVo.setProName(testProject.getProName());
            findProjectVo.setProUnit(testProject.getProUnit());
            findProjectVo.setProNameAbbreviation(testProject.getProNameAbbreviation());
            return findProjectVo;
        }).collect(Collectors.toList());
        return findProjectVos;
    }

    @Override
    public int deleteProject(int id) {
        return projectMapper.deleteProject(id);
    }

    @Override
    public int deleteProjectSys(int id) {
        return projectMapper.deleteProjectSys(id);
    }
}
