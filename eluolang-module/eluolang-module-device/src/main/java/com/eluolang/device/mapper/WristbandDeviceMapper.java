package com.eluolang.device.mapper;

import com.eluolang.common.core.pojo.*;
import com.eluolang.device.dto.EllBodyErrorScreenshotDto;
import com.eluolang.device.dto.EllDeviceDto;
import com.eluolang.device.dto.EllWristbandDeviceDto;
import com.eluolang.device.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 定义数据访问层手环设备接口方法
 *
 * @author dengrunsen
 */
@Mapper
public interface WristbandDeviceMapper {
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
    int addWristbandDeviceList(@Param("ellWristbandDeviceList") List<EllWristbandDevice> ellWristbandDeviceList);

    /**
     * 查询是否有已存在的手环
     * @return
     */
    int selWristbandIsExist(@Param("nfcId")String nfcId,@Param("mac") String mac);

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
