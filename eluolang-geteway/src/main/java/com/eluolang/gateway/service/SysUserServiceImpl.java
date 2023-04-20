package com.eluolang.gateway.service;


import com.eluolang.common.core.constant.CacheConstant;
import com.eluolang.common.core.constant.Constants;
import com.eluolang.common.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZengXiaoQian
 * @createDate 2020-11-10
 */
@Service
public class SysUserServiceImpl implements SysUserService{

    @Resource
    private RedisService redisService;

    @Override
    public boolean isSameDeleteToken(String token) {
        List<String> currentDeleteKey = redisService.getCacheByPrefix(CacheConstant.CURRENT_DELETE_TOKEN + "*");
        if (currentDeleteKey.size()> Constants.RETURN_AFFECTED_ROWS){
            for (int i = 0; i < currentDeleteKey.size(); i++) {
                String currentDeleteValue = redisService.getCacheObject(currentDeleteKey.get(i));
                if (token.equals(currentDeleteValue)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isSameDisabledToken(String token) {
        List<String> currentDisabledKey = redisService.getCacheByPrefix(CacheConstant.CURRENT_DISABLED_TOKEN + "*");
        if (currentDisabledKey.size()>Constants.RETURN_AFFECTED_ROWS){
            for (int i = 0; i < currentDisabledKey.size(); i++) {
                String currentDisabledValue = redisService.getCacheObject(currentDisabledKey.get(i));
                if (token.equals(currentDisabledValue)){
                    return true;
                }
            }
        }
        return false;
    }
}
