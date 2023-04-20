package com.eluolang.module.sys.user.service.impl;

import com.eluolang.common.core.exception.BaseException;
import com.eluolang.common.core.hardware.vo.PfDepartVo;
import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.util.*;
import com.eluolang.common.message.MailUtil;
import com.eluolang.common.redis.service.RedisService;
import com.eluolang.module.sys.user.constant.SysUserConstant;
import com.eluolang.module.sys.user.dto.*;
import com.eluolang.module.sys.user.mapper.SysUserMapper;
import com.eluolang.module.sys.user.model.PfOperatorVo;
import com.eluolang.module.sys.user.service.SysUserService;
import com.eluolang.module.sys.user.service.feign.UserRemoteService;
import com.eluolang.module.sys.user.util.EmailCodeUtils;
import com.eluolang.module.sys.user.vo.DepartVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户管理逻辑实现层
 *
 * @author ZengXiaoQian
 * @createDate 2020/8/25
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private UserRemoteService userRemoteService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private RedisService redisService;

    @Value("${email.account}")
    private String emailAccount;

    @Value("${email.password}")
    private String emailPassword;

    @Value("${email.host}")
    private String emailHost;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int getCode(String name, String email){
            //初始化验证用户名和邮箱的返回参数
            int result=0;
            try{
                //验证用户名和邮箱是否正确
                result= sysUserMapper.getCode(name, email);
                //用户名和邮箱正确
                if (result > SysUserConstant.RETURN_AFFECTED_ROWS){
                    //得到6位随机验证码
                    String code= EmailCodeUtils.getNumber();
                    //调用邮箱模块
                    MailUtil mailUtil=new MailUtil(emailHost,emailAccount,emailPassword);
                    //发送验证码到邮箱
                    mailUtil.sendMail(email,"忘记密码-重置密码",code);
                    //从数据库读取邮箱验证码的有效期
                    String emailValidateTime = sysUserMapper.emailValidateTime();
                    //发送验证码成功，放入redis中
                    redisService.setCacheObject(email, code, Integer.valueOf(emailValidateTime), TimeUnit.MINUTES);
                    return result;
                }else{
                    return result;
                }
            }catch (Exception e){
                //打印错误日志
                LogUtils.printLog(e);
                //抛出友好提示给前端
                throw new BaseException("邮箱或者redis服务异常");
            }finally {
                return result;
            }
    }

    @Override
    public String emailValidateTime() {
        return sysUserMapper.emailValidateTime();
    }

    @Override
    public int modifyPwd(String name, String newPwd) {
        //初始化修改密码的返回参数
        int modifyPwd=0;
        try{
            //对用户修改之后的新密码进行加密
            String saltPwd= MD5Uitls.MD5Lower(newPwd,name);
            //调用修改密码的接口将加密后的密码保存到数据库
            modifyPwd=sysUserMapper.modifyPwd(name, saltPwd);
        }catch (Exception e){
            //打印错误日志
            LogUtils.printLog(e);
            //抛出友好提示给前端
            throw new BaseException("MD5加密异常");
        }finally {
            return modifyPwd;
        }
    }

    @Override
    public PfOperator getRealNameByName(String name) {
        return sysUserMapper.getRealNameByName(name);
    }


    @Override
    public int selectUserPwd(String oldPwd, String name) {
        //判断用户输入的旧密码和数据库加密的密码是否一致
        oldPwd = MD5Uitls.MD5Lower(oldPwd, name);
        return sysUserMapper.selectUserPwd(oldPwd, name);
    }

    @Override
    public List<PfOperator> selectUser(String path) {
        return sysUserMapper.selectUser(path);
    }


    @Override
    public PfOperatorVo getPersonalInfo(Integer id) {
        return sysUserMapper.getPersonalInfo(id);
    }

    @Override
    public int modifyPersonalInfo(SysUserDto sysUserDto) {
        return sysUserMapper.modifyPersonalInfo(sysUserDto);
    }


    @Override
    public Integer modifyBgmUrl(ModifyUserDto modifyUserDto) {
        return sysUserMapper.modifyBgmUrl(modifyUserDto.getId(),modifyUserDto.getBackgroundUrl(),modifyUserDto.getPartBackgroundUrl());
    }
