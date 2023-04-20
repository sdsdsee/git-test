package com.eluolang.module.socket.server.util;

import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.web.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dengrunsen
 * @date 2023年02月07日 11:14
 */
public class MegviiApiUtil {
    private static Logger log =  LoggerFactory.getLogger(MegviiApiUtil.class.getName());
    private static String ip = "http://192.168.3.211";
    public static Result faceToken(String faceToken){
        Result result = null;
        try{
            RestTemplate restTemplate = new RestTemplate();
            //get请求
            //方法一：getForEntity(String url, Class<T> responseType, Object... uriVariables),没有参数
            String url = ip+"/v1/MEGBOX/faces/faceToken/{faceToken}";
            ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class, faceToken);
            result = JSONObject.parseObject(forEntity.getBody(),Result.class);
        }catch (HttpClientErrorException e){
            log.error("调用失败"+e);
        }
        return result;
    }

    public static Result captures(String trackId,long timestamp){
        Result result = null;
        try{
            RestTemplate restTemplate = new RestTemplate();
            Map map = new HashMap<>();
            map.put("trackId", trackId);
            map.put("catchTimestamp", timestamp);
            //get请求
            //方法一：getForEntity(String url, Class<T> responseType, Object... uriVariables),没有参数
            String url = ip+"/v1/MEGBOX/captures/{trackId}/{catchTimestamp}";
            ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class, map);
            result = JSONObject.parseObject(forEntity.getBody(),Result.class);
        }catch (HttpClientErrorException e){
            log.error("调用失败"+e);
        }
        return result;
    }

    public static Object search(String filePath){
        Object object = null;
        try{
            /**
              * 设置请求头为multipart/form-data
              */
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("multipart/form-data");
            headers.setContentType(type);
            //创建一个参数map，添加参数
            MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            FileSystemResource resource = new FileSystemResource(new File(filePath));
            String[] aa = {"test"};
            params.add("image", resource);
            params.add("group", aa);
//            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//            requestFactory.setConnectTimeout(10*1000);
//            requestFactory.setReadTimeout(10*1000);
            //创建restTemplate
            RestTemplate restTemplate = new RestTemplate();

            object = restTemplate.postForObject(ip+"/v1/MEGBOX/search", params, Object.class);
        }catch (HttpClientErrorException e){
            log.error("调用失败"+e);
        }
        return object;
    }

    public static void main(String[] agrs){
        Object result = search("E:\\Capture\\7788.jpg");
        System.out.println(result);
    }
}
