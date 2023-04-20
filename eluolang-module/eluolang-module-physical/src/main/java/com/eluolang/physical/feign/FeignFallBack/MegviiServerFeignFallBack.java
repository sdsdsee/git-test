package com.eluolang.physical.feign.FeignFallBack;

import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.web.Result;
import com.eluolang.physical.feign.ELLSocketServerFeign;
import com.eluolang.physical.feign.MegviiServerFeign;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MegviiServerFeignFallBack implements MegviiServerFeign {


    @Override
    public JSONObject ksImage(MultipartFile image) throws IOException {
        return null;
    }

    @Override
    public JSONObject binding(Map<String,String[]> faceGroupList, String faceToken) throws IOException {
        return null;
    }
}
