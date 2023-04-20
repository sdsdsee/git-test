package com.eluolang.app.manage.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.eluolang.app.manage.dto.EllUserVo;
import com.eluolang.app.manage.dto.LoginAppVo;
import com.eluolang.app.manage.dto.SignDto;
import com.eluolang.app.manage.dto.WxToken;
import com.eluolang.app.manage.service.AppUserService;
import com.eluolang.app.manage.util.*;
import com.eluolang.app.manage.vo.AppointmentPlanVo;
import com.eluolang.app.manage.vo.EllOrderPlanVo;
import com.eluolang.app.manage.vo.EllSubscribeNumVo;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.pojo.EllPlanUser;
import com.eluolang.common.core.pojo.EllUser;
import com.eluolang.common.core.pojo.PfDepart;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.common.core.util.SignUtil;
import com.eluolang.common.core.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.logstash.logback.encoder.org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.*;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * 小程序的控制器层
 *
 * @author dengrunsen
 */
@Api(tags = "小程序控制器")
@RestController
@RequestMapping("/app")
@Transactional
public class AppUserController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AppUserService appUserService;
    @Value("${RSA.private}")//私钥
    private String RsaPrivate;
    @Value("${appId.TzAppId}")
    private String TzAppId;
    @Value("${appId.TzAppSecret}")
    private String TzAppSecret;
    @Value("${hy.key}")
    private String key;
    @Value("${hy.iv}")
    private String iv;
    @Value("${hy.vendorId}")
    private String vendorId;
    @Value("${hy.password}")
    private String password;

    @ApiOperation("用户登录")
    @PostMapping("/loginAppUser")
    public Result userLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginAppVo loginApp) throws Exception {
//        request.setAttribute("code", loginApp.getCode());
        String[] appId;
        String[] secret;
        //判断是否有多个小程序
        if (TzAppId.contains(",")) {
            appId = TzAppId.split(",");
            secret = TzAppSecret.split(",");
        } else {
            appId = new String[1];
            appId[0] = TzAppId;
            secret = new String[1];
            secret[0] = TzAppSecret;
        }
        //用户唯一标识
        String openId = null;
        JSONObject jsonStr = null;
        for (int i = 0; i < appId.length; i++) {
            jsonStr = getWxUser(loginApp.getCode(), appId[i], secret[i]);
            openId = (String) jsonStr.get("openid");
            if (openId != null || openId != "") {
                break;
            }
        }
        JSONObject jsonObject = new JSONObject();
        //查询此微信是否绑定用户信息
        EllUserVo ellUser = appUserService.findUser(openId);
        if (ellUser != null) {
            jsonObject.put("ellUser", ellUser);
            return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
        }
        jsonObject.put("openId", openId);
        //小程序是在前端获取
        //用户名
        //String nickname = (String) jsonStr.get("nickname");
        //用户头像地址
        //String headimgurl = (String) jsonStr.get("headimgurl");
        //int sexNumber = (int) jsonStr.get("sex");
        return new Result(HttpStatus.NOT_FOUND, "未进行绑定!", jsonObject);
    }

    @ApiOperation("微信openId查询学生")
    @GetMapping("/findUserByOpenId")
    public Result findUserByOpenId(String wxOpenId) {
        EllUserVo userVo = appUserService.findUser(wxOpenId);
        //返回解密的身份证进行二维码生成
        //userVo.setIdCard(RSAUtils.decode(userVo.getIdCard(), RsaPrivate));
        return new Result(HttpStatus.SUCCESS, "查询成功", userVo);
    }

    @ApiOperation("获取电话号码")
    @PostMapping("/getPhone")
    public Result getPhone(@RequestBody LoginAppVo loginApp) throws Exception {
        String[] appId;
        String[] secret;
        //判断是否有多个小程序
        if (TzAppId.contains(",")) {
            appId = TzAppId.split(",");
            secret = TzAppSecret.split(",");
        } else {
            appId = new String[1];
            appId[0] = TzAppId;
            secret = new String[1];
            secret[0] = TzAppSecret;
        }
        //用户唯一标识
        String openId = null;
        JSONObject jsonStr = null;
        for (int i = 0; i < appId.length; i++) {
            jsonStr = getWxUser(loginApp.getCode(), appId[i], secret[i]);
            openId = (String) jsonStr.get("openid");
            if (openId != null || openId != "") {
                break;
            }
        }
        JSONObject jsonObject = new JSONObject();
        //用湖信息解码的偏移量
        JSONObject jsonStrs = JSONObject.parseObject(loginApp.getStringJson());
        String iv = (String) jsonStrs.get("iv");
        String encryptedData = (String) jsonStrs.get("encryptedData");
        //解密获取的用户信息
        AESUtils aes = new AESUtils();
        Base64.Decoder decoder = Base64.getDecoder();
//        encryptedData=encryptedData.replaceAll("\n","");
//        encryptedData= StringEscapeUtils.unescapeJava(encryptedData);;
        byte[] result = AESUtils.decrypt(Base64.getDecoder().decode(encryptedData), decoder.decode(jsonStr.getString("session_key")),
                AESUtils.generateIV(decoder.decode(iv)));
        Long orderinfo_id;
        String phoneNumber = "";
        //判断返回参数是否为空
        if (null != result && result.length > 0) {
            try {
                String jsons = new String(result, "UTF-8");
                JSONObject json2 = JSONObject.parseObject(jsons);
                //json解析phoneNumber值
                phoneNumber = (String) json2.get("phoneNumber");
                jsonObject.put("phoneNumber", phoneNumber);
            } catch (Exception e) {
//                e.printStackTrace();
                // System.out.println(e);
                System.out.println("错误");
            }
        } else {
            jsonObject.put("phoneNumber", "未查询到");
        }
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @ApiOperation("通过用户id查询用户的部门")
    @GetMapping("/findUserDept")
    public Result findUserDept(String id) {
        //查询学生
        EllUser user = appUserService.findUserById(id);
        List<PfDepart> pfDeparts = appUserService.selUserPfDepart(user.getOrgId());
        //返回解密的身份证进行二维码生成
        //userVo.setIdCard(RSAUtils.decode(userVo.getIdCard(), RsaPrivate));
        return new Result(HttpStatus.SUCCESS, "查询成功", pfDeparts);
    }

    @ApiOperation("解除微信绑定")
    @GetMapping("/relieveUserWx")
    public Result relieveUserWx(String id) {
        return new Result(HttpStatus.SUCCESS, "解除成功", appUserService.relieveUserWx(id));
    }

    @ApiOperation("用户注册")
    @PostMapping("/registerAppUser")
    public Result registerAppUser(String idCard, String wxOpenId) {
        //判断是否为正确的身份证
        if (!IsIdCardUtil.isIDNumber(idCard)) {
            return new Result(HttpStatus.ID_CARD_ERROR, "身份证错误");
        }
        List<String> stringList = appUserService.findUserIdByIdentification(IdCardUtil.identificationString(idCard));
        if (stringList.size() <= 0) {
            return new Result(HttpStatus.NOT_FOUND, "没有此用户");
        }
        for (int i = 0; i < stringList.size(); i++) {
            EllUser user = appUserService.findUserById(stringList.get(i));
            if (user != null && RSAUtils.decode(user.getIdCard(), RsaPrivate).equals(idCard)) {
                int succeed = appUserService.updateUserWxOpenId(stringList.get(i), wxOpenId);
                if (succeed > 0) {
                    EllUserVo userVo = appUserService.findUser(wxOpenId);
                    //查询学校名称
                    userVo.setSchoolName(appUserService.findUserSchoolName(user.getOrgId()));
                    return new Result(HttpStatus.SUCCESS, "成功", userVo);
                } else {
                    return new Result(HttpStatus.ERROR, "此用户已被其账号绑定");
                }
            }
        }
        return new Result(HttpStatus.ERROR, "没有此用户");
    }

    public JSONObject getWxUser(String code, String appId, String secret) {

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

 /*       JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", openId);
        jsonObject.put("sex", sex);
        jsonObject.put("headimgurl", headimgurl);
        jsonObject.put("nickname", nickname);*/
        return jsonStr;
    }

    @ApiOperation("查询计划")
    @GetMapping("/findPlan")
    public Result findPlan(String userId) {
        List<EllOrderPlanVo> ellPlanList = appUserService.findPlan(userId);
        for (int i = 0; i < ellPlanList.size(); i++) {
            ellPlanList.get(i).setNum(appUserService.findAppointmentAllNum(ellPlanList.get(i).getId()));
            ellPlanList.get(i).setAuthNum(appUserService.selPlanNum(ellPlanList.get(i).getId()));

        }
        return new Result(HttpStatus.SUCCESS, "成功", ellPlanList);
    }

    @ApiOperation("查询每个时间段人数")
    @GetMapping("/findReservationNum")
    public Result findReservationNum(String planId) throws ParseException {
        return new Result(HttpStatus.SUCCESS, "成功", appUserService.findTimeAppointment(planId));
    }

    @ApiOperation("最近一次计划成绩查询")
    @GetMapping("/findRecentlyHistory")
    public Result findRecentlyHistory(String userId) {
        return new Result(HttpStatus.SUCCESS, "成功", appUserService.findHistory(userId));
    }

    @ApiOperation("预约计划")
    @PostMapping("/appointmentPlan")
    public Result appointment(@RequestBody AppointmentPlanVo appointmentPlanVo) {
        EllSubscribeNumVo ellSubscribeNumVo = appUserService.findYetOrderPeopleNumber(appointmentPlanVo.getSubDateId(), appointmentPlanVo.getDayTime(), appointmentPlanVo.getPlanId());
        if (ellSubscribeNumVo != null && ellSubscribeNumVo.getNum() <= ellSubscribeNumVo.getNumSubscribe()) {
            return new Result(HttpStatus.CONFLICT, "预约已满，不允许再预约！");
        }
        int i = appUserService.isHasAccomplishPlan(appointmentPlanVo.getUserId());
        //int i =
        if (i > 0) {
            return new Result(HttpStatus.ALREADY_EXISTS, "上个计划还没完成,请先手动完成计划");
        }
        return new Result(HttpStatus.SUCCESS, "成功", appUserService.addAppointmentState(appointmentPlanVo));
    }

    @ApiOperation("完成预约计划")
    @PostMapping("/appointmentFinish")
    public Result appointmentFinish(@RequestBody EllPlanUser ellPlanUser) {
        int i = appUserService.findIsHasEndScore(ellPlanUser.getPlanId(), ellPlanUser.getUserId());
        if (i <= 0) {
            return new Result(HttpStatus.ALREADY_EXISTS, "上个计划还没完成测试");
        }
        return new Result(HttpStatus.SUCCESS, "成功", appUserService.updateAppointmentState(ellPlanUser));
    }

    @ApiOperation("查询用户测试过的计划")
    @GetMapping("/findUserPlan")
    public Result findUserPlan(String userId, int isExam) {
        return new Result(HttpStatus.SUCCESS, "成功", appUserService.findPlanByUserOrgId(userId, isExam));
    }

    @ApiOperation("查询某一计划成绩")
    @GetMapping("/findPlanScore")
    public Result findPlanScore(String userId, String planId) {
        return new Result(HttpStatus.SUCCESS, "成功", appUserService.findUserHistory(userId, planId));
    }

    @ApiOperation("杭研Sign获取")
    @PostMapping("/hySign")
    public Result hySign(@RequestBody SignDto signDto) throws Exception {
        String encrypt = AESUtil.encrypt(signDto.getPhoneNumber(), key, iv);
        HashMap<String, String> map = new HashMap<>();
        TreeSet<String> treeSet = new TreeSet<>();
        treeSet.add(signDto.getDate());
        treeSet.add(encrypt);
        treeSet.add(password);
        String sign = SignUtil.getSign(treeSet);
        map.put("vendorId", vendorId);
        map.put("userName", encrypt);
        map.put("sign", sign);
        return new Result(HttpStatus.SUCCESS, "加密成功", map);
    }

    @ApiOperation("上传免测图片")
    @PostMapping("/avoidImage")
    public Result avoidImage(MultipartFile image) throws IOException {
        String fileName = "";
        String url = "";
        if (!image.isEmpty()) {
            String[] type = image.getContentType().toString().split("/");
            //把文件写入到目标位置
            fileName = new Date().getTime() + IdUtils.fastSimpleUUID() + "." + type[1];
            String path = "";
            if (FileUploadUtil.isLinux() == false) {
                path = WindowsSite.ImageAvoid + fileName;
                url = WindowsSite.ImageAvoidNginx + fileName;
            } else {
                path = LinuxSite.ImageAvoid + fileName;
                url = LinuxSite.ImageAvoidNginx + fileName;
            }
            File file = new File(path);
            PathGeneration.createPath(path);
            image.transferTo(file);//把文件写入目标文件地址
        }
        return new Result(HttpStatus.SUCCESS, "成功", url);
    }
}
