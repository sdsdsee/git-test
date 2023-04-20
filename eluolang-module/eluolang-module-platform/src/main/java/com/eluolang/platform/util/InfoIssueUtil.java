package com.eluolang.platform.util;


import com.eluolang.common.core.hardware.dto.CancelInfoDeliveryDto;
import com.eluolang.common.core.hardware.dto.InfoDeliveryDto;

import com.eluolang.common.core.pojo.EllInfoPublishTarget;
import com.eluolang.platform.vo.EllDeviceIdAndNum;
import com.eluolang.platform.vo.EllInfoFileVo;
import com.eluolang.platform.vo.EllSelInfoVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InfoIssueUtil {
    public static List<InfoDeliveryDto> infoAssemble(Map<String, Integer> deviceOrder, List<EllDeviceIdAndNum> deviceIds, List<EllSelInfoVo> ellSelInfoVoList, List<EllInfoFileVo> fileList) {
        List<InfoDeliveryDto> infoDeliveryDtoList = new ArrayList<>();
        for (int i = 0; i < deviceIds.size(); i++) {
            InfoDeliveryDto infoDeliveryDto = new InfoDeliveryDto();
            infoDeliveryDto.setDeviceId(deviceIds.get(i).getDeviceId());
            infoDeliveryDto.setDeliveryType(1);
            infoDeliveryDto.setDeliveryId(ellSelInfoVoList.get(0).getId());
            infoDeliveryDto.setText(ellSelInfoVoList.get(0).getText());
            //获取该设备的发布顺序
            infoDeliveryDto.setInfoPri(deviceOrder.get(deviceIds.get(i).getId()));
            //文件
            List<InfoDeliveryDto.DataBean> dataBeanList = new ArrayList<>();
            //不是文字
            if (ellSelInfoVoList.get(0).getInfoType() != 3) {
                for (int j = 0; j < fileList.size(); j++) {
                    InfoDeliveryDto.DataBean dataBean = new InfoDeliveryDto.DataBean();
                    dataBean.setFileId(fileList.get(j).getFileId());
                    dataBean.setFileName(fileList.get(j).getFileUrl());
                    dataBean.setFileType(ellSelInfoVoList.get(0).getInfoType());
                    dataBean.setTimeInterval(fileList.get(j).getIntervalTime());
                    //内容播放顺序
                    dataBean.setPri(j + 1);
                    dataBeanList.add(dataBean);
                }
                infoDeliveryDto.setFileInfo(dataBeanList);
             /*   int finalI = i;
                //拿取和这个设备id相同的信息发布信息
                ellInfoPublishTargetList.stream().forEach(ellInfoPublishTarget -> {
                    if (ellInfoPublishTarget.getDeviceId().equals(deviceIds.get(finalI).getId())) {


                    }
                });*/
            } /*else {
                InfoDeliveryDto.DataBean dataBean = new InfoDeliveryDto.DataBean();
                dataBean.setFileId(null);
                dataBean.setFileName(null);
                dataBean.setFileType(null);
                dataBean.setTimeInterval(null);
                //获取该设备的发布顺序
                dataBean.setPri(deviceOrder.get(deviceIds.get(i).getId()));
                dataBeanList.add(dataBean);
            }
            infoDeliveryDto.setFileInfo(dataBeanList);*/
            infoDeliveryDtoList.add(infoDeliveryDto);
        }
        return infoDeliveryDtoList;
    }

    //信息撤回组装
    public static List<CancelInfoDeliveryDto> infoRevocation(List<EllDeviceIdAndNum> deviceIds, List<EllSelInfoVo> ellSelInfoVoList, List<EllInfoFileVo> fileList) {
        List<CancelInfoDeliveryDto> cancelInfoDeliveryDtoList = new ArrayList<>();
        for (int i = 0; i < deviceIds.size(); i++) {
            CancelInfoDeliveryDto cancelInfoDeliveryDto = new CancelInfoDeliveryDto();
            cancelInfoDeliveryDto.setDeviceId(deviceIds.get(i).getDeviceId());
            cancelInfoDeliveryDto.setDeliveryId(ellSelInfoVoList.get(0).getId());
            cancelInfoDeliveryDto.setType(ellSelInfoVoList.get(0).getInfoType());
            //文件
            List<CancelInfoDeliveryDto.DataBean> fileInfo = new ArrayList<>();
            if (ellSelInfoVoList.get(0).getInfoType() != 3) {
                for (int j = 0; j < fileList.size(); j++) {
                    CancelInfoDeliveryDto.DataBean dataBean = new CancelInfoDeliveryDto.DataBean();
                    dataBean.setFileId(fileList.get(j).getFileId());
                    dataBean.setFileName(fileList.get(j).getFileUrl());
                    dataBean.setFileType(ellSelInfoVoList.get(0).getInfoType());
                    fileInfo.add(dataBean);
                }
                cancelInfoDeliveryDto.setFileInfo(fileInfo);
            }
            cancelInfoDeliveryDtoList.add(cancelInfoDeliveryDto);
        }

        return cancelInfoDeliveryDtoList;
    }
}
