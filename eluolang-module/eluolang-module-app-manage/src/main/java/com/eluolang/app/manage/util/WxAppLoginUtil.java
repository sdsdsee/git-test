package com.eluolang.app.manage.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eluolang.app.manage.dto.WxToken;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

import static com.alibaba.fastjson.JSON.parseObject;

public class WxAppLoginUtil {
    public static JSONObject getWxUser(RestTemplate restTemplate, String code, String appId, String secret) {
        //判断
        if (code == null) {
            throw new RuntimeException("用户禁止授权");
        }
        //获取到了code值，回调没有问题
        //定义地址
        String token_url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        //发送请求
        String json = restTemplate.getForObject(token_url, String.class);
        WxToken token = parseObject(json, WxToken.class);
        //微信小程序没有
        //获取到接口调用凭证
        //获取个人信息
        //String user_url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + token.getAccess_token() + "&openid=" + token.getOpenid();
        //String jsonStr1 = restTemplate.getForObject(user_url, String.class);
        JSONObject jsonStr = (JSONObject) JSON.parse(json);
        //判断是否获取成功，成功就没有errcode,然后在使用的地方判断json是否为空为空就没获取到
    /*    if (jsonStr.containsKey("errcode")) {
            return null;
        }*/
 /*       JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", openId);
        jsonObject.put("sex", sex);
        jsonObject.put("headimgurl", headimgurl);
        jsonObject.put("nickname", nickname);*/
        return jsonStr;
    }
}
