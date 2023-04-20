package com.eluolang.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.hardware.dto.CancelInfoDeliveryDto;
import com.eluolang.common.core.hardware.dto.InfoDeliveryDto;
import com.eluolang.common.core.pojo.EllInfoFile;
import com.eluolang.common.core.pojo.EllInfoPublishTarget;
import com.eluolang.common.core.pojo.EllInformation;
import com.eluolang.common.core.pojo.EllLedPublishInfo;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.common.core.web.Result;
import com.eluolang.platform.fegin.TcpServerRemoteService;
import com.eluolang.platform.mapper.InfoMapper;
import com.eluolang.platform.redis.DeviceRService;
import com.eluolang.platform.service.InfoService;
import com.eluolang.platform.util.CreateTime;
import com.eluolang.platform.util.InfoIssueUtil;
import com.eluolang.platform.util.PathGeneration;
import com.eluolang.platform.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;


@Transactional
@Service
public class InfoServiceImpl implements InfoService {
    @Autowired
    private InfoMapper infoMapper;
    @Autowired
    private TcpServerRemoteService tcpServerRemoteService;
    @Autowired
    private DeviceRService deviceRService;

    @Override
    public int addInfo(EllInformationVo ellInformationVo) throws IOException {
        ellInformationVo.setCreateTime(CreateTime.getTime());
        ellInformationVo.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
        if (ellInformationVo.getInfoType() != 3) {
            if (ellInformationVo.getFileIds() == null && ellInformationVo.getFileIds().size() == 0) {
                return -1;
            }
            //所有文件绑定发布信息
            List<EllInfoFile> ellInfoFileList = new ArrayList<>();
            for (int i = 0; i < ellInformationVo.getFileIds().size(); i++) {
                //文件绑定添加的信息
                EllInfoFile ellInfoFile = new EllInfoFile();
                ellInfoFile.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                ellInfoFile.setFileId(ellInformationVo.getFileIds().get(i));
                ellInfoFile.setInfoId(ellInformationVo.getId());
                ellInfoFile.setIntervalTime(ellInformationVo.getIntervalTime());
                ellInfoFileList.add(ellInfoFile);
            }
            //文件与信息绑定
            infoMapper.insertBatchInfoFile(ellInfoFileList);
        }
        return infoMapper.addInfo(ellInformationVo);
    }

    @Override
    public int update(EllInformationVo ellInformationVo) throws IOException {
        switch (ellInformationVo.getInfoType()) {
            //视频和文字都没间隔时间
            case 2:
            case 3:
                ellInformationVo.setIntervalTime(-1);
                break;
        }
        //视频和文字都为空
        if (ellInformationVo.getInfoType() != 3) {
            ellInformationVo.setText("");
        }
        ellInformationVo.setUpdateTime(CreateTime.getTime());
        //先删除文件绑定
        infoMapper.delInfoFile(ellInformationVo.getId());
        if (ellInformationVo.getInfoType() != 3) {
            if (ellInformationVo.getFileIds() == null && ellInformationVo.getFileIds().size() == 0) {
                return -1;
            }
            //修改文件绑定发布信息
            List<EllInfoFile> ellInfoFileList = new ArrayList<>();
            for (int i = 0; i < ellInformationVo.getFileIds().size(); i++) {
                //文件绑定添加的信息
                EllInfoFile ellInfoFile = new EllInfoFile();
                ellInfoFile.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                ellInfoFile.setFileId(ellInformationVo.getFileIds().get(i));
                ellInfoFile.setInfoId(ellInformationVo.getId());
                ellInfoFile.setIntervalTime(ellInformationVo.getIntervalTime());
                ellInfoFileList.add(ellInfoFile);
            }
            //文件与信息绑定
            infoMapper.insertBatchInfoFile(ellInfoFileList);
        }
        return infoMapper.updateInfo(ellInformationVo);
    }

    @Override
    public List<EllSelInfoVo> findInfo(EllInformation ellInformation) {
        return infoMapper.findInfo(ellInformation);
    }

    @Override
    public List<EllInfoPublishTarget> selPublishDevice(String infoId) {
        return infoMapper.selPublishDevice(infoId);
    }

