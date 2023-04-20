package com.eluolang.platform.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.web.Result;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;


/**
 * Auto Create on 2022-06-10 17:03:46
 */
public class ArtemisPostTest {
    /**
     * STEP1：设置平台参数，根据实际情况,设置host appkey appsecret 三个参数.
     * 合作方Key：28033520
     * 合作方Secret：yI3USmFQntv0yjHXODKY
     * 内网地址：172.30.0.5
     * 互联网地址 112.44.248.179 : 8001或者端口号30443
     */
    static {
        ArtemisConfig.host = "112.44.248.179:30443";// 平台门户/nginx的IP和端口（必须使用https协议，https端口默认为443）
        ArtemisConfig.appKey = "28033520"; // 秘钥appkey
        ArtemisConfig.appSecret = "yI3USmFQntv0yjHXODKY";// 秘钥appSecret
    }

    /**
     * STEP2：设置OpenAPI接口的上下文
     */
    private static final String ARTEMIS_PATH = "/artemis";

    //查询停车库剩余车位数
    public static String remainSpaceNum(RemainSpaceNumRequest remainSpaceNumRequest) {
        String remainSpaceNumDataApi = ARTEMIS_PATH + "/api/pms/v1/park/remainSpaceNum";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", remainSpaceNumDataApi);
            }
        };
        String body = JSON.toJSONString(remainSpaceNumRequest);
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json");
        return result;
    }

    //查询车库信息
    public static String remainGarageNum(ParkListRequest parkListRequest) {
        String remainSpaceNumDataApi = ARTEMIS_PATH + "/api/resource/v1/park/parkList";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", remainSpaceNumDataApi);
            }
        };
        String body = JSON.toJSONString(parkListRequest);
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json");
        return result;
    }

    public static void main(String[] args) {
/*        ParkListRequest parkListRequest = new ParkListRequest();
        parkListRequest.setParkIndexCodes("");
        String A = remainGarageNum(parkListRequest);
        Result result = JSONObject.parseObject(A, Result.class);*/
        RemainSpaceNumRequest remainSpaceNumRequest=new RemainSpaceNumRequest();
        remainSpaceNumRequest.setParkSyscode("");
        System.out.println(remainSpaceNum(remainSpaceNumRequest));
    }
}
