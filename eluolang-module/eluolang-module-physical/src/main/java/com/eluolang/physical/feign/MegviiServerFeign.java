package com.eluolang.physical.feign;

import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.web.Result;
import com.eluolang.physical.feign.FeignFallBack.ELLSocketServerFeignFallBack;
import com.eluolang.physical.feign.FeignFallBack.MegviiServerFeignFallBack;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

@FeignClient(url = "http://192.168.3.211", name = "megviiUrl", fallback = MegviiServerFeignFallBack.class)
public interface MegviiServerFeign {
    @PostMapping(value = "/v1/MEGBOX/faces", produces = MediaType.MULTIPART_FORM_DATA_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public JSONObject ksImage(MultipartFile image) throws IOException;

    @PostMapping(value = "/v1/MEGBOX/faceGroups/binding/{faceToken}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JSONObject binding(Map<String,String[]> faceGroupList, @PathVariable("faceToken") String faceToken) throws IOException;
}
