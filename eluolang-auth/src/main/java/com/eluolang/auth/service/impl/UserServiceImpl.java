package com.eluolang.auth.service.impl;

import com.eluolang.auth.mapper.UserMapper;
import com.eluolang.auth.service.UserService;
import com.eluolang.common.core.constant.CacheConstant;
import com.eluolang.common.core.enums.UserStatus;
import com.eluolang.common.core.exception.BaseException;
import com.eluolang.common.core.hardware.vo.PfDepartVo;
import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.util.MD5Uitls;
import com.eluolang.common.core.util.StringUtils;
import com.eluolang.common.security.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author suziwei
 * @date 2020/9/7
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private TokenService tokenService;

    static final String TYPE1="1";
    static final String TYPE2="2";
    static final String TYPE3="3";
    static final String TYPE4="4";

    @Override
    public LoginUser getUserInfo(String username) {
        return null;
    }

    @Override
    public LoginUser login(String username, String pwd) {
        /** 用户名、密码非空验证 */
        if (username.isEmpty() || pwd.isEmpty()) {
            throw new BaseException("用户/密码必须填写！");
        }
        /** 根据用户名、密码获取用户信息 */
        LoginUser loginUser = userMapper.login(username, MD5Uitls.MD5Lower(pwd, username));
        if (StringUtils.isNull(loginUser)) {
            throw new BaseException("登录用户：" + username + " 不存在或密码错误！");
        }
        if   (UserStatus.LOGOUT.getCode()==loginUser.getStatus()) {
            throw new BaseException("对不起，您的账号：" + username + " 已冻结！");
        }
//        if (UserStatus.DISABLE.getCode()==loginUser.getState()) {
//            throw new BaseException("对不起，您的账号：" + username + " 已禁用！");
//        }
        List<PfMenu> pfMenuList = userMapper.getPfMenuByOptId(loginUser.getId());
        List<PfDepartVo> pfDepartList = userMapper.selPfDepartById(loginUser.getId());
        loginUser.setPfMenuList(pfMenuList);
        loginUser.setPfDepartList(pfDepartList);
//        /** 根据用户id获取角色list */
//        List<SysRole> sysRoleList = userMapper.getRole(loginUser.getId());
//        /** 角色Set */
//        Set<String> roleSet = new HashSet<>();
//        if (!(sysRoleList.isEmpty())) {
//            /** 总权限list */
//            List<SysPermission> sysPermissionsLists = new ArrayList<>();
//            for (int i = 0; i < sysRoleList.size(); i++) {
//                /** 根据角色id获取单个角色权限list */
//                List<SysPermission> sysPermissionsList = userMapper.getPermission(sysRoleList.get(i).getRoleId());
//                /** 追加到总权限中 */
//                sysPermissionsLists.addAll(sysPermissionsList);
//                roleSet.add(sysRoleList.get(i).getRoleName());
//            }
//            loginUser.setRoles(roleSet);
//            /** 权限Set,不同权限等级 */
//            Set<String> permSet = new HashSet<>();
//            Set<String> permSet1 = new HashSet<>();
//            Set<String> permSet2 = new HashSet<>();
//            Set<String> permSet3 = new HashSet<>();
//            Set<String> permSet4 = new HashSet<>();
//            for (int i = 0; i < sysPermissionsLists.size(); i++) {
//                SysPermission perm = sysPermissionsLists.get(i);
//                /** 判断权限是否为空 */
//                if (perm == null || "".equals(perm.getPerms())) {
//                    continue;
//                }
//                if (sysPermissionsLists.get(i).getType().equals(TYPE1)){
//                    permSet1.add(perm.getPerms());
//                }else if (sysPermissionsLists.get(i).getType().equals(TYPE2)){
//                    permSet2.add(perm.getPerms());
//                }else if (sysPermissionsLists.get(i).getType().equals(TYPE3)){
//                    permSet3.add(perm.getPerms());
//                }else if (sysPermissionsLists.get(i).getType().equals(TYPE4)){
//                    permSet4.add(perm.getPerms());
//                }
//                permSet.add(perm.getPerms());
//            }
//            loginUser.setPermissions(permSet);
//            loginUser.setPermissions1(permSet1);
//            loginUser.setPermissions2(permSet2);
//            loginUser.setPermissions3(permSet3);
//            loginUser.setPermissions4(permSet4);
//        }
        Integer expireTime = tokenService.creatToken(loginUser);
        if (expireTime == 0) {
            throw new BaseException("登录失败，请稍后重试！");
        }

        /** 设置过期时间 */
        loginUser.setExpireTime(expireTime.longValue());
        return loginUser;
    }

    @Override
    public List<PfMenu> getPfMenuByOptId(Integer optId) {
        return userMapper.getPfMenuByOptId(optId);
    }

    @Override
    public Boolean logout(HttpServletRequest request) {
        String token = request.getHeader(CacheConstant.HEADER);
        if (StringUtils.isNotEmpty(token) && token.startsWith(CacheConstant.TOKEN_PREFIX)) {
            token = token.replace(CacheConstant.TOKEN_PREFIX, "");
        } else {
            throw new BaseException("身份验证失败");
        }
        tokenService.delLoginUser(token);
        return true;
    }

    @Override
    public Boolean refresh(HttpServletRequest request) {
        String token = request.getHeader(CacheConstant.HEADER);
        if (StringUtils.isNotEmpty(token) && token.startsWith(CacheConstant.TOKEN_PREFIX)) {
            token = token.replace(CacheConstant.TOKEN_PREFIX, "");
        } else {
            throw new BaseException("身份验证失败");
        }
        tokenService.refreshToken(token);
        return true;
    }
}
