package com.eluolang.platform.service.impl;

import act.Method;
import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.hardware.dto.InfoDeliveryDto;
import com.eluolang.common.core.hardware.dto.OnOffDeviceDto;
import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.common.core.web.Result;
import com.eluolang.platform.fegin.TcpServerRemoteService;
import com.eluolang.platform.mapper.DeviceMapper;
import com.eluolang.platform.redis.DeviceRService;
import com.eluolang.platform.service.DeviceService;
import com.eluolang.platform.util.*;
import com.eluolang.platform.vo.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private DeviceRService deviceRService;
    @Autowired
    private TcpServerRemoteService tcpServerRemoteService;

    @Override
    public int insertDevice(EllLargeScreen ellDevice) throws Exception {
        ellDevice.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
        ellDevice.setCreateTime(CreateTime.getTime());
        //查询上级账号
        List<EllOperator> ellOperatorList = deviceMapper.selOpt(ellDevice.getCreateBy());
        //把权限分配给本和上级账号
        List<EllOperatorRat> ellOperatorRatList = new ArrayList<>();
        if (ellOperatorList != null) {
            for (int i = 0; i < ellOperatorList.size(); i++) {
                EllOperatorRat ellOperatorRat = new EllOperatorRat();
                ellOperatorRat.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                ellOperatorRat.setRat(ellDevice.getId());
                ellOperatorRat.setModule(2);
                ellOperatorRat.setOptId(ellOperatorList.get(i).getId());
                ellOperatorRatList.add(ellOperatorRat);
            }
            if (ellOperatorList.size() > 0) {
                deviceMapper.addJurisdiction(ellOperatorRatList);
            }
        }
        if (ellDevice.getActionTime() != null && ellDevice.getActionTime() != "") {
            OnOffDeviceDto ellCloseOpenVo = new OnOffDeviceDto();
            ellCloseOpenVo.setDeviceId(ellDevice.getDeviceId());
            ellCloseOpenVo.setStatus(1);
            ellCloseOpenVo.setStartTime(ellDevice.getActionTime());
            ellCloseOpenVo.setEndTime(ellDevice.getCloseTime());
            tcpServerRemoteService.onOffDevice(ellCloseOpenVo);
        }
        return deviceMapper.insertDevice(ellDevice);
    }

