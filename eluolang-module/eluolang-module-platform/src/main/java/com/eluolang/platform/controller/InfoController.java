package com.eluolang.platform.controller;


import act.Method;
import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.pojo.EllInfoPublishTarget;
import com.eluolang.common.core.pojo.EllInformation;
import com.eluolang.common.core.pojo.EllLedPublishInfo;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.common.core.web.Result;
import com.eluolang.common.log.annotation.Log;
import com.eluolang.common.log.constant.OperType;
import com.eluolang.platform.mapper.InfoMapper;
import com.eluolang.platform.redis.DeviceRService;
import com.eluolang.platform.service.InfoService;
import com.eluolang.platform.util.CreateTime;
import com.eluolang.platform.util.LEDLargeScreenUtil;

import com.eluolang.platform.util.RSAUtils;
import com.eluolang.platform.util.Test;
import com.eluolang.platform.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import po.DisplayParam;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@Transactional
@Api(tags = "信息发布管理")
public class InfoController {
    @Autowired
    private InfoService infoService;
    @Autowired
    private DeviceRService deviceRService;
    // new 一个方法对象

    @Value("${url.nginx}")
    public String URL;

    @ApiOperation("test")
    @PostMapping("/uploadFile")
    @ResponseBody
    public String uploadFile(@RequestParam("files") MultipartFile[] file, @RequestParam("id") String id) {


        if (file == null && file.length == 0) {
            return "没有文件";
        }

        return "没有文件";
    }

    @Log(title = "添加信息", operType = OperType.INSERT, content = "添加信息")
    @ApiOperation("添加信息")
    @PostMapping("/addInfo")
    @ApiParam(name = "files", value = "files", required = false)
    public Result addInfo( EllInformationVo ellInformationVo) throws IOException {
        int i = infoService.addInfo(ellInformationVo);
        if (i > 0) {
            return new Result(HttpStatus.SUCCESS, "成功", i);
        }
        return new Result(HttpStatus.INFO_NOT_CONTENT, "信息没有内容请上传内容");
    }

