package com.eluolang.module.sys.user.mapper;

import com.eluolang.common.core.hardware.vo.PfDepartVo;
import com.eluolang.common.core.pojo.*;
import com.eluolang.module.sys.user.dto.*;
import com.eluolang.module.sys.user.model.PfOperatorVo;
import com.eluolang.module.sys.user.vo.DepartVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户管理接口
 *
 * @author  dengrunsen
 * @createDate  2020-8-25
 */
@Mapper
public interface SysUserMapper {

     /**
      * 验证用户名是否重复
      * @param
      * @return
      */
     int verifyUsernameRepeat(@Param("name") String name);

     /**
      * 根据用户名查询用户真实姓名
      * @param
      * @return
      */
     PfOperator getRealNameByName(@Param("name") String name);

     /**
      * 根据用户id修改背景路径
      * @param
      * @return
      */
     Integer modifyBgmUrl(@Param("id") Integer id,
                          @Param("backgroundUrl") String backgroundUrl,
                          @Param("partBackgroundUrl") String partBackgroundUrl);

      /**
       * 验证传回来的用户名和邮箱是否正确
       *
       * @param name 用户名
       * @param email 邮箱
       * @return int
       */
     int getCode(@Param("name") String name, @Param("email") String email);

     /**
      * 修改密码
      *
      * @param name 用户名
      * @param newPwd 新密码
      * @return int
      */
     int modifyPwd(@Param("name") String name, @Param("newPwd") String newPwd);

     /**
      * 记得密码-在修改密码之前先判断用户对应的原密码是否正确
      *
      * @param oldPwd 旧密码
      * @param name 用户名
      * @return int
      */
     int selectUserPwd(@Param("oldPwd") String oldPwd, @Param("name") String name);


     /**
      * 查询用户列表
      *
      * @param path 路径
      * @return List<PfOperator>
      */
     List<PfOperator> selectUser(String path);

     /**
      * 根据用户id查询出用户基本信息
      * @param
      * @return
      */
     PfOperatorVo getPersonalInfo(@Param("id") Integer id);

     /**
      * 查询邮箱的有效期
      * @param
      * @return
      */
     String emailValidateTime();

     /**
      * 用户修改本人信息
      *
      * @param sysUserDto 参数用户类
      * @return int
      */
     int modifyPersonalInfo(SysUserDto sysUserDto);

     /**
      * 获取到所有的用户id
      * @param
      * @return
      */
     List<Integer> getAllUserId(UserDto userDto);

//
//     /**
//      * 查询用户的注销时间
//      *
//      * @return List<SysUserVo>
//      */
//     List<SysUserVo> getUserLogout();

//
//     /**
//      * 管理员启用、禁用用户
//      *
//      * @param state 用户状态
//      * @param id 用户id
//      * @return int
//      */
//     int modifyUserState(@Param("state") Integer state, @Param("id") Integer id);

     /**
      * 根据操作员ID查询其所拥有的菜单权限
      * @param id
      * @return
      */
     List<PfMenu> selPfMenuById(@Param("id") Integer id);

     /**
      * 根据操作员ID查询其所拥有的部门权限
      * @param id
      * @return
      */
     List<PfDepart> selPfDepartById(@Param("id") Integer id);

     /**
      * 添加操作员
      * @param pfOperatorDto
      * @return
      */
     int addPfOperator(PfOperatorDto pfOperatorDto);

     /**
      * 根据操作员ID查询其所拥有的所有部门权限
      * @param optId
      * @return
      */
     List<DepartVo> selDepartByOptId(Integer optId);

     /**
      * 通过操作员账号验证是否存在此账号
      * @param optName
      * @return
      */
     int pfOperatorIsExist(String optName);

     /**
      * 添加操作员权限
      * @param pfOperatorRatList
      * @return
      */
     int addPfOperatorRat(@Param("pfOperatorRatList") List<PfOperatorRat> pfOperatorRatList);

     /**
      * 修改操作员
      * @param pfOperatorDto
      * @return
      */
     int updatePfOperator(PfOperatorDto pfOperatorDto);

     /**
      * 根据操作员id删除操作员所拥有权限
      * @param optId
      * @return
      */
     int delPfOperatorRat(Integer optId);

     /**
      * 根据操作员id删除操作员信息
      * @param optId
      * @return
      */
     int delPfOperator(Integer optId);

     /**
      * 根据父级id查询操作员
      * @param optId
      * @return
      */
     int selPfOperatorByParentId(Integer optId);

     /**
      * 根据路径查询操作员
      * @param path
      * @return
      */
     List<PfOperator> selPfOperatorByPath(String path);

     /**
      * 根据手机号查询操作员
      * @param phone
      * @return
      */
     List<PfOperator> selPfOperatorByPhone(String phone);

     /**
      * 按类型查询数据字典
      * @return
      */
     List<SysDataDictionary> selSysDataDictionaryByType(Integer type);

     /**
      * 添加数据字典
      * @param sysDataDictionary
      * @return
      */
     int addSysDataDictionary(SysDataDictionary sysDataDictionary);

     /**
      * 根据类型和名称判断是否存在相同数据
      * @param sysDataDictionary
      * @return
      */
     int selSysDataDictionaryIsExist(SysDataDictionary sysDataDictionary);

     /**
      * 根据ID修改字典数据
      * @param sysDataDictionary
      * @return
      */
     int updateSysDataDictionary(SysDataDictionary sysDataDictionary);

     /**
      * 根据id删除数据字典
      * @param id
      * @return
      */
     int delSysDataDictionary(Integer id);

     /**
      * 保存上传的文件信息
      * @param fileMgr
      * @return
      */
     Integer saveFileInfo(FileMgr fileMgr);

     /**
      * 根据文件id查询文件信息
      * @param fileId
      * @return
      */
     FileMgr getFileInfo(String fileId);
}