//    @Scheduled(cron = "0 0/10 * * * ?")
   /* @Override
    public void selGarage() throws Exception {
   *//*     //获取车库信息
        ParkListRequest parkListRequest = new ParkListRequest();
        parkListRequest.setParkIndexCodes("");
        ResultVo result = JSONObject.parseObject(ArtemisPostTest.remainGarageNum(parkListRequest), ResultVo.class);
        if (result.msg.equals("success")) {
            Gson gson = new Gson();
            //获取车库信息
            List<EllGarageVo> ellGarageVo = gson.fromJson(String.valueOf(result.getData()), new TypeToken<List<EllGarageVo>>() {
            }.getType());
*//**//*
            ResultVo resultCar = JSONObject.parseObject(ArtemisPostTest.remainSpaceNum(
                    parkListRequest), ResultVo.class);*//**//*
        }*//*
        RemainSpaceNumRequest remainSpaceNumRequest = new RemainSpaceNumRequest();
        remainSpaceNumRequest.setParkSyscode("");
        ResultVo resultCar = JSONObject.parseObject(ArtemisPostTest.remainSpaceNum(
                remainSpaceNumRequest), ResultVo.class);
        System.out.println("1111");
        if (resultCar != null && resultCar.msg.equals("success!")) {
            Gson gson = new Gson();
            List<EllGarageVo> ellGarageVo = gson.fromJson(String.valueOf(resultCar.getData()), new TypeToken<List<EllGarageVo>>() {
            }.getType());
            int trucSpace = deviceRService.selTruckSpaceNum("240305004110343");
            if (trucSpace != Integer.parseInt(ellGarageVo.get(0).getLeftPlace())) {
                //坐标图片
                int coord[][] = {{260, 690}, {1300, 750}};
                LedImage.readImage(coord, ellGarageVo.get(0).getLeftPlace() + ",000");
                DisplayParamVo displayParamVo = new DisplayParamVo();
                displayParamVo.setFileUrl("/home/file/LED/truckSpace.png");
                //displayParamVo.setFileUrl("D:\\EllTestImg\\truckSpace.png");
                displayParamVo.setStayTime(1200);
                displayParamVo.setScreenWidth(320);//屏宽
                displayParamVo.setScreenHeight(208);//屏高
                displayParamVo.setDisplayType(0);
                displayParamVo.setDisplaySpeed(10);
                displayParamVo.setColor(2);//双色
                Method method = new Method();
                String[] deviceId = {
                        "240305004110343"
                };
                if (FileUploadUtil.isLinux() == false) {

                } else {
                    method.connect(
                            "www.listentech.com.cn", 9035, 1, "admin", "admin");
                    Map<String, String> map = LEDLargeScreenUtil.sendImage(method, displayParamVo, deviceId, 1);
                    deviceRService.addTruckSpaceNum("240305004110343", Integer.parseInt(ellGarageVo.get(0).getLeftPlace()));
                }
            }
        }
        //return deviceRService.selTruckSpaceNum("240305004110343");
    }*/

    @Override
    public int updateDevice(EllLargeScreen ellDevice) throws Exception {
        if (ellDevice.getActionTime() != null && ellDevice.getActionTime() != "") {
            OnOffDeviceDto ellCloseOpenVo = new OnOffDeviceDto();
            ellCloseOpenVo.setDeviceId(ellDevice.getDeviceId());
            ellCloseOpenVo.setStatus(1);
            ellCloseOpenVo.setStartTime(ellDevice.getActionTime());
            ellCloseOpenVo.setEndTime(ellDevice.getCloseTime());
            tcpServerRemoteService.onOffDevice(ellCloseOpenVo);
        }
        return deviceMapper.updateDevice(ellDevice);
    }

    @Override
    public List<EllDeviceInfoVo> findDeviceInfo(String deviceId) {
        //查询新闻
        return deviceMapper.findDeviceInfo(deviceId);
    }

    @Override
    public List<String> selFileUrl(String infoId) {
        return deviceMapper.selFileUrl(infoId);
    }

    @Override
    public int closeOpen(String deviceNum, int isOpen) throws Exception {
        OnOffDeviceDto ellCloseOpenVo = new OnOffDeviceDto();
        ellCloseOpenVo.setDeviceId(deviceNum);
        ellCloseOpenVo.setStatus(isOpen);
        ellCloseOpenVo.setStartTime("-1");
        ellCloseOpenVo.setEndTime("-1");
        tcpServerRemoteService.onOffDevice(ellCloseOpenVo);
        return 1;
    }

    @Override
    public int deleteDevice(String id, String deviceId, int deviceType) throws Exception {
        switch (deviceType) {
            case 1:   //通知路灯设备删除信息
                InfoDeliveryDto infoDeliveryDto = new InfoDeliveryDto();
                infoDeliveryDto.setDeviceId(deviceId);
                tcpServerRemoteService.infoDelivery(infoDeliveryDto);
                break;
            case 2:
                //厕所删除信息
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cmd", 25);
                tcpServerRemoteService.sendTo(jsonObject.toJSONString(), deviceId);
                break;
        }
        //删除该设备发布的信息
        deviceMapper.deletePublishTarget(id);
        return deviceMapper.deleteDevice(id);
    }

    @Override
    public List<EllDeviceVo> queryAllByLimit(EllLargeScreen ellDevice) {
        List<EllDeviceVo> ellDeviceVoList = deviceMapper.queryAllByLimit(ellDevice);
        return ellDeviceVoList;
    }

    @Override
    public int addElectricity(EllElectricQuantity ellElectricQuantity) {
        ellElectricQuantity.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
        //拿id
        EllLargeScreen ellDevice = new EllLargeScreen();
        ellDevice.setDeviceId(ellElectricQuantity.getDeviceId());
        ellElectricQuantity.setDeviceId(deviceMapper.queryAllByLimit(ellDevice).get(0).getDeviceId());
        return deviceMapper.addElectricity(ellElectricQuantity);
    }

    @Override
    public int addAlarm(EllDeviceAlarm ellDeviceAlarm) {
        ellDeviceAlarm.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
        //拿id
        EllLargeScreen ellDevice = new EllLargeScreen();
        ellDevice.setDeviceId(ellDeviceAlarm.getDeviceId());
        EllDeviceVo ellDeviceVo = deviceMapper.queryAllByLimit(ellDevice).get(0);
        ellDeviceAlarm.setDeviceId(ellDeviceVo.getId());
        ellDeviceAlarm.setAlarmType(ellDeviceVo.getDeviceType());
        ellDeviceAlarm.setAlarmTime(CreateTime.getTime());
        return deviceMapper.addAlarm(ellDeviceAlarm);
    }

    @Override
    public List<EllDeviceLocationVo> selLocation(int createBy, int deviceType) {
        List<EllDeviceLocationVo> ellDeviceLocationVoList = deviceMapper.selLocation(createBy, deviceType);
        ellDeviceLocationVoList.stream().forEach(ellDeviceLocationVo -> {
            ellDeviceLocationVo.setSite(ellDeviceLocationVo.getLocationXY().split(","));
            ellDeviceLocationVo.setLocationXY(null);
        });
        return ellDeviceLocationVoList;
    }

    @Override
    public String selRtsp(String deviceId) {
        return deviceMapper.selRtsp(deviceId);
    }

    /**
     * 关闭播放
     * @param deviceId
     * @return
     */
    @Override
    public Boolean newClose(String deviceId) {
        destroyCmd(deviceId);
        return Boolean.TRUE;
    }

    @Override
    public void destroyCmd(String app) {
            log.info("执行destroyCmd方法，参数为：app：" + app);
            /** 关闭cmd */
            BufferedReader reader = null;
            try {
                /** 查看所有进程 */
                Process process = Runtime.getRuntime().exec("ps -ef");
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    /** 判断是否为设备id对应的进程 */
                    if (line.contains(app)) {
                        String[] strs = line.split("\\s+");
                        /** 结束进程 */
                        closeByPid(strs[1]);
                    }
                }
            } catch (Exception e) {
                log.error("CameraServiceImpl==destroyCmd==catch=error:" + e.getMessage(), e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        log.error("CameraServiceImpl==destroyCmd==finally=error:" + e.getMessage(), e);
                    }
                }
            }
        }

    public void closeByPid(String pid) {
        log.info("关闭进程，PID为：" + pid);
        BufferedReader reader = null;
        Process process = null;
        try {
            /** 结束进程 */
            process = Runtime.getRuntime().exec("kill -9 " + pid);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        } catch (Exception e) {
            log.error("CameraServiceImpl==closeByPid==catch=error:" + e.getMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    log.error("CameraServiceImpl==closeByPid==finally=error:" + e.getMessage(), e);
                }
            }
        }
    }
}
