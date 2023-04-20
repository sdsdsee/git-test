package com.eluolang.common.log.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.util.IdUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.eluolang.common.core.constant.CacheConstant;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.util.ClientIPUtils;
import com.eluolang.common.core.util.ServletUtils;
import com.eluolang.common.core.util.StringUtils;
import com.eluolang.common.core.web.Result;
import com.eluolang.common.log.annotation.Log;
import com.eluolang.common.log.constant.OperStatus;
import com.eluolang.common.log.constant.OperType;
import com.eluolang.common.log.service.AsyncLogService;
import com.eluolang.common.redis.service.RedisService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 操作日志记录处理
 *
 * @author ZengXiaoQian
 * @createDate 2020/9/14
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Resource
    private AsyncLogService asyncLogService;

    @Resource
    private RedisService redisService;

    /**
     * 配置织入点
     */
    @Pointcut("@annotation(com.eluolang.common.log.annotation.Log)")
    public void logPointCut() {
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
        try {
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }

            //获取保存之后的登录用户名
            HttpServletRequest request = ServletUtils.getRequest();
            // *========数据库操作日志=========* //
            OperLog operLog = new OperLog();
            operLog.setOperStatus(OperStatus.NORMAL.ordinal());
            // 请求的地址
            /*String ip = IPUtils.getIpAddr(ServletUtils.getRequest());*/
            String ip = ClientIPUtils.getInterIP2();
            operLog.setOperIp(ip);
            // 返回参数
            operLog.setJsonResult(JSON.toJSONString(jsonResult));
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(jsonResult));
            if (jsonObject != null) {
                Integer code = jsonObject.getInteger("code");
                if (code == HttpStatus.ERROR) {
                    operLog.setOperResult("操作失败");
                } else {
                    operLog.setOperResult("操作成功");
                }
            } else {
                operLog.setOperResult("操作异常");
            }


            String token = request.getHeader(CacheConstant.HEADER);
            if (StringUtils.isNotEmpty(token)) {
                token = token.substring(token.indexOf(" ") + 1);
                List<String> loginIdList = redisService.getCacheByPrefix(CacheConstant.LOGIN_ID_KEY + "*");
                for (int i = 0; i < loginIdList.size(); i++) {
                    String loginId = loginIdList.get(i);
                    LoginUser loginUser = redisService.getCacheObject(loginId);
                    if (loginUser.getToken().equals(token)) {
                        operLog.setOperName(loginUser.getOptName());
                        operLog.setOperRealName(loginUser.getNickname());
                    }
                }
            }

            if (e != null) {
                operLog.setOperStatus(OperStatus.ABNORMAL.ordinal());
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, operLog);
            //如果操作类型是登录则将登录的信息保存到操作日志表中
            if (operLog.getOperType() == OperType.LOGIN.ordinal()) {
                LoginBody loginBody = JSONObject.parseObject(operLog.getOperParam(), LoginBody.class);
                String userName = loginBody.getUsername();
                operLog.setOperName(userName);
                //根据登录账号查询操作员姓名
                Result result = asyncLogService.getRealNameByName(userName);
                //如果返回的data不为空,则登录操作的日志加入操作员姓名
                if (result.getData() != null) {
                    PfOperator pfOperator = JSON.parseObject(JSON.toJSONString(result.getData()), PfOperator.class);
                    operLog.setOperRealName(pfOperator.getNickname());
                }
            }
            //生成UUID
            String uuid = IdUtils.fastSimpleUUID();
            operLog.setId(uuid);
            // 保存数据库
            Result insertLogResult = asyncLogService.insertLog(operLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log     日志
     * @param operLog 操作日志
     * @throws Exception 抛异常
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, OperLog operLog) throws Exception {
        // 设置action动作
        operLog.setOperType(log.operType().ordinal());
        // 设置标题模块
        operLog.setOperModule(log.title());
        // 设置操作人类别
        operLog.setOperObj(log.operObj().ordinal());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operLog);
        }
        // 如果只是单纯针对软件的增删改查则设置操作内容
        operLog.setOperContent(log.content());
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, OperLog operLog) throws Exception {
        String requestMethod = operLog.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            operLog.setOperParam(StringUtils.substring(params, 0, params.length()));
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                if (!isFilterObject(paramsArray[i])) {
                    try {
                        Object jsonObj = JSON.toJSON(paramsArray[i]);
                        params += jsonObj.toString() + " ";
                    } catch (Exception e) {
                    }
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o) {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }
}
