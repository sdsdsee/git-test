package com.eluolang.module.sys.user.service;

import com.eluolang.common.core.pojo.FileMgr;
import com.eluolang.common.core.pojo.PfDepart;
import com.eluolang.module.sys.user.dto.DepartDto;
import com.eluolang.module.sys.user.vo.DepartVo;
import com.eluolang.module.sys.user.vo.EllDeriveDepart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组织机构接口
 */
public interface DepartService {

    /**
     * 添加组织机构
     *
     * @param departDto 传入的departDto对象
     * @return 成功或失败状态
     */
    int regDepart(DepartDto departDto);

    /**
     * 添加组织机构
     *
     * @param departDto 传入的departDto对象
     * @return 成功或失败状态
     */
    int regDepartByTree(DepartDto departDto);

    /**
     * 更新组织机构
     *
     * @param departDto 传入的departDto对象
     * @return 成功或失败状态
     */
    int updateDepart(DepartDto departDto);

    /**
     * 更新组织机构
     *
     * @param departDto 传入的departDto对象
     * @return 成功或失败状态
     */
    int updateDepartByTid(DepartDto departDto);

    /**
     * 通过组织机构id更新组织机构path
     *
     * @param id 组织机构id
     * @return int
     */
    int updateDepartPathById(String id);

    /**
     * 通过组织机构id更新组织机构path
     *
     * @param id 组织机构id
     * @return int
     */
    int updateDepartPathAndPidByTid(@Param("id") String id);

    /**
     * 删除组织机构信息
     *
     * @param id 组织机构id
     * @return int
     */
    int delDepartById(@Param("id") String id);

    /**
     * 删除组织机构信息
     *
     * @param id 组织机构id
     * @return int
     */
    int delDepartByPid(@Param("id") String id);

    /**
     * 获取所有组织机构信息
     *
     * @return {@link List<DepartVo>}
     */
    List<DepartVo> getAllDepart();

    /**
     * 通过id查询是否存在数据
     *
     * @param id
     * @return
     */
    int checkExistById(String id);

    /**
     * 通过id查询是否存在数据
     *
     * @param id
     * @return
     */
    int checkExistByTid(String id);

    /**
     * 通过部门id从学生表中查询学生人数
     *
     * @param id
     * @return
     */
    int queryUserByDeptId(String id);

    /**
     * 通过id查询出后代个数
     *
     * @param id id
     * @return 后代个数
     */
    int queryChildCountById(String id);

    /**
     * 通过tid查询出后代个数
     *
     * @param tid tid
     * @return 后代个数
     */
    int queryChildCountByTid(String tid);

    /**
     * 通过操作员path获取组织机构信息
     *
     * @param path 操作员path
     * @return {@link List<DepartVo>}
     */
    List<DepartVo> getDepartByOperPath(String path);

    /**
     * 通过path获取组织机构信息
     *
     * @param path 组织机构path
     * @return {@link List<DepartVo>}
     */
    List<DepartVo> getDepartByPath(String path);

    /**
     * 通过操作员id获取组织机构信息
     *
     * @param optId 操作员id
     * @return
     */
    List<DepartVo> getDepartByOperId(String optId);

    /**
     * 通过树形id获取部门信息
     *
     * @param tid 树形id
     * @return
     */
    List<DepartVo> getDepartByTreeId(String tid);

    /**
     * 通过DepartVo中的Path查询出所有列表信息
     *
     * @param dv
     * @return
     */
    List<DepartVo> getDepartByDepartVoPath(List<DepartVo> dv);

    /**
     * 通过optId和path获取组织机构信息
     *
     * @param optId 操作员id
     * @param path  路径
     * @return
     */
    List<DepartVo> getDepartByOptIdAndPath(String optId, String path);

    /**
     * 查询部门名称
     */
    DepartDto selDepartName(String departName, String parentId, String id);

    /**
     * 添加部门
     */
    int addDepart(List<DepartDto> departDto);

    /**
     * 查询最大id
     */
    int selMaxId();

    /**
     * 查询有权限的部门名称和id
     */
    List<EllDeriveDepart> selDepart(int accountOrgId, int id,String deptId);



    /**
     * 定时修改年级名称（目前只有斯普莱斯有
     * ）
     */
    void upDepartName();


    /**
     * 根据部门id查询学校信息
     * @param deptId
     * @return
     */
    PfDepart selSchoolByDeptId(Integer deptId);

}
