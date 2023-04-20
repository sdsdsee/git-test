package com.eluolang.platform.service;

import com.eluolang.common.core.pojo.EllDevice;
import com.eluolang.common.core.pojo.EllDeviceAlarm;
import com.eluolang.common.core.pojo.EllElectricQuantity;
import com.eluolang.common.core.pojo.EllLargeScreen;
import com.eluolang.platform.vo.EllDeviceInfoVo;
import com.eluolang.platform.vo.EllDeviceLocationVo;
import com.eluolang.platform.vo.EllDeviceVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceService {
    /**
     * 新增数据
     *
     * @param ellDevice 实例对象
     * @return 实例对象
     */
    int insertDevice(EllLargeScreen ellDevice) throws Exception;

    /**
     * 查询车位
     */
//    void selGarage() throws Exception;

    /**
     * 待机/开机
     */
    int closeOpen(String deviceNum, int isOpen) throws Exception;

    /**
     * 更新设备数据
     *
     * @param ellDevice 实例对象
     * @return 实例对象
     */
    int updateDevice(EllLargeScreen ellDevice) throws Exception;

    /**
     * 查询该设备信息发布
     */
    List<EllDeviceInfoVo> findDeviceInfo(String deviceId);

    /**
     * 查看该新闻发布的所有文件
     */
    List<String> selFileUrl(String infoId);

    /**
     * 删除设备
     */
    int deleteDevice(String id, String deviceId, int deviceType) throws Exception;

    /**
     * 查询设备数据
     *
     * @param ellDevice 查询条件
     * @return 对象列表
     */
    List<EllDeviceVo> queryAllByLimit(EllLargeScreen ellDevice);

    /**
     * 添加电量
     */
    int addElectricity(EllElectricQuantity ellElectricQuantity);

    /**
     * 添加警报
     */
    int addAlarm(EllDeviceAlarm ellDeviceAlarm);

    /**
     * 查询设备坐标
     */
    List<EllDeviceLocationVo> selLocation(int createBy, int deviceType);

    /**
     * 通过设备id获取rtsp
     */
    String selRtsp(String deviceId);

    /**
     * 关闭播放
     *
     * @param deviceId
     * @return
     */
    Boolean newClose(String deviceId);

    /**
     * 销毁cmd
     */
    void destroyCmd(String app);
}