    @Override
    public int upInfoState(EllInformationVo ellInformationVo, String deviceArray[]) throws Exception {

        //查询设备路灯id
        List<EllDeviceIdAndNum> lampDeviceIds = null;
        //厕所设备id
        List<EllDeviceIdAndNum> toiletDeviceIds = null;
        //查询文件地址
        List<EllInfoFileVo> fileList = null;
        //查询要发布的信息和要删除的信息
        List<EllSelInfoVo> ellSelInfoVoList = null;
        //记录每个设备的发布顺序
        Map<String, Integer> deviceOrder = new HashMap<>();
        //所有选择设备deviceId
        List<String> allDeviceId = new ArrayList<>();
        //查询所有选择设备的id
        List<String> allId = new ArrayList<>();
        if (deviceArray != null && deviceArray.length > 0) {
            //查询设备路灯id
            lampDeviceIds = infoMapper.selDeviceId(deviceArray, 1);
            //选择设备deviceId
            lampDeviceIds.stream().forEach(lampDeviceId -> allDeviceId.add(lampDeviceId.getDeviceId()));
            //选择设备的id
            lampDeviceIds.stream().forEach(lampDeviceId -> allId.add(lampDeviceId.getId()));
            //厕所设备id
            toiletDeviceIds = infoMapper.selDeviceId(deviceArray, 2);
            //选择设备deviceId
            toiletDeviceIds.stream().forEach(toiletDeviceId -> allDeviceId.add(toiletDeviceId.getDeviceId()));
            //选择设备的id
            toiletDeviceIds.stream().forEach(toiletDeviceId -> allId.add(toiletDeviceId.getId()));
            //查询文件地址
            fileList = infoMapper.selFile(ellInformationVo.getId());
            //查询要发布的信息
            EllInformation ellInformation = new EllInformation();
            ellInformation.setId(ellInformationVo.getId());
            ellSelInfoVoList = infoMapper.findInfo(ellInformation);
        }
        //查询设备已经发布的厕所id
//        List<EllDeviceIdAndNum> toiletDeviceDel = infoMapper.selDeviceByInfo(ellInformationVo.getId(), 2);
        //查询设备已经发布的路灯id
//        List<EllDeviceIdAndNum> lampDeviceIdDel = infoMapper.selDeviceByInfo(ellInformationVo.getId(), 1);
        if (ellSelInfoVoList != null && ellSelInfoVoList.size() == 0) {
            return -2;
        }
        //2是发布3是结束，都要去删除信息发布设备
        switch (ellInformationVo.getState()) {
            case 2:
               /* //发布时候先删除这条信息发布现在没有的设备确保发布信息是选择的设备

                if (toiletDeviceDel != null && lampDeviceIdDel != null) {
                    //先通知拥有这条信息发布的设备先把这条信息进行删除
                    delDeviceInfo(ellInformationVo, ellSelInfoVoList, toiletDeviceDel, fileList, lampDeviceIdDel);
                }
                //删除数据库相关设备发布信息
                delInfoDevice(ellInformationVo, allId);*/
                //删除数据库相关设备发布信息
                infoMapper.deleteByInfoId(ellInformationVo.getId(), null);
                List<EllInfoPublishTarget> ellInfoPublishTargetList = new ArrayList<>();
                //没有发布的设备就不发布
                if (deviceArray != null && deviceArray.length > 0) {
                    for (int i = 0; i < allId.size(); i++) {
                        EllInfoPublishTarget ellInfoPublishTarget = new EllInfoPublishTarget();
                        ellInfoPublishTarget.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                        ellInfoPublishTarget.setDeviceId(allId.get(i));
                        ellInfoPublishTarget.setReleaseTime(CreateTime.getTime());
                        ellInfoPublishTarget.setInfoId(ellInformationVo.getId());
                        //设置该新闻在该设备的发布顺序
                        int order = infoMapper.findInfoOrder(allId.get(i)) + 1;
                        if (order > 10) {
                            return -4;
                        }
                        ellInfoPublishTarget.setReleaseOrder(order);
                        //文字只能发一条
                        if (ellSelInfoVoList.get(0).getInfoType() == 3 && ellInfoPublishTarget.getReleaseOrder() > 1) {
                            return -3;
                        }
                        //保存顺序
                        deviceOrder.put(ellInfoPublishTarget.getDeviceId(), ellInfoPublishTarget.getReleaseOrder());
                        ellInfoPublishTargetList.add(ellInfoPublishTarget);
                    }
                    //在添加
                    if (ellInfoPublishTargetList != null && ellInfoPublishTargetList.size() > 0) {
                        infoMapper.insertBatch(ellInfoPublishTargetList);
                    }
                    int i = infoMapper.updateInfo(ellInformationVo);
                    if (i > 0 && ellInformationVo.getDeviceIds() != null && ellInformationVo.getDeviceIds() != "") {
                        //信息发布路灯数据组装
                        List<InfoDeliveryDto> infoDeliveryDtoList = InfoIssueUtil.infoAssemble(deviceOrder, lampDeviceIds, ellSelInfoVoList, fileList);
                        if (infoDeliveryDtoList != null) {
                            for (int j = 0; j < infoDeliveryDtoList.size(); j++) {
                                System.out.println(infoDeliveryDtoList.get(j));
                                tcpServerRemoteService.infoDelivery(infoDeliveryDtoList.get(j));
                            }
                        }
                        if (toiletDeviceIds != null && toiletDeviceIds.size() > 0) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("cmd", 10);
                            jsonObject.put("infoId", ellInformationVo.getId());
                            for (int j = 0; j < toiletDeviceIds.size(); j++) {
                                tcpServerRemoteService.sendTo(jsonObject.toJSONString(), toiletDeviceIds.get(j).getDeviceId());
                            }
                        }
                    }
                }
                return 1;
            case 3:
                //判断是单个设备撤销信息还，删除这条信息
                if (ellInformationVo.getIsDelete() == null || ellInformationVo.getIsDelete() == 0) {
                    //删除
                    infoMapper.deleteByInfoId(ellInformationVo.getId(), ellInformationVo.getDeviceIds());
                } else {
                    //删除信息发布的所有设备
                    delInfoDevice(ellInformationVo, allId);

                }
                //通知相关设备删除这条信息的文件
                delDeviceInfo(ellInformationVo, ellSelInfoVoList, toiletDeviceIds, fileList, lampDeviceIds);
                return infoMapper.updateInfo(ellInformationVo);
        }
        return -1;
    }

    @Override
    public List<EllDeviceIdAndNum> selDeviceByInfo(String infoId, int deviceType) {
        return infoMapper.selDeviceByInfo(infoId, deviceType);
    }

    /**
     * @param ellInformationVo
     * @param allDeviceId      设备的deviceId
     */
    public void delInfoDevice(EllInformationVo ellInformationVo, List<String> allDeviceId) {
        //查询设备已经发布的id
        List<EllInfoPublishTarget> ellInfoPublishTargets = infoMapper.selPublishDevice(ellInformationVo.getId());
        if (ellInfoPublishTargets != null && ellInfoPublishTargets.size() > 0) {
            Map<String, EllInfoPublishTarget> ellInfoPublishTargetMap = new HashMap<>();
            ellInfoPublishTargets.stream().forEach(ellInfoPublishTarget -> ellInfoPublishTargetMap.put(ellInfoPublishTarget.getDeviceId(), ellInfoPublishTarget));
            //删除这次发布有的设备避免把发布的设备也删除了
            if (allDeviceId != null) {
                allDeviceId.stream().forEach(deviceId -> ellInfoPublishTargetMap.remove(deviceId));
            }
            //发布时候先删除这条信息发以前布现在没有的设备，确保发布信息是选择的设备
            for (String deviceId : ellInfoPublishTargetMap.keySet()) {
                infoMapper.deleteByInfoId(ellInformationVo.getId(), deviceId);
            }
        }
    }

    public void delDeviceInfo(EllInformationVo ellInformationVo, List<EllSelInfoVo> ellSelInfoVoList, List<EllDeviceIdAndNum> toiletDeviceIds, List<EllInfoFileVo> fileList, List<EllDeviceIdAndNum> lampDeviceIds) throws Exception {
        //删除发布的这条信息的设备
        List<CancelInfoDeliveryDto> cancelInfoDeliveryDtoList = null;
        if (ellSelInfoVoList != null && ellSelInfoVoList.size() > 0 && lampDeviceIds != null && lampDeviceIds.size() > 0) {
            //数据组装
            cancelInfoDeliveryDtoList = InfoIssueUtil.infoRevocation(lampDeviceIds, ellSelInfoVoList, fileList);
        }
        //通知路灯设备删除信息
        if (cancelInfoDeliveryDtoList != null) {
            for (int j = 0; j < cancelInfoDeliveryDtoList.size(); j++) {
                System.out.println(cancelInfoDeliveryDtoList.get(j));
                tcpServerRemoteService.cancelInfoDelivery(cancelInfoDeliveryDtoList.get(j));
            }
        }
        //通知厕所设备删除信息
        if (toiletDeviceIds != null && toiletDeviceIds.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cmd", 15);
            jsonObject.put("infoId", ellInformationVo.getId());
            for (int j = 0; j < toiletDeviceIds.size(); j++) {
                tcpServerRemoteService.sendTo(jsonObject.toJSONString(), toiletDeviceIds.get(j).getDeviceId());
            }
        }
    }

    @Override
    public List<EllFIleVo> selFileUrl(String infoId) {
        return infoMapper.selFileUrl(infoId);
    }

    @Override
    public int delInfoDevice(String infoId) throws Exception {
        //删除信息
        EllInformationVo ellInformation = new EllInformationVo();
        ellInformation.setState(3);
        ellInformation.setIsDelete(1);
        ellInformation.setId(infoId);
        //查询信息发布设备id
        List<EllInfoPublishTarget> ellInfoPublishTargetList = selPublishDevice(infoId);
        if (ellInfoPublishTargetList != null && ellInfoPublishTargetList.size() > 0) {
            String deviceIds = "";
            for (int i = 0; i < ellInfoPublishTargetList.size(); i++) {
                if (i == 0) {
                    deviceIds = ellInfoPublishTargetList.get(i).getDeviceId();
                } else {
                    deviceIds = deviceIds + "," + ellInfoPublishTargetList.get(i).getDeviceId();
                }
            }
            ellInformation.setDeviceIds(deviceIds);
        }
        String deviceArray[] = null;
        //当只有一个设备号的时候就直接设置
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
        //通知设备删除信息
        int i = upInfoState(ellInformation, deviceArray);
        if (i < 0) {
            return i;
        }
        //删除信息发布的设备
        return infoMapper.delInfoDevice(infoId);
    }

    @Override
    public EllAddFileVo uploadImage(int infoType, MultipartFile files) throws IOException {
        EllAddFileVo ellAddFileVo = new EllAddFileVo();
        ellAddFileVo.setInfoId(new Date().getTime() + IdUtils.fastSimpleUUID());
        String size = saveFile(infoType, files);
        if (size == null) {
            return null;
        }
        ellAddFileVo.setFileIds(size);
        return ellAddFileVo;
    }

    @Override
    public InfoDeliveryFileVo selInfoDeliveryFile(String deviceId, String infoId) {
        return infoMapper.selInfoDeliveryFile(deviceId, infoId);
    }

    @Override
    public List<DeliveryFileVo> selFileByInfoId(String infoId) {
        return infoMapper.selFileByInfoId(infoId);
    }

    @Override
    public int insertLedInfo(EllLedInformationVo ellLedInformationVo) {
        ellLedInformationVo.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
        ellLedInformationVo.setCreateTime(CreateTime.getTime());
        return infoMapper.insertLedInfo(ellLedInformationVo);
    }

    @Override
    public int updateLED(EllLedInformationVo ellLedInformationVo) {
        ellLedInformationVo.setCreateBy(ellLedInformationVo.getCreateTime());
        return infoMapper.updateLED(ellLedInformationVo);
    }

    @Override
    public List<EllLedInformationPublishVo> selLedPublish(EllLedInformationPublishVo ellLedInformationPublishVo) {
        return infoMapper.selLedPublish(ellLedInformationPublishVo);
    }

    @Override
    public List<EllLedInformationVo> selLedInfo(EllLedInformationVo ellLedInformationVo) {
        return infoMapper.selLedInfo(ellLedInformationVo);
    }

    @Override
    public List<EllDeviceIdAndNum> selDeviceId(String[] deviceArray, int deviceType) {
        return infoMapper.selDeviceId(deviceArray, deviceType);
    }

    @Override
    public String selFileLed(String fileId) {
        return infoMapper.selFileLed(fileId);
    }

    @Override
    public int delLedInfo(String ledInfoId, String deviceId) {
        return infoMapper.delLedInfo(ledInfoId, deviceId);
    }

    @Override
    public int addPublishLedInfo(List<EllLedPublishInfo> ellLedPublishInfoList) {
        return infoMapper.addPublishLedInfo(ellLedPublishInfoList);
    }


    public String saveFile(int infoType, MultipartFile files) throws IOException {
        String fileIds = "";
        //所有文件信息
        List<EllFIleVo> fIleVoList = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            //文件大小不能超过210m
            if (files.getSize() > 219715200) {
                return null;
            }
            String path = "";
            String[] type = files.getContentType().toString().split("/");
            String folder = "";
            switch (infoType) {
                case 1:
                    folder = "image";
                    break;
                case 2:
                    folder = "video";
            }
            //把文件写入到目标位置
            String fileName = new Date().getTime() + UUID.randomUUID().toString();
            if (FileUploadUtil.isLinux() == false) {
                path = WindowsSite.FILE_INFO_PATH + folder + "\\" + fileName + "." + type[1];
            } else {
                path = LinuxSite.FILE_INFO_PATH + folder + "/" + fileName + "." + type[1];
            }
            File file = new File(path);
            //路径生成
            PathGeneration.createPath(file);
            //文件保存
            files.transferTo(file);
            String url = "/" + folder + "/" + fileName + "." + type[1];
            //文件信息
            EllFIleVo ellFIleVo = new EllFIleVo();
            ellFIleVo.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
            ellFIleVo.setSize(file.length());
            ellFIleVo.setFileName(fileName);
            ellFIleVo.setFileUrl(url);
            ellFIleVo.setType("1");
            ellFIleVo.setUploadTime(CreateTime.getTime());
            fIleVoList.add(ellFIleVo);
            //返回文件id
            fileIds = ellFIleVo.getId();
        }

        //保存文件地址
        infoMapper.uploadImage(fIleVoList);

        return fileIds;
    }
}
