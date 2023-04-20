package com.eluolang.common.security.aspect;

import com.eluolang.common.core.exception.BaseException;
import com.eluolang.common.core.util.ServletUtils;
import com.eluolang.common.security.annotation.PreAuthorize;
import com.eluolang.common.security.service.TokenService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * PreAuthorize注解逻辑判断
 *
 * @author suziwei
 * @date 2020/9/4
 */
@Aspect
@Component
public class PreAuthorizeAspect
{
    static final String ADMIN="admin";

    static final String APP="appUser";

    @Autowired
    private TokenService tokenService;

    @Around("@annotation(com.eluolang.common.security.annotation.PreAuthorize)")
    public Object around(ProceedingJoinPoint point) throws Throwable
    {
        if(true){
            return point.proceed();
        }
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        PreAuthorize annotation = method.getAnnotation(PreAuthorize.class);
        /** 获取请求头 */
        HttpServletRequest request= ServletUtils.getRequest();
        /** 判断是否是非系统用户（小程序） */
        if (APP.equals(request.getHeader("role")))
        {
            /** 执行目标方法 */
            return point.proceed();
        }

        /** 判断方法是否需要权限访问 */
        if (annotation == null)
        {
            return point.proceed();
        }
//        /** 验证用户是否具备访问权限 */
//        if (!(StringUtils.isEmpty(annotation.hasPermi())) && hasPermi(annotation.hasPermi()))
//        {
//            return point.proceed();
//        }
        else
        {
            /** 该用户没有权限访问，抛出异常 */
            throw new BaseException("权限异常");
        }
    }
//
//    /**
//     * 验证用户是否具备某权限
//     *
//     * @param permission 权限字符串
//     * @return 用户是否具备某权限
//     */
//    public boolean hasPermi(String permission)
//    {
//        LoginUser userInfo = tokenService.getLoginUser();
//        if (StringUtils.isEmpty(userInfo) || CollectionUtils.isEmpty(userInfo.getPermissions()))
//        {
//            return false;
//        }
//        return hasPermissions(userInfo.getPermissions(), permission);
//    }
//
//
//    /**
//     * 判断是否包含权限
//     *
//     * @param authorities 权限列表
//     * @param permission 权限字符串
//     * @return 用户是否具备某权限
//     */
//    private boolean hasPermissions(Collection<String> authorities, String permission)
//    {
//        return authorities.contains(permission);
//    }

}
