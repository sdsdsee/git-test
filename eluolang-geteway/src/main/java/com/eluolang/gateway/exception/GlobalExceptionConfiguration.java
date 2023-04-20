package com.eluolang.gateway.exception;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author suziwei
 * @date 2020/9/24
 */
@Slf4j
@Order(-1)
@RequiredArgsConstructor
@Configuration
public class GlobalExceptionConfiguration implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper;

    public final static String TIME_OUT_EXCEPTION="class java.util.concurrent.TimeoutException";

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        // header set_json响应
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        //是否响应状态异常
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatus());
        }
        return response
                .writeWith(Mono.fromSupplier(() -> {
                    DataBufferFactory bufferFactory = response.bufferFactory();
                    try {

                        //返回json异常原因给前端
                        log.error(ex.getMessage());
                        JSONObject jsonObject=new JSONObject();
                        jsonObject.put("code","500");
                        String classStr= String.valueOf(ex.getClass());
                        if (TIME_OUT_EXCEPTION.equals(classStr))
                        {
                            jsonObject.put("message","连接超时，请稍后重试！");
                        }else {
                            jsonObject.put("message",ex.getMessage());
                        }
                        jsonObject.put("data",null);
                        return bufferFactory.wrap(objectMapper.writeValueAsBytes(jsonObject));
                    } catch (JsonProcessingException e) {
                        log.warn("Error writing response", ex);
                        return bufferFactory.wrap(new byte[0]);
                    }
                }));
    }
}
