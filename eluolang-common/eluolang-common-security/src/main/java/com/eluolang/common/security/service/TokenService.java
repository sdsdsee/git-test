package com.eluolang.common.security.service;

import com.eluolang.common.core.constant.CacheConstant;
import com.eluolang.common.core.exception.BaseException;
import com.eluolang.common.core.pojo.LoginUser;
import com.eluolang.common.core.util.DateUtils;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.common.core.util.ServletUtils;
import com.eluolang.common.redis.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author suziwei
 * @date 2020/9/2
 */
@Component
public class TokenService {

    @Autowired
    private RedisService redisService;

    private final static String EXPIRE_TIME = "sessionTime";


    /**
     * 创建令牌
     */
    public Integer creatToken(LoginUser loginUser) {
        /** 远程调用获取会话失效时间 */
//        Result result=sysServicer.getByConfigAll();
//        List<Config> list= (List<Config>) result.getData();
        Integer expireTime = 0;
//        if (list.size()==0){
//            throw new BaseException("获取会话失效时间失败！");
//        }
//        for (int i=0;i<list.size();i++){
//            ObjectMapper objectMapper=new ObjectMapper();
//            Config config=objectMapper.convertValue(list.get(i),Config.class);
//            if (EXPIRE_TIME.equals(config.getName())){
        /** 转换为秒 */
        expireTime = 30 * 60;
//                break;
//            }
//        }
        Integer id;
        if (loginUser.getOptName() != null) {
            id = Integer.valueOf(loginUser.getId());
        }else {
            id = Integer.valueOf(loginUser.getId());;
        }
        /** 获得当前时间戳 */
        String date = DateUtils.date2TimeStamp(DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS), DateUtils.YYYY_MM_DD_HH_MM_SS);
        /** 设置登录时间 */
        loginUser.setLoginTime(Long.valueOf(date));
        /** 生成token */
        String token = IdUtils.fastUUID();
        /** 保存或更新用户token */
        loginUser.setToken(token);
        /** 设置过期时间 */
        loginUser.setExpireTime(Long.valueOf(expireTime));
        /** KEY：token VALUE:用户id */
        redisService.setCacheObject(CacheConstant.LOGIN_TOKEN_KEY + token, id, expireTime, TimeUnit.SECONDS);
        /** KEY：用户id VALUE:token 唯一*/
        redisService.setCacheObject(CacheConstant.LOGIN_ID_KEY + id, loginUser, expireTime, TimeUnit.SECONDS);
        return expireTime;
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser() {
        //获取用户信息
        return getLoginUser(ServletUtils.getRequest());
    }

    /**
     * 刷新令牌有效期
     *
     * @param token token
     */
    public long refreshToken(String token) {
        /** 远程调用获取会话失效时间 */
//        Result result=sysServicer.getByConfigAll();
//        List<Config> list= (List<Config>) result.getData();
        Integer expireTime = 0;
//        for (int i=0;i<list.size();i++){
//            ObjectMapper objectMapper=new ObjectMapper();
//            Config config=objectMapper.convertValue(list.get(i),Config.class);
//            if (EXPIRE_TIME.equals(config.getName())){
        /** 转换为秒 */
        expireTime = 30 * 60;
//                break;
//            }
//        }
        if (expireTime == 0) {
            throw new BaseException("获取会话失效时间失败！");
        }
        Integer userId = redisService.getCacheObject(CacheConstant.LOGIN_TOKEN_KEY + token);
        redisService.refresh(CacheConstant.LOGIN_TOKEN_KEY + token, expireTime, TimeUnit.SECONDS);
        redisService.refresh(CacheConstant.LOGIN_ID_KEY + userId, expireTime, TimeUnit.SECONDS);
        return expireTime;
    }

    private String getTokenKey(Integer id, String token) {
        if (id != null) {
            return CacheConstant.LOGIN_ID_KEY + id;
        }
        return CacheConstant.LOGIN_TOKEN_KEY + token;
    }


    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        /** 获取请求中token */
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            Integer userId = redisService.getCacheObject(CacheConstant.LOGIN_TOKEN_KEY + token);
            LoginUser user = redisService.getCacheObject(getTokenKey(userId, null));
            return user;
        }
        return null;
    }

    /**
     * 退出登录
     *
     * @param token 登录者token
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            Integer userId = redisService.getCacheObject(CacheConstant.LOGIN_TOKEN_KEY + token);
            redisService.deleteObject(getTokenKey(userId, null));
            redisService.deleteObject(getTokenKey(null, token));
        }
    }

    /**
     * 如果操作员删除后，账号已登录则删除token
     *
     * @param optId
     */
    public void delLoginPfOperator(Integer optId) {
        LoginUser user = redisService.getCacheObject(getTokenKey(optId, null));
        if (user != null) {
            Integer userId = redisService.getCacheObject(CacheConstant.LOGIN_TOKEN_KEY + user.getToken());
            redisService.deleteObject(getTokenKey(userId, null));
            redisService.deleteObject(getTokenKey(null, user.getToken()));
        }
    }

    /**
     * 获取请求token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(CacheConstant.HEADER);
        if (StringUtils.isNotEmpty(token) && token.startsWith(CacheConstant.TOKEN_PREFIX)) {
            token = token.replace(CacheConstant.TOKEN_PREFIX, "");
        }
        return token;
    }
}
