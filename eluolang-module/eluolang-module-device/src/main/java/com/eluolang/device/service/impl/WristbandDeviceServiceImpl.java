package com.eluolang.device.service.impl;

import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.device.dto.EllBodyErrorScreenshotDto;
import com.eluolang.device.dto.EllDeviceDto;
import com.eluolang.device.dto.EllWristbandDeviceDto;
import com.eluolang.device.mapper.DeviceMapper;
import com.eluolang.device.mapper.WristbandDeviceMapper;
import com.eluolang.device.service.DeviceService;
import com.eluolang.device.service.WristbandDeviceService;
import com.eluolang.device.util.CreateTime;
import com.eluolang.device.util.PathGeneration;
import com.eluolang.device.util.VideoChangeUtil;
import com.eluolang.device.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 手环设备业务逻辑层
 *
 * @author dengrunsen
 */
@Transactional
@Service
public class WristbandDeviceServiceImpl implements WristbandDeviceService {

    @Autowired
    private WristbandDeviceMapper wristbandDeviceMapper;

    @Override
    public int addWristbandDevice(EllWristbandDevice ellWristbandDevice) {
        return wristbandDeviceMapper.addWristbandDevice(ellWristbandDevice);
    }

    @Override
    public int addWristbandDeviceList(List<EllWristbandDevice> ellWristbandDeviceList) {
        return wristbandDeviceMapper.addWristbandDeviceList(ellWristbandDeviceList);
    }

    @Override
    public int selWristbandIsExist(String nfcId, String mac) {
        return wristbandDeviceMapper.selWristbandIsExist(nfcId, mac);
    }

    @Override
    public int deleteWristbandDevice(String id) {
        return wristbandDeviceMapper.deleteWristbandDevice(id);
    }

    @Override
    public int updateWristbandDevice(EllWristbandDevice ellWristbandDevice) {
        return wristbandDeviceMapper.updateWristbandDevice(ellWristbandDevice);
    }

    @Override
    public List<EllWristbandDevice> selectWristbandDevice(EllWristbandDeviceDto ellWristbandDeviceDto) {
        return wristbandDeviceMapper.selectWristbandDevice(ellWristbandDeviceDto);
    }

    @Override
    public EllWristbandDevice selWristbandByNfcId(String nfcId) {
        return wristbandDeviceMapper.selWristbandByNfcId(nfcId);
    }

    @Override
    public int selDepartIsExist(Integer deptId) {
        return wristbandDeviceMapper.selDepartIsExist(deptId);
    }
}

