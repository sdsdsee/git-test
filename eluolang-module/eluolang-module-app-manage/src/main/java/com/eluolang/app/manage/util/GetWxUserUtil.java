package com.eluolang.app.manage.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eluolang.app.manage.dto.WxToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

import static com.alibaba.fastjson.JSON.parseObject;

@Component
public class GetWxUserUtil {
    @Autowired
    private RestTemplate restTemplate;

    public JSONObject getWxUser(HttpServletRequest request) {
        //获取到code值
        String code = request.getParameter("code");
        //判断
        if (code == null) {
            throw new RuntimeException("用户禁止授权");
        }
        //获取到了code值，回调没有问题
        //定义地址
        String token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx2c920336a8fa970d&secret=ca31a2e38db888f63234d4aa560cdc1d&code="
                + code + "&grant_type=authorization_code";
        //发送请求
        String json = restTemplate.getForObject(token_url, String.class);
        WxToken token = parseObject(json, WxToken.class);
        //获取到接口调用凭证
        //获取个人信息
        String user_url = "https://api.weixin.qq.com/wxa/getpaidunionid?access_token=" + token.getAccess_token() + "&openid=" + token.getOpenid();
        String jsonStr1 = restTemplate.getForObject(user_url, String.class);
        JSONObject jsonStr = (JSONObject) JSON.parse(jsonStr1);

 /*       JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", openId);
        jsonObject.put("sex", sex);
        jsonObject.put("headimgurl", headimgurl);
        jsonObject.put("nickname", nickname);*/
        return jsonStr;
    }

}