//
//    @Override
//    public int resetUserPwd(String pwd,Integer id) {
//        return sysUserMapper.resetUserPwd(pwd,id);
//    }
//
//    @Override
//    public List<SysUserVo> getUserLogout() {
//        return sysUserMapper.getUserLogout();
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public int modifyUserState(Integer state,Integer id) {
//        log.info("修改用户状态的用户状态为"+state+",用户id为"+id);
//        //初始化返回结果
//        int returnModifyState=0;
//        //调用修改用户状态的方法
//        int modifyUserState = sysUserMapper.modifyUserState(state, id);
//        //如果状态改为已禁用且修改用户状态成功
//        if (state==SysUserConstant.DISABLED_USER && modifyUserState > SysUserConstant.RETURN_AFFECTED_ROWS) {
//            //从redis获取到所有的key（login_id）
//            List<String> loginIdList = redisService.getCacheByPrefix(CacheConstant.LOGIN_ID_KEY + "*");
//            for (int j = 0; j < loginIdList.size(); j++) {
//                String loginId = loginIdList.get(j);
//                LoginUser loginUser = redisService.getCacheObject(loginId);
//                //根据login_id获取到现有的token
//                LoginUser currentLoginUser = redisService.getCacheObject(CacheConstant.LOGIN_ID_KEY + id);
//                if (StringUtils.isNotNull(currentLoginUser)){
//                    //如果token和所有的token一对比 有相同的值 就将token值保存到数据库
//                    if (loginUser.getToken().equals(currentLoginUser.getToken())){
//                        redisService.setCacheObject(CacheConstant.CURRENT_DISABLED_TOKEN+id,currentLoginUser.getToken());
//                        continue;
//                    }
//                }
//            }
//            returnModifyState=SysUserConstant.MODIFY_USER_SUCCESS;
//        }else if(state==SysUserConstant.AVAILABLE_USER && modifyUserState > SysUserConstant.RETURN_AFFECTED_ROWS){
//            returnModifyState=SysUserConstant.MODIFY_USER_SUCCESS;
//        }
//        else{
//            log.error("修改用户id为"+id+"的状态失败");
//            throw new BaseException("修改用户状态失败！");
//        }
//        return returnModifyState;
//    }

    @Override
    public List<PfMenu> selPfMenuById(Integer id) {
        return sysUserMapper.selPfMenuById(id);
    }

    @Override
    public List<PfDepart> selPfDepartById(Integer id) {
        return sysUserMapper.selPfDepartById(id);
    }

    @Override
    public int addPfOperator(PfOperatorDto pfOperatorDto) {
        return sysUserMapper.addPfOperator(pfOperatorDto);
    }

    @Override
    public int pfOperatorIsExist(String optName) {
        return sysUserMapper.pfOperatorIsExist(optName);
    }

    @Override
    public int addPfOperatorRat(List<PfOperatorRat> pfOperatorRatList) {
        return sysUserMapper.addPfOperatorRat(pfOperatorRatList);
    }

    @Override
    public List<DepartVo> selDepartByOptId(Integer optId) {
        return sysUserMapper.selDepartByOptId(optId);
    }

    @Override
    public int updatePfOperator(PfOperatorDto pfOperatorDto) {
        return sysUserMapper.updatePfOperator(pfOperatorDto);
    }

    @Override
    public int delPfOperatorRat(Integer optId) {
        return sysUserMapper.delPfOperatorRat(optId);
    }

    @Override
    public int delPfOperator(Integer optId) {
        return sysUserMapper.delPfOperator(optId);
    }

    @Override
    public int selPfOperatorByParentId(Integer optId) {
        return sysUserMapper.selPfOperatorByParentId(optId);
    }

    @Override
    public List<PfOperator> selPfOperatorByPath(String path) {
        return sysUserMapper.selPfOperatorByPath(path);
    }

    @Override
    public List<PfOperator> selPfOperatorByPhone(String phone) {
        return sysUserMapper.selPfOperatorByPhone(phone);
    }

    @Override
    public List<SysDataDictionary> selSysDataDictionaryByType(Integer type) {
        return sysUserMapper.selSysDataDictionaryByType(type);
    }

    @Override
    public int addSysDataDictionary(SysDataDictionary sysDataDictionary) {
        return sysUserMapper.addSysDataDictionary(sysDataDictionary);
    }

    @Override
    public int selSysDataDictionaryIsExist(SysDataDictionary sysDataDictionary) {
        return sysUserMapper.selSysDataDictionaryIsExist(sysDataDictionary);
    }

    @Override
    public int updateSysDataDictionary(SysDataDictionary sysDataDictionary) {
        return sysUserMapper.updateSysDataDictionary(sysDataDictionary);
    }

    @Override
    public int delSysDataDictionary(Integer id) {
        return sysUserMapper.delSysDataDictionary(id);
    }

    @Override
    public Integer saveFileInfo(FileMgr fileMgr) {
        return sysUserMapper.saveFileInfo(fileMgr);
    }

    @Override
    public FileMgr getFileInfo(String fileId) {
        return sysUserMapper.getFileInfo(fileId);
    }

}
