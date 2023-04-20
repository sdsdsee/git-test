package com.eluolang.physical.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dengrunsen
 * @date 2022年12月06日 16:29
 */
public class FeignUtil {
    private static Logger log =  LoggerFactory.getLogger(FeignUtil.class.getName());
    /**
     * 调用api post接口
     *
     * @param url
     * @param jsonObject
     * @return java.lang.String
     * @author li.zhm
     * @date 2022/8/20 13:45
     * @history <author>  <time>  <version>  <desc>
     */
    public static String sendPost(String url, Map<String,String[]> jsonObject) {
        log.info("请求post地址：{}", url);
        String body = "";
        // 创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();

        CloseableHttpResponse response = null;
        // 创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig =
                RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
        httpPost.setConfig(requestConfig);
        // 装填参数
        StringEntity s = new StringEntity(JSONObject.toJSONString(jsonObject), "utf-8");
        s.setContentEncoding(new BasicHeader("Content_Type", "application/json"));
        // 设置参数到请求对象中
        httpPost.setEntity(s);
        log.info("请求参数：{}", jsonObject.toString());

        // 设置header信息, 指定报文头【Content-type】 【User-Agent】
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        try {
            // 执行请求操作，并拿到结果（同步阻塞）
            response = client.execute(httpPost);
            log.info("response 返回状态码：{}", response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK || response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
                // 获取结果实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // 按指定编码转换结果实体为String类型
                    body = EntityUtils.toString(entity, "utf-8");
                    log.info("响应body:", body);
                }
                // 关闭流
                EntityUtils.consume(entity);
            } else {
                log.error("response 返回状态码异常：{}", response.getStatusLine().getStatusCode());
            }
            // 释放链接
            response.close();
        } catch (IOException e) {
            log.error("调用amf-sub api异常，{0}", e);
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    log.error("关闭http client异常，{0}", e);
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("关闭http response异常，{0}", e);
                    e.printStackTrace();
                }
            }
        }
        return String.valueOf(response.getStatusLine().getStatusCode());
    }
    public static void main(String[] args) throws Exception {
        Map<String,Object> faceGroupList = new HashMap<>();
        File file = new File("E:\\Capture\\333.jpeg");
        File images = file;
        MultipartFile image = FileToMultipartFile.getMultipartFile(images);
        String[] aa = {"test"};
        faceGroupList.put("image",image);
        faceGroupList.put("faceGroupList",aa);
    }
}
