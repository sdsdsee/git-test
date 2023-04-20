package com.eluolang.module.sys.user.service.impl;

import com.eluolang.common.core.pojo.EllUser;
import com.eluolang.common.core.pojo.FileMgr;
import com.eluolang.common.core.pojo.PfDepart;
import com.eluolang.module.sys.user.dto.DepartDto;
import com.eluolang.module.sys.user.mapper.DepartMapper;
import com.eluolang.module.sys.user.service.DepartService;
import com.eluolang.module.sys.user.util.GradeCalculate;
import com.eluolang.module.sys.user.vo.DepartVo;
import com.eluolang.module.sys.user.vo.EllDeriveDepart;
import org.apache.ibatis.annotations.Param;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组织机构接口
 */
@Service
public class DepartServiceImpl implements DepartService{

    @Resource
    private DepartMapper departMapper;
    
	@Override
	public int regDepart(DepartDto departDto) {
		return departMapper.regDepart(departDto);
	}

	@Override
	public int updateDepart(DepartDto departDto) {
		return departMapper.updateDepart(departDto);
	}
	
	@Override
	public int updateDepartByTid(DepartDto departDto) {
		return departMapper.updateDepartByTid(departDto);
	}

	@Override
	public int delDepartById(String id) {
		return departMapper.delDepartById(id);
	}
	
	@Override
	public int delDepartByPid(String id) {
		return departMapper.delDepartByPid(id);
	}

	@Override
	public List<DepartVo> getAllDepart() {
		return departMapper.getAllDepart();
	}

	@Override
	public int updateDepartPathById(String id) {
		return departMapper.updateDepartPathById(id);
	}

	@Override
	public int updateDepartPathAndPidByTid(String id) {
		return departMapper.updateDepartPathAndPidByTid(id);
	}

	@Override
	public int regDepartByTree(DepartDto departDto) {
		return departMapper.regDepartByTree(departDto);
	}

	@Override
	public int checkExistById(String id) {
		return departMapper.checkExistById(id);
	}

	@Override
	public int checkExistByTid(String id) {
		return departMapper.checkExistByTid(id);
	}

	@Override
	public int queryChildCountById(String id) {
		return departMapper.queryChildCountById(id);
	}

	@Override
	public List<DepartVo> getDepartByOperPath(String path) {
		return departMapper.getDepartByOperPath(path);
	}

	@Override
	public List<DepartVo> getDepartByPath(String path) {
		return departMapper.getDepartByPath(path);
	}

	@Override
	public List<DepartVo> getDepartByOperId(String optId) {
		return departMapper.getDepartByOperId(optId);
	}

	@Override
	public List<DepartVo> getDepartByDepartVoPath(List<DepartVo> dv) {
		return departMapper.getDepartByDepartVoPath(dv);
	}

	@Override
	public List<DepartVo> getDepartByOptIdAndPath(String optId, String path) {
		return departMapper.getDepartByOptIdAndPath(optId,path);
	}

    @Override
    public DepartDto selDepartName(String departName, String parentId, String id) {
        return departMapper.selDepartName(departName, parentId, id);
    }

    @Override
    public int addDepart(List<DepartDto> departDto) {
        return departMapper.addDepart(departDto);
    }

    @Override
    public int selMaxId() {
        return departMapper.selMaxId();
    }

    @Override
    public List<EllDeriveDepart> selDepart(int accountOrgId, int id,String deptId) {
        return departMapper.selDepart(accountOrgId, id,deptId);
    }
    @Scheduled(cron = "0 0 0 1 8 ?")
    @Override
    public void upDepartName() {
        List<EllDeriveDepart> ellDeriveDepartList = departMapper.selAllClass();
        Map<Integer, EllDeriveDepart> ellDeriveDepartMap = new HashMap<>();
        for (int i = 0; i < ellDeriveDepartList.size(); i++) {
            //判断是否为修改过
            if (!ellDeriveDepartMap.containsKey(ellDeriveDepartList.get(i).getParentId())) {
                //查询学生计算年级
                List<EllUser> ellUserList = departMapper.selUser(ellDeriveDepartList.get(i).getId());
                StringBuffer grade = new StringBuffer(GradeCalculate.calculateGrade(ellUserList.get(0).getEnTime(), Integer.parseInt(ellUserList.get(0).getEnGrade())));
                departMapper.upDepartName(grade.toString(), ellDeriveDepartList.get(i).getParentId());
                ellDeriveDepartMap.put(ellDeriveDepartList.get(i).getParentId(), ellDeriveDepartList.get(i));
                System.out.println("8月跟新了年级部门的名称");
            }
        }

    }

    @Override
    public PfDepart selSchoolByDeptId(Integer deptId) {
        return departMapper.selSchoolByDeptId(deptId);
    }

    @Override
    public List<DepartVo> getDepartByTreeId(String tid) {
        return departMapper.getDepartByTreeId(tid);
    }

	@Override
	public int queryChildCountByTid(String tid) {
		return departMapper.queryChildCountByTid(tid);
	}

	@Override
	public int queryUserByDeptId(String id) {
		return departMapper.queryUserByDeptId(id);
	}
}
