package com.eluolang.platform.controller;

import act.Method;
import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.pojo.EllDevice;
import com.eluolang.common.core.pojo.EllDeviceAlarm;
import com.eluolang.common.core.pojo.EllElectricQuantity;
import com.eluolang.common.core.pojo.EllLargeScreen;
import com.eluolang.common.core.web.Result;
import com.eluolang.common.log.annotation.Log;
import com.eluolang.common.log.constant.OperType;
import com.eluolang.platform.redis.DeviceRService;
import com.eluolang.platform.service.DeviceService;
import com.eluolang.platform.util.FFmpegUtil;
import com.eluolang.platform.util.GetIp;
import com.eluolang.platform.util.LEDLargeScreenUtil;
import com.eluolang.platform.util.VoiceUtil;
import com.eluolang.platform.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "设备管理")
@RestController
@Slf4j
@Transactional
public class DeviceController {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceRService deviceRService;
    @Value("${url.ffmpeg}")
    private String ip;

    @Log(title = "添加信息", operType = OperType.INSERT, content = "添加信息")
    @ApiOperation("添加信息")
    @PostMapping("/addDevice")
    public Result addDevice(@RequestBody EllLargeScreen ellDevice) throws Exception {
       /* Boolean b = deviceRService.selOnDeviceCount(ellDevice.getDeviceId());
        if (!b && ellDevice.getDeviceType() != 3) {
            return new Result(HttpStatus.ERROR, "没有这个设备/此设备异常");
        }*/
        EllLargeScreen selSameDevice = new EllLargeScreen();
        selSameDevice.setDeviceId(ellDevice.getDeviceId());
        List<EllDeviceVo> ellDeviceVoList = deviceService.queryAllByLimit(selSameDevice);
        if (ellDeviceVoList != null && ellDeviceVoList.size() > 0) {
            return new Result(HttpStatus.ALREADY_EXISTS, "设备已经存在");
        }
        return new Result(HttpStatus.SUCCESS, "成功", deviceService.insertDevice(ellDevice));
    }

    /**
     * 视频地址
     */
    @ApiOperation("查询视频")
    @GetMapping("/selVideo")
    public Result selVideo(String deviceId) throws Exception {
        FFmpegUtil fFmpegUtil = new FFmpegUtil(deviceId, deviceService.selRtsp(deviceId), ip);
        Thread thread = new Thread(fFmpegUtil);
        thread.start();
        return new Result(HttpStatus.SUCCESS, "连接成功");
    }

    /**
     * 更新数据
     *
     * @param ellDevice 实例对象
     * @return 实例对象
     */
    @Log(title = "更新设备数据", operType = OperType.UPDATE, content = "更新设备数据")
    @ApiOperation("更新设备数据")
    @PutMapping("/upDevice")
    public Result upDevice(@RequestBody EllLargeScreen ellDevice) throws Exception {
        Boolean b = deviceRService.selOnDeviceCount(ellDevice.getDeviceId());
        if (!b && ellDevice.getDeviceType() != 3) {
            return new Result(HttpStatus.ERROR, "没有这个设备/此设备异常");
        }
        return new Result(HttpStatus.SUCCESS, "成功", deviceService.updateDevice(ellDevice));
    }

