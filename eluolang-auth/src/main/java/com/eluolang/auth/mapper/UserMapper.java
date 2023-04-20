package com.eluolang.auth.mapper;

import com.eluolang.common.core.hardware.vo.PfDepartVo;
import com.eluolang.common.core.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author suziwei
 * @date 2020/9/7
 */
@Mapper
public interface UserMapper {

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param pwd      密码
     * @return 结果
     */
    public LoginUser login(@Param("username") String username, @Param("pwd") String pwd);

    /**
     * 通过用户名查询用户基本信息
     *
     * @param username 用户名
     * @return 结果
     */
    public LoginUser getUser(String username);

    /**
     * 根据操作员ID查询所拥有权限菜单
     * @param optId
     * @return
     */
    public List<PfMenu> getPfMenuByOptId(Integer optId);


    /**
     * 根据操作员ID查询其所拥有的部门权限
     * @param id
     * @return
     */
    List<PfDepartVo> selPfDepartById(Integer id);
}
