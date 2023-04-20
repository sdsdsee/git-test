package com.eluolang.common.core.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.models.*;
import com.aliyun.sdk.service.dysmsapi20170525.*;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;

//import javax.net.ssl.KeyManager;
//import javax.net.ssl.X509TrustManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SendSmsAliYun {
    public static void main(String[] args) throws Exception {


    }

    public static int codeCreate() {
        return (int)((Math.random()*9+1)*100000);
    }


    public static Map<String, String> sendMassage(String phone, int code) throws Exception {
        Map<String, String> stringStringMap = new HashMap<>();
        // HttpClient配置
        /*HttpClient httpClient = new ApacheAsyncHttpClientBuilder()
                .connectionTimeout(Duration.ofSeconds(10)) // 设置连接超时时间，默认为10秒
                .responseTimeout(Duration.ofSeconds(10)) // 设置响应超时时间，默认为20秒
                .maxConnections(128) // 设置连接池大小
                .maxIdleTimeOut(Duration.ofSeconds(50)) // 设置连接池超时时间，默认为30秒
                // 配置代理
                .proxy(new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress("<your-proxy-hostname>", 9001))
                        .setCredentials("<your-proxy-username>", "<your-proxy-password>"))
                //如果是https连接，您需要配置证书，或者忽略证书(.ignoreSSL(true))
                .x509TrustManagers(new X509TrustManager[]{})
                .keyManagers(new KeyManager[]{})
                .ignoreSSL(false)
                .build();*/

        // 配置证书认证信息，包括ak、secret、token
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId("LTAI5tDSpFAch6YhQsb2mxcP")
                .accessKeySecret("7IRUBeUpLZ7PgoudxpM6ItspVDWviL")
                //.securityToken("<your-token>") //使用STS token
                .build());
        // 匿名访问方法(需要API支持)
        // AnonymousCredentialProvider provider = AnonymousCredentialProvider.create();

        // 配置客户端
        AsyncClient client = AsyncClient.builder()
                .region("cn-qingdao") // Region ID
                //.httpClient(httpClient) //使用配置的httpClient，否则使用默认的httpClient(Apache httpClient)
                .credentialsProvider(provider)
                //serviceConfiguration(configuration .create()) //服务级配置
                // 重写客户端级配置，可以设置端点、Http请求参数等。
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                        //.setReadTimeout(Duration.ofSeconds(30))
                )
                .build();

        // API请求的参数设置
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName("易洛朗智慧体育")
                .templateCode("SMS_129870016")
                .phoneNumbers(phone)
                .templateParam("{\"code\":\"" + code + "\"}")
                // 请求级配置重写，可以设置Http请求参数等。
                // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                .build();

        // 异步获取API请求的返回值
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        // 同步获取API请求的返回值
        SendSmsResponse resp = response.get();
        //存储返回信息做判断
        stringStringMap.put("code", response.get().getBody().getCode());
        stringStringMap.put("massage", response.get().getBody().getMessage());
        System.out.println("*************************************************");
        System.out.println(new Gson().toJson(resp));
        // 返回值的异步处理
        /*response.thenAccept(resp -> {
            System.out.println(new Gson().toJson(resp));
        }).exceptionally(throwable -> { // Handling exceptions
            System.out.println(throwable.getMessage());
            return null;
        });*/

        // 最后，关闭客户端
        client.close();
        return stringStringMap;
    }
}