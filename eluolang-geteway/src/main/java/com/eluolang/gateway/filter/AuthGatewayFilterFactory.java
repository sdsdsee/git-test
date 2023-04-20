package com.eluolang.gateway.filter;

import com.eluolang.common.core.constant.CacheConstant;
import com.eluolang.common.core.util.StringUtils;
import com.eluolang.common.redis.service.RedisService;
import com.eluolang.gateway.service.AuthService;
import com.eluolang.gateway.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * token验证
 *
 * @author suziwei
 * @date 2020/9/24
 */
@Slf4j
@Component
public class AuthGatewayFilterFactory implements GlobalFilter, Ordered {

    /**
     * 排除过滤的 uri 地址
     */
    private static final String[] WHITE_LIST = {"/auth/login", "/auth/logout", "/user/sysUser/manage/getCode", "/user/sysUser/manage/verifyCode",
            "/user/sysUser/manage/disRememberModifyPwd", "/physical/examinerLoginDevice", "/dev/device/selDeviceRegister", "/dev/device/selectStartPlan",
            "/dev/device/selectPhysicalTest", "/dev/device/selectLoopPhysicalTest", "/physical/studentsRegister", "/physical/studentsFaceRegister",
            "/physical/submitScores", "/physical/submitBatchScores", "/dev/device/uploadBodyScreenshot", "/socket/webSocket/index", "/appUser/app/loginAppUser", "/dev/device/imageGather", "/dev/device/uploadTestingVideo",
            "/dev/device/startTranscribe", "/dev/device/stopTranscribe", "/physical/scoreList", "/physical/currentTime", "/physical/getOneScore","/dev/device/selVersionUrl"};

    private static final String LOGIN = "/auth/login";

    static final String APP = "appUser";

    static final String ROLE = "role";

    @Resource
    private AuthService authService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private RedisService redisService;

    /**
     * 全局拦截器
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        /** 声明返回值类型为JSON */
        HttpHeaders httpHeaders = exchange.getResponse().getHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        /** 判断是否为小程序用户 */
        if (APP.equals(exchange.getRequest().getHeaders().getFirst(ROLE))) {
            return chain.filter(exchange).then(Mono.defer(() -> {
                exchange.getResponse().getHeaders().entrySet().stream()
                        .filter(kv -> (kv.getValue() != null && kv.getValue().size() > 1))
                        .filter(kv -> (kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)
                                || kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS)))
                        .forEach(kv ->
                        {
                            kv.setValue(new ArrayList<String>() {{
                                add(kv.getValue().get(0));
                            }});
                        });
                return chain.filter(exchange);
            }));
        }
//        Boolean bool = null;
//        bool = redisService.getCacheObject("licenseStatus");
//        if (StringUtils.isNull(bool) || bool == false) {
//            bool = (Boolean) licenseService.verifyLicense().getData();
//            redisService.setCacheObject("licenseStatus",bool, 1,TimeUnit.DAYS);
//        }

        /** 判断是否为登录请求 **/
        RequestPath path = exchange.getRequest().getPath();
        if (String.valueOf(path).contains(LOGIN)) {
            return chain.filter(exchange);
        }
//        /** 判断license */
//        if (!bool){
//            return Mono.defer(() -> {
//                /** 设置status */
//                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                final ServerHttpResponse response = exchange.getResponse();
//                /** 设置返回参数 */
//                byte[] bytes = "{\"code\":\"409\",\"message\":\"license未激活！\",\"data\":\"null\"}".getBytes(StandardCharsets.UTF_8);
//                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
//                /** 设备body */
//                return response.writeWith(Flux.just(buffer));
//            });
//        }
        /** 判断license */
//        if (!bool){
//            return Mono.defer(() -> {
//                /** 设置status */
//                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                final ServerHttpResponse response = exchange.getResponse();
//                /** 设置返回参数 */
//                byte[] bytes = "{\"code\":\"409\",\"message\":\"license未激活！\",\"data\":\"null\"}".getBytes(StandardCharsets.UTF_8);
//                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
//                /** 设备body */
//                return response.writeWith(Flux.just(buffer));
//            });
//        }

        /** 跳过不需要验证的路径 **/
        String url = exchange.getRequest().getURI().getPath();
        if (Arrays.asList(WHITE_LIST).contains(url)) {
            return chain.filter(exchange);
        }

        /** 获取请求头中token */
        String token = exchange.getRequest().getHeaders().getFirst(CacheConstant.HEADER);
        /** 判断是否登录 */
        if (StringUtils.isEmpty(token)) {
            return Mono.defer(() -> {
                /** 设置status */
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                final ServerHttpResponse response = exchange.getResponse();
                /** 设置返回参数 */
                byte[] bytes = "{\"code\":\"403\",\"message\":\"非法访问,请先登录！\",\"data\":\"null\"}".getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                /** 设备body */
                return response.writeWith(Flux.just(buffer));
            });
        }

        token = token.replace(CacheConstant.TOKEN_PREFIX, "");
        /** 判断用户是否登录 */
        if (authService.isLogin(token)) {
            return Mono.defer(() -> {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                final ServerHttpResponse response = exchange.getResponse();
                byte[] bytes = "{\"code\":\"403\",\"message\":\"登录超时,请重新登录！\"}".getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                return response.writeWith(Flux.just(buffer));
            });
        }
        /** 判断是否为被挤账号 */
        if (authService.isSqueezed(token)) {
            return Mono.defer(() -> {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                final ServerHttpResponse response = exchange.getResponse();
                byte[] bytes = "{\"code\":\"403\",\"message\":\"该账号已在其他地方登录，请联系管理员修改密码！\",\"data\":\"null\"}".getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                return response.writeWith(Flux.just(buffer));
            });
        }

        //判断删除的用户的token是否和redis中的相同
        if (sysUserService.isSameDeleteToken(token)) {
            return Mono.defer(() -> {
                /** 设置status */
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                final ServerHttpResponse response = exchange.getResponse();
                /** 设置返回参数 */
                byte[] bytes = "{\"code\":\"403\",\"message\":\"您已被删除！\",\"data\":\"null\"}".getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                /** 设备body */
                return response.writeWith(Flux.just(buffer));
            });
        }

        //判断禁用的用户的token是否和redis中的相同
        if (sysUserService.isSameDisabledToken(token)) {
            return Mono.defer(() -> {
                /** 设置status */
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                final ServerHttpResponse response = exchange.getResponse();
                /** 设置返回参数 */
                byte[] bytes = "{\"code\":\"403\",\"message\":\"您已被禁用！\",\"data\":\"null\"}".getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                /** 设备body */
                return response.writeWith(Flux.just(buffer));
            });
        }

        return chain.filter(exchange).then(Mono.defer(() -> {
            exchange.getResponse().getHeaders().entrySet().stream()
                    .filter(kv -> (kv.getValue() != null && kv.getValue().size() > 1))
                    .filter(kv -> (kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)
                            || kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS)))
                    .forEach(kv ->
                    {
                        kv.setValue(new ArrayList<String>() {{
                            add(kv.getValue().get(0));
                        }});
                    });
            return chain.filter(exchange);
        }));
    }

    /**
     * 控制拦截器优先级，越小优先级越高
     *
     * @return
     */
    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER + 1;
    }
}