    @ApiOperation("分页查询")
    @PostMapping("/selInfo")
    public Result selInfo(@RequestBody EllInformation ellInformation, int page) {
        PageHelper.startPage(page, 10);
        List<EllSelInfoVo> ellInformationList = infoService.findInfo(ellInformation);
        PageInfo pageInfo = new PageInfo<>(ellInformationList);
        for (int i = 0; i < ellInformationList.size(); i++) {
            //查询信息的文件
            List<EllFIleVo> stringList = infoService.selFileUrl(ellInformationList.get(i).getId());
            if (stringList != null && stringList.size() > 0) {
                ellInformationList.get(i).setFileUrl(stringList);
            }
            //查询发布的设备
            List<EllInfoPublishTarget> ellInfoPublishTargetList = infoService.selPublishDevice(ellInformationList.get(i).getId());
            if (ellInfoPublishTargetList != null && ellInfoPublishTargetList.size() > 0) {
                ellInformationList.get(i).setDevices(ellInfoPublishTargetList);
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("info", ellInformationList);
        jsonObject.put("size", pageInfo.getTotal());
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    /**
     * 更新数据
     *
     * @param ellInformation 实例对象
     * @return 实例对象
     */
    @Log(title = "更新信息数据", operType = OperType.UPDATE, content = "更新信息数据")
    @ApiOperation("更新数据")
    @PostMapping("/upInfo")
    public Result upInfo(EllInformationVo ellInformation) throws IOException {
        int i = infoService.update(ellInformation);
        if (i > 0) {
            return new Result(HttpStatus.SUCCESS, "成功", i);
        }
        return new Result(HttpStatus.INFO_NOT_CONTENT, "信息没有内容请上传内容");
    }

    /**
     * 查询信息发布的设备
     */
    @ApiOperation("查询信息发布的设备")
    @GetMapping("/selPublishDevice")
    public Result selPublishDevice(String infoId) {
        return new Result(HttpStatus.SUCCESS, "成功", infoService.selPublishDevice(infoId));
    }

    /**
     * 文件保存
     */
    @Log(title = "文件保存", operType = OperType.INSERT, content = "文件保存")
    @ApiOperation("文件保存")
    @PostMapping("/uploadInfoFile")
    public Result uploadFile(int infoType, MultipartFile files) throws IOException {
        EllAddFileVo ellAddFileVo = infoService.uploadImage(infoType, files);
        if (ellAddFileVo != null) {
            return new Result(HttpStatus.SUCCESS, "成功", ellAddFileVo);
        }
        return new Result(HttpStatus.ERROR, "文件过大");
    }

    /**
     * 更新数据
     *
     * @param id 实例对象
     * @return 实例对象
     */
    @ApiOperation("删除信息")
    @Log(title = "删除信息", operType = OperType.DELETE, content = "删除信息")
    @PostMapping("/delInfo")
    public Result delInfo(String id) throws Exception {
        int i = infoService.delInfoDevice(id);
        switch (i) {
            case 1:
                return new Result(HttpStatus.SUCCESS, "成功");
            case -2:
                return new Result(HttpStatus.NOT_FOUND, "没有找到此信息");
            case -3:
                return new Result(HttpStatus.OUT_SIZE, "有设备已经存在文字信息请先删除");
            case -4:
                return new Result(HttpStatus.OUT_SIZE, "有设备发布的信息已经超出10个信息，请先结束/删除");
            case -5:
                return new Result(HttpStatus.ERROR, "没有这个设备/此设备异常");
        }
        return new Result(HttpStatus.SUCCESS, "成功");
    }

    /**
     * 更新数据
     *
     * @param ellInformation 实例对象
     * @return 实例对象
     */
    @Log(title = "发布信息", operType = OperType.UPDATE, content = "发布信息")
    @ApiOperation("发布信息")
    @PostMapping("/upInfoState")
    public Result upInfoState(@RequestBody EllInformationVo ellInformation) throws Exception {
        //不在线的设备
        String deviceIdErro = "";
        //在线的设备
        String deviceIdSuccess = null;
        String deviceArray[] = null;
        //当只有一个设备号的时候就直接设置
        deviceArray = AllIssueDeviceId(ellInformation);
        if (deviceArray == null && deviceArray.length < 1) {
            return new Result(HttpStatus.ERROR, "请选择设备");
        }
        //判断设备是否在线
     /*   Map<String, String> stringMap = isOnlineDevice(deviceArray);
        deviceIdErro = stringMap.get("error");
        deviceIdSuccess = stringMap.get("success");
        //在线的设备
        if (deviceIdSuccess != null) {
            if (deviceIdSuccess.contains(",")) {
                deviceArray = deviceIdSuccess.split(",");
            } else {
                deviceArray = new String[1];
                deviceArray[0] = deviceIdSuccess;
            }
        }*/
        int i = infoService.upInfoState(ellInformation, deviceArray);
        if (deviceIdErro != null || deviceIdErro != "" || (!deviceIdErro.equals("null"))) {
            return new Result(HttpStatus.ERROR, "部分设备未发布成功:" + deviceIdErro + "(无此设备/设备异常)");
        }
        switch (i) {
            case 1:
                return new Result(HttpStatus.SUCCESS, "成功");
            case -2:
                return new Result(HttpStatus.NOT_FOUND, "没有找到此信息");
            case -3:
                return new Result(HttpStatus.OUT_SIZE, "有设备已经存在文字信息请先删除");
            case -4:
                return new Result(HttpStatus.OUT_SIZE, "有设备发布的信息已经超出10个信息，请先结束/删除");
            case -5:
                return new Result(HttpStatus.ERROR, "没有这个设备/此设备异常");
        }
        return new Result(HttpStatus.UNSUPPORTED_TYPE, "状态出错/没有该条信息");
    }

    /**
     * 更新数据
     *
     * @param ellInformation 实例对象
     * @return 实例对象
     */
    @Log(title = "结束该设备的某条信息发布", operType = OperType.UPDATE, content = "结束该设备的某条信息发布")
    @ApiOperation("结束该设备的某条信息发布")
    @PostMapping("/endInfoState")
    public Result endInfoState(@RequestBody EllInformationVo ellInformation) throws Exception {

        //不在线的设备
        String deviceIdErro = null;
        //在线的设备
        String deviceIdSuccess = null;
        String deviceArray[] = null;
        //当只有一个设备号的时候就直接设置
        deviceArray = AllIssueDeviceId(ellInformation);
        if (deviceArray == null && deviceArray.length < 1) {
            return new Result(HttpStatus.ERROR, "请选择设备");
        }
        //判断设备是否在线
        Map<String, String> stringMap = isOnlineDevice(deviceArray);
        deviceIdErro = stringMap.get("error");
        deviceIdSuccess = stringMap.get("success");
        //在线的设备
        if (deviceIdSuccess != null) {
            if (deviceIdSuccess.contains(",")) {
                deviceArray = deviceIdSuccess.split(",");
            } else {
                deviceArray = new String[1];
                deviceArray[0] = deviceIdSuccess;
            }
        }
        int i = infoService.upInfoState(ellInformation, deviceArray);
        if (deviceIdErro != null) {
            return new Result(HttpStatus.ERROR, "部分设备未结束成功:" + deviceIdErro + "(无此设备/设备异常)");
        }
        switch (i) {
            case 1:
                return new Result(HttpStatus.SUCCESS, "成功");
            case -2:
                return new Result(HttpStatus.NOT_FOUND, "没有找到此信息");
            case -3:
                return new Result(HttpStatus.OUT_SIZE, "有设备已经存在文字信息请先删除");
            case -4:
                return new Result(HttpStatus.OUT_SIZE, "有设备发布的信息已经超出10个信息，请先结束/删除");
            case -5:
                return new Result(HttpStatus.ERROR, "没有这个设备/此设备异常");
        }
        return new Result(HttpStatus.UNSUPPORTED_TYPE, "状态出错/没有该条信息");
    }

    /**
     * 根据设备id和发布id查询信息发布记录
     *
     * @return 实例对象
     */
    @ApiOperation("根据设备id和发布id查询信息发布记录")
    @PostMapping("/selInfoDeliveryFile")
    public Result selInfoDeliveryFile(String deviceId, String infoId) {
        Map<String, String> map = RSAUtils.createRSAKeys();
        //根据设备id和发布id查询信息发布记录
        InfoDeliveryFileVo infoDeliveryFileVo = infoService.selInfoDeliveryFile(deviceId, infoId);
        if (infoDeliveryFileVo == null) {
            return new Result(HttpStatus.ERROR, "未查询到信息发布数据", null);
        }
        if (infoDeliveryFileVo.getInfoType() == 1 || infoDeliveryFileVo.getInfoType() == 2) {
            //根据信息发布id查询发布文件
            List<DeliveryFileVo> deliveryFileVoList = infoService.selFileByInfoId(infoId);
            if (deliveryFileVoList.size() != 0) {
                infoDeliveryFileVo.setFileInfo(deliveryFileVoList);
                return new Result(HttpStatus.SUCCESS, "查询成功", RSAUtils.encode(JSONObject.toJSON(infoDeliveryFileVo).toString(), map.get("public")));
            }
            return new Result(HttpStatus.ERROR, "发布类型为文件，但未查询到内容", null);
        }
        return new Result(HttpStatus.SUCCESS, "查询成功", RSAUtils.encode(JSONObject.toJSON(infoDeliveryFileVo).toString(), map.get("public")));
    }

    /**
     * led信息发布
     *
     * @return 实例对象
     */
    @ApiOperation("led发送图片")
    @PostMapping("/ledInfoImage")
    public Result ledInfo(@RequestBody DisplayParamVo displayParamVo) throws Exception {
        EllLedInformationVo ellLedInformationVo = new EllLedInformationVo();
        ellLedInformationVo.setId(displayParamVo.getInfoId());
        ellLedInformationVo.setInfoType(1);
        List<EllLedInformationVo> ellLedInformationVoList = infoService.selLedInfo(ellLedInformationVo);
        //查询文件地址
        if (ellLedInformationVoList != null && ellLedInformationVoList.size() > 0) {
            displayParamVo.setFileUrl(URL + infoService.selFileLed(ellLedInformationVoList.get(0).getImageId()));
        }
        Method method = new Method();
        method.connect(
                "www.listentech.com.cn", 9035, 1, "admin", "admin");
        String[] deviceId = null;
        if (displayParamVo.getDeviceIds().contains(",")) {
            deviceId = displayParamVo.getDeviceIds().split(",");
        } else {
            deviceId = new String[1];
            deviceId[0] = displayParamVo.getDeviceIds();

        }

        List<EllDeviceIdAndNum> ellDeviceIdAndNumList = infoService.selDeviceId(deviceId, 3);
        deviceId = new String[ellDeviceIdAndNumList.size() + 1];
        for (int i = 0; i < ellDeviceIdAndNumList.size(); i++) {
            deviceId[i] = ellDeviceIdAndNumList.get(i).getDeviceId();
        }
        //order是播放顺序
        Map<String, String> map = LEDLargeScreenUtil.sendImage(method, displayParamVo, deviceId, 1);
        //发送失败的设备编号
        String deviceNumber = "";
        List<EllLedPublishInfo> ellLedPublishInfoList = new ArrayList<>();
        for (int i = 0; i < ellDeviceIdAndNumList.size(); i++) {
            //判断那个设备发送成功
            if (map.get(ellDeviceIdAndNumList.get(i).getDeviceId()) != null && map.get(ellDeviceIdAndNumList.get(i).getDeviceId()).equals("RESULT_OK")) {
                EllLedPublishInfo ellLedPublishInfo = new EllLedPublishInfo();
                ellLedPublishInfo.setLedInfoId(displayParamVo.getInfoId());
                ellLedPublishInfo.setCreateTime(CreateTime.getTime());
                ellLedPublishInfo.setType(2);
                ellLedPublishInfo.setContent(JSONObject.toJSON(displayParamVo).toString());
                ellLedPublishInfo.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                ellLedPublishInfoList.add(ellLedPublishInfo);
            } else {
                if (i == 0) {
                    deviceNumber = ellDeviceIdAndNumList.get(i).getDeviceId();
                } else {
                    deviceNumber = deviceNumber + "/" + ellDeviceIdAndNumList.get(i).getDeviceId();
                }
            }
        }

        //关闭连接
        method.disConnect();
        if (ellLedPublishInfoList.size() > 0) {
            infoService.addPublishLedInfo(ellLedPublishInfoList);
        }
        if (deviceNumber != "") {
            return new Result(HttpStatus.BAD_REQUEST, "部分设备未发送成功这些设备请重试" + deviceNumber);
        }
        return new Result(HttpStatus.SUCCESS, "成功");
    }

    /**
     * 添加led发布信息
     */
    @Log(title = "添加LED信息", operType = OperType.INSERT, content = "添加LED信息")
    @ApiOperation("添加LED信息")
    @PostMapping("/addLEDInfo")
    public Result addLEDInfo(@RequestBody EllLedInformationVo ellLedInformationVo) throws IOException {
        return new Result(HttpStatus.SUCCESS, "成功", infoService.insertLedInfo(ellLedInformationVo));
    }

    /**
     * 更新数据
     *
     * @param ellLedInformationVo 实例对象
     * @return 实例对象
     */
    @Log(title = "更新LED信息数据", operType = OperType.UPDATE, content = "更新LED信息数据")
    @ApiOperation("更新LED数据")
    @PostMapping("/upLEDInfo")
    public Result upLEDInfo(@RequestBody EllLedInformationVo ellLedInformationVo) throws IOException {
  /*      EllLedInformationPublishVo ellLedInformationPublishVo = new EllLedInformationPublishVo();
        ellLedInformationPublishVo.setLedInfoId(ellLedInformationPublishVo.getId());
        List<EllLedInformationPublishVo> ellLedInformationPublishVoList = infoService.selLedPublish(ellLedInformationPublishVo);
        if (ellLedInformationPublishVoList!=null &&ellLedInformationPublishVoList.size()>0){
            return new Result(HttpStatus.ERROR,"该信息已发布不允许修改");
        }*/
        return new Result(HttpStatus.SUCCESS, "成功", infoService.updateLED(ellLedInformationVo));
    }

    /**
     * 更新数据
     *
     * @return 实例对象
     */
    @Log(title = "发布LED信息数据", operType = OperType.UPDATE, content = "发布LED信息数据")
    @ApiOperation("发布LED文字")
    @PostMapping("/publishLEDInfo")
    public Result publishLEDInfo(@RequestBody TextDisplayParamVo textDisplayParamVo) throws IOException {
        EllLedInformationVo ellLedInformationVo = new EllLedInformationVo();
        ellLedInformationVo.setId(textDisplayParamVo.getInfoId());
        ellLedInformationVo.setInfoType(2);
        List<EllLedInformationVo> ellLedInformationVoList = infoService.selLedInfo(ellLedInformationVo);
        for (int i = 0; i < ellLedInformationVoList.size(); i++) {
            textDisplayParamVo.setContent(ellLedInformationVoList.get(i).getContent());
        }
        Method method = new Method();
        method.connect(
                "www.listentech.com.cn", 9035, 1, "admin", "admin");
        String[] deviceId = null;
        if (textDisplayParamVo.getDeviceIds().contains(",")) {
            deviceId = textDisplayParamVo.getDeviceIds().split(",");
        } else {
            deviceId = new String[1];
            deviceId[0] = textDisplayParamVo.getDeviceIds();
        }
        List<EllDeviceIdAndNum> ellDeviceIdAndNumList = infoService.selDeviceId(deviceId, 3);
        for (int i = 0; i < ellDeviceIdAndNumList.size(); i++) {
            deviceId[i] = ellDeviceIdAndNumList.get(i).getDeviceId();
        }
        Map<String, String> map = LEDLargeScreenUtil.sendText(method, textDisplayParamVo, deviceId, 1);
        //发送失败的设备编号
        String deviceNumber = "";
        List<EllLedPublishInfo> ellLedPublishInfoList = new ArrayList<>();
        for (int i = 0; i < ellDeviceIdAndNumList.size(); i++) {
            //判断那个设备发送成功
            if (map.get(ellDeviceIdAndNumList.get(i).getDeviceId()) != null && map.get(ellDeviceIdAndNumList.get(i).getDeviceId()).equals("RESULT_OK")) {
                EllLedPublishInfo ellLedPublishInfo = new EllLedPublishInfo();
                ellLedPublishInfo.setLedInfoId(textDisplayParamVo.getInfoId());
                ellLedPublishInfo.setCreateTime(CreateTime.getTime());
                ellLedPublishInfo.setType(2);
                ellLedPublishInfo.setContent(JSONObject.toJSON(textDisplayParamVo).toString());
                ellLedPublishInfo.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                ellLedPublishInfoList.add(ellLedPublishInfo);
            } else {
                if (i == 0) {
                    deviceNumber = ellDeviceIdAndNumList.get(i).getDeviceId();
                } else {
                    deviceNumber = deviceNumber + "/" + ellDeviceIdAndNumList.get(i).getDeviceId();
                }
            }
        }
        //关闭连接
        method.disConnect();
        if (ellLedPublishInfoList.size() > 0) {
            infoService.addPublishLedInfo(ellLedPublishInfoList);
        }
        if (deviceNumber != "") {
            return new Result(HttpStatus.BAD_REQUEST, "部分设备未发送成功这些设备请重试" + deviceNumber);
        }
        return new Result(HttpStatus.SUCCESS, "成功");
    }

    //发布的所有设备的id
    private String[] AllIssueDeviceId(EllInformationVo ellInformation) {
        String[] deviceArray = null;
        if (ellInformation.getDeviceIds() != null && ellInformation.getDeviceIds() != "") {
            if (!ellInformation.getDeviceIds().contains(",")) {
                deviceArray = new String[1];
                deviceArray[0] = ellInformation.getDeviceIds();
                if (ellInformation.getDeviceIds() == null || ellInformation.getDeviceIds() == "") {
                    deviceArray[0] = "";
                }
            } else {
                deviceArray = ellInformation.getDeviceIds().split(",");
            }
        }
        return deviceArray;
    }

    //判断设备是否在线
    private Map<String, String> isOnlineDevice(String deviceArray[]) {
        String deviceIdErro = null;
        String deviceIdSuccess = null;
        List<EllDeviceIdAndNum> ellDeviceIdAndNumList = infoService.selDeviceId(deviceArray, 0);
        //判断设备是否在线
        if (ellDeviceIdAndNumList != null) {
            for (int i = 0; i < ellDeviceIdAndNumList.size(); i++) {
                Boolean b = deviceRService.selOnDeviceCount(ellDeviceIdAndNumList.get(i).getDeviceId());
                if (!b) {
                    if (i == 0) {
                        deviceIdErro = ellDeviceIdAndNumList.get(i).getDeviceName();
                    } else {
                        deviceIdErro = deviceIdErro + "/" + ellDeviceIdAndNumList.get(i).getDeviceName();
                    }
                } else {
                    if (deviceIdSuccess == null) {
                        deviceIdSuccess = ellDeviceIdAndNumList.get(i).getId();
                    } else {
                        deviceIdSuccess = deviceIdSuccess + "," + ellDeviceIdAndNumList.get(i).getId();
                    }
                }
            }
        }
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("error", deviceIdErro);
        stringMap.put("success", deviceIdSuccess);
        return stringMap;
    }

}