    /**
     * 查询设备
     */
    @ApiOperation("查询设备")
    @PostMapping("/selDevice")
    public Result selDevice(@RequestBody EllLargeScreen ellDevice, int page) throws IOException {
        if (page >= 0) {
            PageHelper.startPage(page, 10);
        }
        List<EllDeviceVo> ellDeviceVoList = deviceService.queryAllByLimit(ellDevice);
        //查询设备环境
        ellDeviceVoList = deviceRService.selDeviceEnvironment(ellDeviceVoList);
        PageInfo info = new PageInfo<>(ellDeviceVoList);
        for (int i = 0; i < ellDeviceVoList.size(); i++) {
            //查询是否异常
            ellDeviceVoList.get(i).setIsError(deviceRService.selOnDeviceCount(ellDeviceVoList.get(i).getDeviceId()));
            //查询是否开机
            ellDeviceVoList.get(i).setIsOpen(deviceRService.selIsOpen(ellDeviceVoList.get(i).getDeviceId()));
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("device", ellDeviceVoList);
        jsonObject.put("size", info.getTotal());
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    /**
     * 查询设备发布的信息
     *
     * @param deviceId 实例对象
     * @return 实例对象
     */
    @ApiOperation("查询设备发布的信息(后台)")
    @GetMapping("/selDeviceInfo")
    public Result selDeviceInfo(String deviceId, int page) {
        if (page >= 0) {
            PageHelper.startPage(page, 10);
        }
        List<EllDeviceInfoVo> ellDeviceInfoVoList = deviceService.findDeviceInfo(deviceId);
        PageInfo info = new PageInfo<>(ellDeviceInfoVoList);
        //去除infoId为空的
        List<EllDeviceInfoVo> ellDeviceInfoList = new ArrayList<>();
        try {
            if (ellDeviceInfoVoList != null && ellDeviceInfoVoList.size() > 0)
                ellDeviceInfoVoList.stream().forEach(ellDeviceInfoVo -> {
                    if (ellDeviceInfoVo.getInfoId() != null && !ellDeviceInfoVo.getInfoId().equals("")) {
                        ellDeviceInfoList.add(ellDeviceInfoVo);
                    }
                });
        } catch (Exception e) {

        }

        //查询新闻文件
        if (ellDeviceInfoList != null && ellDeviceInfoList.size() > 0) {
            ellDeviceInfoList.stream().forEach(ellDeviceInfo -> ellDeviceInfo.setFileList(deviceService.selFileUrl(ellDeviceInfo.getInfoId())));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("infoVoList", ellDeviceInfoList);
        jsonObject.put("size", info.getTotal());
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    /**
     * 查询设备发布的信息
     *
     * @param deviceId 实例对象
     * @return 实例对象
     */
    @ApiOperation("查询设备发布的信息(设备)")
    @GetMapping("/selDeviceInfoDe")
    public Result selDeviceInfoDe(String deviceId) {
        EllLargeScreen ellDevice = new EllLargeScreen();
        ellDevice.setDeviceId(deviceId);
        List<EllDeviceVo> ellDeviceList = deviceService.queryAllByLimit(ellDevice);
        if (ellDeviceList == null || ellDeviceList.size() <= 0) {
            return new Result(HttpStatus.SUCCESS, "未查询到此设备");
        }
        List<EllDeviceInfoVo> ellDeviceInfoVoList = deviceService.findDeviceInfo(ellDeviceList.get(0).getId());
        //查询新闻文件
        if (ellDeviceInfoVoList != null && ellDeviceInfoVoList.size() > 0 && ellDeviceInfoVoList.get(0) != null) {
            ellDeviceInfoVoList.stream().forEach(ellDeviceInfoVo -> ellDeviceInfoVo.setFileList(deviceService.selFileUrl(ellDeviceInfoVo.getInfoId())));
        } else {
            ellDeviceInfoVoList = null;
        }
     /*   JSONObject jsonObject = new JSONObject();
        jsonObject.put("infoVoList", ellDeviceInfoVoList);*/
        return new Result(HttpStatus.SUCCESS, "成功", ellDeviceInfoVoList);
    }

    /**
     * 待机/开机
     */
    @Log(title = "待机/开机", operType = OperType.UPDATE, content = "待机/开机")
    @ApiOperation("待机/开机")
    @PutMapping("/closeOpen")
    public Result closeOpen(String deviceNum, int isOpen) throws Exception {
        return new Result(HttpStatus.SUCCESS, "成功", deviceService.closeOpen(deviceNum, isOpen));
    }

    /**
     * 删除设备
     */
    @Log(title = "删除设备", operType = OperType.DELETE, content = "删除设备")
    @ApiOperation("删除设备")
    @DeleteMapping("/delDevice")
    public Result delDevice(String id, String deviceId/*, int deviceType*/) throws Exception {
     /*   Boolean b = deviceRService.selOnDeviceCount(deviceId);
        if (!b && deviceType != 3) {
            return new Result(HttpStatus.ERROR, "没有这个设备/此设备异常");
        }*/
        return new Result(HttpStatus.SUCCESS, "成功", deviceService.deleteDevice(id, deviceId, 0/*deviceType*/));
    }

    @Log(title = "添加电量", operType = OperType.INSERT, content = "添加电量")
    @ApiOperation("添加电量")
    @PostMapping("/addElectricity")
    public Result addElectricity(@RequestBody EllElectricQuantity ellElectricQuantity) {
        return new Result(HttpStatus.SUCCESS, "成功", deviceService.addElectricity(ellElectricQuantity));
    }

    @Log(title = "添加警报", operType = OperType.INSERT, content = "添加警报")
    @ApiOperation("添加警报")
    @PostMapping("/addAlarm")
    public Result addAlarm(@RequestBody EllDeviceAlarm ellDeviceAlarm) {
        return new Result(HttpStatus.SUCCESS, "成功", deviceService.addAlarm(ellDeviceAlarm));
    }

    @ApiOperation("查询坐标")
    @GetMapping("/selLocation")
    public Result selLocation(int createBy, int deviceType) {
        return new Result(HttpStatus.SUCCESS, "成功", deviceService.selLocation(createBy, deviceType));
    }

    @Log(title = "厕所保存环境", operType = OperType.INSERT, content = "厕所保存环境")
    @ApiOperation("厕所保存环境")
    @PostMapping("/addToiletEnvironmentVo")
    public Result ToiletEnvironmentVo(@RequestBody EllToiletEnvironmentVo ellToiletEnvironmentVo) {
        deviceRService.addEnvironment(ellToiletEnvironmentVo.getDeviceId(), ellToiletEnvironmentVo);
        return new Result(HttpStatus.SUCCESS, "成功");
    }

    //写死
    @ApiOperation("给厕所返回温度信息")
    @PostMapping("/selEnvironmentInfo")
    public Result selEnvironmentInfo() {
        EllLargeScreen ellDevice = new EllLargeScreen();
        ellDevice.setDeviceType(1);
        List<EllDeviceVo> deviceVoList = deviceService.queryAllByLimit(ellDevice);
        List<DeviceEnvironmentParamDtoVo> deviceEnvironmentParamDtoVoList = null;
        DeviceEnvironmentParamDtoVo deviceEnvironmentParamDtoVo = null;
        if (deviceVoList == null && deviceVoList.size() <= 0) {
            return new Result(HttpStatus.SUCCESS, "成功", deviceEnvironmentParamDtoVo);
        }
        for (int i = 0; i < deviceVoList.size(); i++) {
            Boolean b = deviceRService.selOnDeviceCount(deviceVoList.get(i).getDeviceId());
            if (b) {
                List<EllDeNameAndId> ellDeNameAndIdList = new ArrayList<>();
                EllDeNameAndId ellDeNameAndId = new EllDeNameAndId(deviceVoList.get(i).getDeviceName(), deviceVoList.get(i).getDeviceId(), 0);
                ellDeNameAndIdList.add(ellDeNameAndId);
                deviceEnvironmentParamDtoVoList = deviceRService.selEnvironment(ellDeNameAndIdList, 1);
                break;
            }
        }
/*        deviceEnvironmentParamDtoVo.setDeviceName("sawdasd");
        deviceEnvironmentParamDtoVo.setParticles("545");
        deviceEnvironmentParamDtoVo.setNoise("545");
        deviceEnvironmentParamDtoVo.setHumidity("sddss");
        deviceEnvironmentParamDtoVo.setCount(0);
        deviceEnvironmentParamDtoVo.setTemperature("sadsadas");
        deviceEnvironmentParamDtoVo.setIlluminance("564564");*/
        if (deviceEnvironmentParamDtoVoList != null) {
            deviceEnvironmentParamDtoVo = deviceEnvironmentParamDtoVoList.get(0);
        }
        return new Result(HttpStatus.SUCCESS, "成功", deviceEnvironmentParamDtoVo);
    }

    @ApiOperation("语音通话")
    @GetMapping("/callVoice")
    public Result callVoice(String deviceId, HttpServletRequest request) {
        EllLargeScreen ellDevice = new EllLargeScreen();
        ellDevice.setDeviceId(deviceId);
        //查询设备
        List<EllDeviceVo> ellDeviceVoList = deviceService.queryAllByLimit(ellDevice);
        VoiceUtil.address = GetIp.getIp(request);
        if (ellDeviceVoList != null && ellDeviceVoList.size() > 0) {
            VoiceUtil.deviceAddress = ellDeviceVoList.get(0).getDeviceIp();
        }
        return new Result(HttpStatus.SUCCESS, "成功");
    }

    @ApiOperation("清屏")
    @PostMapping("/clearScreen")
    public Result clearScreen(String deviceId) {
        String[] deviceIds = new String[1];
        deviceIds[0] = deviceId;
        Method method = null;
        method.connect(
                "www.listentech.com.cn", 9035, 1, "admin", "admin");
        return new Result(HttpStatus.SUCCESS, "成功", LEDLargeScreenUtil.clearScreen(deviceIds, method));
    }

    /**
     * 关闭播放请求
     *
     * @param deviceId 摄像头id
     * @return rtmp地址
     */
//    @PreAuthorize(hasPermi = "surveillance:show:show")
    @GetMapping("/newClose")
    public Result newClose(String deviceId) {
        ///Camera camera = cameraService.selCameraById(id);
        if (StringUtils.isBlank(deviceId)) {
            log.error("CameraController===newPlay==参数异常空");
            return new Result(HttpStatus.SUCCESS, "参数为空", Boolean.FALSE);
        }
        Boolean ret = deviceService.newClose(deviceId);
        return new Result(HttpStatus.SUCCESS, "", ret);
    }
}
