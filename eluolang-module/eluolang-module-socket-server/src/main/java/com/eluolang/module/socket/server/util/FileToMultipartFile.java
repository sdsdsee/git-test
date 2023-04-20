package com.eluolang.module.socket.server.util;

import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.util.FileUtils;
import com.google.gson.JsonObject;
import org.apache.http.entity.ContentType;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class FileToMultipartFile {
    //File文件转为MultipartFile文件(注意：未判空，请自己添加)
    public static   MultipartFile getMultipartFile(File file){
        FileInputStream fileInputStream = null;
        MultipartFile multipartFile = null;
        try {
            fileInputStream = new FileInputStream(file);
            multipartFile = new MockMultipartFile("image",file.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(),fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multipartFile;
    }
    public static void main(String[] args){
//        Map<String,Object> faceGroupList = new HashMap<>();
//        File file = new File("E:\\Capture\\333.jpeg");
//        File images = file;
//        MultipartFile image = FileToMultipartFile.getMultipartFile(images);

//        faceGroupList.put("image",image);
//        faceGroupList.put("faceGroupList",aa);
        String url = "http://192.168.3.211/v1/MEGBOX/search";//定义一个url
        /**
         设置请求头为multipart/form-data
         */
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);
        //创建一个参数map，添加参数
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        FileSystemResource resource = new FileSystemResource(new File("E:\\Capture\\333.jpeg"));
        String[] aa = {"test"};
        params.add("image", resource);
        params.add("group",aa);
        //创建restTemplate
        RestTemplate restTemplate = new RestTemplate();

        Object object = restTemplate.postForObject(url, params, Object.class);
        JSONObject jsonObject = ((JSONObject) JSONObject.toJSON(object));
        JSONObject data = (JSONObject) JSONObject.toJSON(jsonObject.get("data"));
        JSONObject detectionResult = (JSONObject) JSONObject.toJSON(data.get("top1"));
        System.out.println(jsonObject);
    }
}
