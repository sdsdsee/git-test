package com.eluolang.app.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.eluolang.app.manage.dto.EllUserVo;
import com.eluolang.app.manage.redis.SkipAppRedisService;
import com.eluolang.app.manage.service.SkipAppService;
import com.eluolang.app.manage.util.*;
import com.eluolang.app.manage.vo.*;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.pojo.EllSkipDevice;
import com.eluolang.common.core.pojo.EllSkipJoinContest;
import com.eluolang.common.core.pojo.EllUser;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.common.core.util.SendSmsAliYun;
import com.eluolang.common.core.util.StringUtils;
import com.eluolang.common.core.web.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(tags = "易绳")
@RestController
@Transactional
@Slf4j
public class SkipAppController {
    @Autowired
    private SkipAppRedisService skipAppRedisService;
    @Autowired
    private RestTemplate restTemplate;
    //易绳的appid
    @Value("${appId.YsAppId}")
    private String YsAppId;
    //易绳的secret
    @Value("${appId.YsAppSecret}")
    private String YsAppSecret;
    @Autowired
    private SkipAppService skipAppService;
    @Value("${RSA.private}")//私钥
    private String RsaPrivate;

    @ApiOperation("TEST")
    @PostMapping("/TEST")
    public String test(@RequestBody String I) {
        System.out.println(I);
        return I;
    }

    @ApiOperation("TEST2")
    @PostMapping("/TEST2")
    public MultipartFile test2(MultipartFile file) {
        System.out.println(file);
        return file;
    }

