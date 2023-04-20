package com.eluolang.device.service;

import com.eluolang.common.core.pojo.*;
import com.eluolang.device.dto.EllBodyErrorScreenshotDto;
import com.eluolang.device.dto.EllDeviceDto;
import com.eluolang.device.dto.EllWristbandDeviceDto;
import com.eluolang.device.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 定义业务层的手环设备接口方法
 *
 * @author dengrunsen
 */
public interface WristbandDeviceService {
    /**
     * 添加手环设备
     * @param ellWristbandDevice
     * @return
     */
    int addWristbandDevice(EllWristbandDevice ellWristbandDevice);

    /**
     * 批量添加手环设备
     * @param ellWristbandDeviceList
     * @return
     */
    int addWristbandDeviceList(List<EllWristbandDevice> ellWristbandDeviceList);

    /**
     * 查询是否有已存在的手环
     * @return
     */
    int selWristbandIsExist(String nfcId,String mac);

    /**
     * 根据id删除手环设备
     * @param id
     * @return
     */
    int deleteWristbandDevice(String id);

    /**
     * 修改手环设备信息
     * @param ellWristbandDevice
     * @return
     */
    int updateWristbandDevice(EllWristbandDevice ellWristbandDevice);

    /**
     * 按条件查询手环设备信息
     * @param ellWristbandDeviceDto
     * @return
     */
    List<EllWristbandDevice> selectWristbandDevice(EllWristbandDeviceDto ellWristbandDeviceDto);

    /**
     * 根据NFC查询手环信息
     * @param nfcId
     * @return
     */
    EllWristbandDevice selWristbandByNfcId(String nfcId);

    /**
     * 查询部门是否存在
     * @param deptId
     * @return
     */
    int selDepartIsExist(Integer deptId);
}