    @ApiOperation("跳绳用户登录")
    @GetMapping("/skipUserLogin")
    public Result skipUserLogin(String code) {
        JSONObject jsonStr = WxAppLoginUtil.getWxUser(restTemplate, code, YsAppId, YsAppSecret);
        //用户唯一标识
        String openId = (String) jsonStr.get("openid");
        //判断是否获取到openId
        if (openId == null || openId == "") {
            return new Result(HttpStatus.PARTIAL_DATA_ERROR, "请刷新/重新进入小程序后登陆");
        }
        //查询此微信是否绑定用户信息
        EllUserVo ellUser = skipAppService.findUser(openId);
        if (ellUser != null) {
        /*    //游客用户
            if (ellUser.getUserRole() == 4) {
                ellUser.setUserName("游客" + openId.substring(0, 6));
            }*/
            return new Result(HttpStatus.SUCCESS, "查询成功", ellUser);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("wxOpenId", openId);
        //小程序是在前端获取
        //用户名
        //String nickname = (String) jsonStr.get("nickname");
        //用户头像地址
        //String headimgurl = (String) jsonStr.get("headimgurl");
        //int sexNumber = (int) jsonStr.get("sex");
        return new Result(HttpStatus.NOT_FOUND, "未进行注册绑定!", jsonObject);
    }

    @ApiOperation("跳绳用户注册")
    @PostMapping("/skipRegisterAppUser")
    public Result registerAppUser(@RequestBody EllSkipUserVo ellSkipUserVo) {
        //判断验证码是否正确
        String code = skipAppRedisService.findLockCode(String.valueOf(ellSkipUserVo.getPhone()));
        if (code != null && code.equals(ellSkipUserVo.getCode())) {
            //判断是否为正确的身份证
            if (!IsIdCardUtil.isIDNumber(ellSkipUserVo.getIdCard())) {
                return new Result(HttpStatus.ID_CARD_ERROR, "身份证错误");
            }
            //生成id
            ellSkipUserVo.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
            //注册时候微信wxOpenId放的是查询wxOpenId的code
            //获取微信openid
            String wxCode = ellSkipUserVo.getWxOpenId();
            JSONObject jsonStr = WxAppLoginUtil.getWxUser(restTemplate, wxCode, YsAppId, YsAppSecret);
            //用户唯一标识openid
            String openId = (String) jsonStr.get("openid");
            //判断是否获取到openId
            if (openId == null || openId == "") {
                return new Result(HttpStatus.PARTIAL_DATA_ERROR, "请刷新/重新进入小程序后注册");
            }
            ellSkipUserVo.setWxOpenId(openId);
            List<String> stringList = skipAppService.findUserIdByIdentification(IdCardUtil.identificationString(ellSkipUserVo.getIdCard()));
            //没有就注册为游客
            if (stringList.size() <= 0) {
                ellSkipUserVo.setIsTourist(1);
                ellSkipUserVo.setUserRole(4);
                //设置游客姓名
                ellSkipUserVo.setUserName("游客" + ellSkipUserVo.getWxOpenId().substring(0, 6));
                //游客的userId就是直接的id
                ellSkipUserVo.setUserId(ellSkipUserVo.getId());
                int succeed = skipAppService.addSkipUserWx(ellSkipUserVo);
                if (succeed <= 0) {
                    return new Result(HttpStatus.ERROR, "此用户此角色或微信已绑定其它账号或用户请先注销用户在注册");
                }
                EllUserVo userVo = new EllUserVo();
                userVo.setWxOpenId(openId);
                userVo.setUserName("游客" + ellSkipUserVo.getWxOpenId().substring(0, 6));
                //游客为4
                userVo.setUserRole(4);
                userVo.setId(ellSkipUserVo.getUserId());
                return new Result(HttpStatus.SUCCESS, "成功", userVo);
            }
            for (int i = 0; i < stringList.size(); i++) {
//                log.info(""+(code==ellSkipUserVo.getCode()));
                //后台拥有的用户
                EllUser user = skipAppService.findUserById(stringList.get(i));
                if (user != null && RSAUtils.decode(user.getIdCard(), RsaPrivate).equals(ellSkipUserVo.getIdCard())) {
                    //查询后台用户头像id
                    String imgId = skipAppService.findUserImgId(user.getId());
                    ellSkipUserVo.setIsTourist(0);
                    ellSkipUserVo.setUserId(stringList.get(i));
                    ellSkipUserVo.setImgId(imgId);
                    ellSkipUserVo.setUserName(user.getUserName());
                    int succeed = skipAppService.addSkipUserWx(ellSkipUserVo);
                    if (succeed > 0) {
                        EllUserVo userVo = skipAppService.findUser(ellSkipUserVo.getWxOpenId());
                        //查询学校名称
                        //userVo.setSchoolName(skipAppService.findUserSchoolName(user.getOrgId()));
                        return new Result(HttpStatus.SUCCESS, "成功", userVo);
                    } else {
                        return new Result(HttpStatus.ERROR, "此用户此角色或微信已绑定其它账号或用户请先注销用户在注册");
                    }
                } else if (user == null && (i + 1) == stringList.size()) {
                    //后台没有的用户注册为游客
                    EllUserVo userVo = skipAppService.findTourist(stringList.get(i), ellSkipUserVo.getUserRole());
                    if (userVo == null) {
                        //设置游客姓名
                        ellSkipUserVo.setUserName("游客" + ellSkipUserVo.getWxOpenId().substring(0, 6));
                        ellSkipUserVo.setIsTourist(1);
                        ellSkipUserVo.setUserRole(4);
                        ellSkipUserVo.setUserId(ellSkipUserVo.getId());
                        int succeed = skipAppService.addSkipUserWx(ellSkipUserVo);
                        if (succeed > 0) {
                            userVo = new EllUserVo();
                            userVo.setWxOpenId(openId);
                            userVo.setUserName("游客" + ellSkipUserVo.getWxOpenId().substring(0, 6));
                            //游客为4
                            userVo.setUserRole(4);
                            userVo.setId(ellSkipUserVo.getUserId());
                            return new Result(HttpStatus.SUCCESS, "成功", userVo);
                        }
                    }
                    return new Result(HttpStatus.ERROR, "此身份证已经绑定了其它游客账号");
                }
            }
        }
//        log.info(""+code.equals(ellSkipUserVo.getCode()));

        return new Result(HttpStatus.CODE_ERROR, "验证码错误");
    }

    @ApiOperation("验证码")
    @PostMapping("/getCode")
    public Result registerAppUser(String phone) throws Exception {
        int code = SendSmsAliYun.codeCreate();
        if (skipAppRedisService.getLock(phone + "", 5, code)) {
            Map<String, String> stringStringMap = SendSmsAliYun.sendMassage(phone + "", code);
            if (stringStringMap.get("code").equals("OK")) {
                return new Result(HttpStatus.SUCCESS, "发送成功");
            } else {
                return new Result(HttpStatus.CODE_SEND_ERROR, stringStringMap.get("massage"));
            }
        }
        return new Result(HttpStatus.CODE_REPETITION_SEND, "请勿重复发送");
    }

    @ApiOperation("跳绳上传成绩")
    @PostMapping("/addPerformance")
    public Result addPerformance(@RequestBody EllSkipNumberVo ellSkipNumberVo) {
        if (ellSkipNumberVo.getIsHomework() == 1) {
            EllSkipHomeworkVo ellSkipHomeworkVo = new EllSkipHomeworkVo();
            ellSkipHomeworkVo.setId(ellSkipNumberVo.getHomeworkId());
            ellSkipHomeworkVo.setUserId(ellSkipNumberVo.getSkipUserId());
            List<EllSkipHomeworkVo> ellSkipHomeworkVoList = skipAppService.findHomework(ellSkipHomeworkVo);
            switch (ellSkipHomeworkVoList.get(0).getWorkPattern()) {
                case 2:
                    if (ellSkipHomeworkVoList.get(0).getWorkSetting() > ellSkipNumberVo.getSkipTime()) {
                        return new Result(HttpStatus.ERROR, "上传失败,没有达到作业要求");
                    }
                    break;
                case 3:
                    if (ellSkipHomeworkVoList.get(0).getWorkSetting() > ellSkipNumberVo.getSkipNum()) {
                        return new Result(HttpStatus.ERROR, "上传失败,没有达到作业要求");
                    }
                    break;
            }
        }
        int i = skipAppService.addSkipNum(ellSkipNumberVo);
        if (i > 0) {
            return new Result(HttpStatus.SUCCESS, "上传成功");
        }
        return new Result(HttpStatus.ERROR, "上传失败");
    }

    @ApiOperation("比赛上传成绩")
    @PostMapping("/addContestPerformance")
    public Result addContestPerformance(EllSkipNumberVo ellSkipNumberVo, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        int i = skipAppService.addSkipContestNum(ellSkipNumberVo, file);
        if (i > 0) {
            return new Result(HttpStatus.SUCCESS, "上传成功");
        }
        if (i < 0) {
            return new Result(HttpStatus.ERROR, "成绩上传成功,但不计算排名,请严格按照比赛内容进行参赛");
        }
        return new Result(HttpStatus.ERROR, "上传失败");
    }

    @ApiOperation("查询本周每天的数量")
    @GetMapping("/findWeekSkipNum")
    public Result findWeekSkipNum(String skipUserId) {
        return new Result(HttpStatus.SUCCESS, "查询成功", skipAppService.findSkipDayNum(skipUserId));
    }

    @ApiOperation("跳绳记录")
    @GetMapping("/skipRecord")
    public Result skipRecord(String skipUserId, int dayNum, int isWork, int page) {
        PageHelper.startPage(page, 20);
        //查询前多少天的跳绳记录
        List<EllSkipNumberVo> skipNumberVoList = skipAppService.findSkipTimeQuantum(skipUserId, -dayNum, isWork);
        //把时间分秒去掉
        skipNumberVoList.stream().forEach(skip -> skip.setCreateTime(CreateTime.excludeMinuteAndSecond(skip.getCreateTime())));
        PageInfo pageInfo = new PageInfo<>(skipNumberVoList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("size", pageInfo.getTotal());
        jsonObject.put("skipNumberVoList", skipNumberVoList);
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    @ApiOperation("设备连接")
    @GetMapping("/skipConnect")
    public Result skipConnect(String userId, String deviceCoding) {
        List<EllSkipDevice> ellSkipDeviceList = skipAppService.findDeviceCoding(userId, deviceCoding);
        if (ellSkipDeviceList != null && ellSkipDeviceList.size() > 1) {
            return new Result(HttpStatus.CONFLICT, "用户已绑定其他设备或设备已被其他用户绑定");
        }
        //设备绑定
        if (ellSkipDeviceList == null) {
            skipAppService.addUserSkipDevice(IdUtils.fastSimpleUUID(), userId, deviceCoding);
        }
        return new Result(HttpStatus.SUCCESS, "成功");
    }

    @ApiOperation("查询比赛")
    @PostMapping("/selContest")
    public Result selContest(@RequestBody EllSkipContestVo ellSkipContestVo) {
        return new Result(HttpStatus.SUCCESS, "成功", skipAppService.findEllSkipContest(ellSkipContestVo));
    }

    @ApiOperation("比赛报名")
    @PostMapping("/addContest")
    public Result selContest(@RequestBody EllSkipJoinContest ellSkipJoinContest) throws Exception {
        return new Result(HttpStatus.SUCCESS, "成功", skipAppService.addContest(ellSkipJoinContest));
    }

    @ApiOperation("设备命令")
    @GetMapping("/deviceOrder")
    public Result deviceOrder(int pattern, int parameter) throws DecoderException {
        //前缀
        String orderTop = "AA01000D";
        //中间部分
        String orderCenter = "0100040008";
        //结尾
        String orderEnd = "3e00000000";
        //补全命令
        StringBuffer stringBuffer = new StringBuffer(orderCenter + "0" + pattern);
        //转成4字节16进制
        String parameterOrder = StringUtils.toHex(parameter, 4);
        stringBuffer.append(parameterOrder + orderEnd);
        //通过拼接的中后的命令计算校验码
        byte[] decodeHex = Hex.decodeHex(stringBuffer.toString());
        int orderVerify = 0;
        for (int i = 0; i < decodeHex.length; i++) {
            orderVerify = 0xff & (decodeHex[i] + orderVerify);
        }
        //转成2字节的16进制
        String orderVerifySex = StringUtils.toHex(orderVerify, 2);
        return new Result(HttpStatus.SUCCESS, "成功", orderTop + orderVerifySex + "00" + stringBuffer.toString());
    }

    @ApiOperation("查询总个数和总时间")
    @GetMapping("/selAllSkipNumAndTime")
    public Result findSkipAllNumAndTime(String skipUserId) {
        return new Result(HttpStatus.SUCCESS, "查询成功!", skipAppService.findSkipAllNumAndTime(skipUserId));
    }

    @ApiOperation("查询比赛排名")
    @GetMapping("/findSkipRanking")
    public Result findSkipRanking(String skipContestId, String userId) {
        PageHelper.startPage(1, 10);
        //查询前10排名
        List<EllSkipRankingVo> ellSkipRankingVoList = skipAppService.findSkipRanking(skipContestId, null);
        //查询本人排名
        List<EllSkipRankingVo> ellSkipRankingVoMyself = skipAppService.findSkipRanking(skipContestId, userId);
        JSONObject data = new JSONObject();
        data.put("topTen", ellSkipRankingVoList);
        data.put("myself", ellSkipRankingVoMyself.get(0));
        return new Result(HttpStatus.SUCCESS, "查询成功!", data);
    }

    @ApiOperation("查询比赛个数")
    @GetMapping("/findNum")
    public Result findNum() {

        return new Result(HttpStatus.SUCCESS, "查询成功!", skipAppService.findNum());
    }

    @ApiOperation("查询作业")
    @PostMapping("/selHomework")
    public Result selHomework(@RequestBody EllSkipHomeworkVo ellSkipHomeworkVo) {
        PageHelper.startPage(ellSkipHomeworkVo.getPage(), 15);
        List<EllSkipHomeworkVo> ellSkipHomeworkVoList = skipAppService.findHomework(ellSkipHomeworkVo);
        PageInfo info = new PageInfo<>(ellSkipHomeworkVoList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("size", info.getTotal());
        jsonObject.put("homework", ellSkipHomeworkVoList);
        return new Result(HttpStatus.SUCCESS, "查询成功!", jsonObject);
    }
}
