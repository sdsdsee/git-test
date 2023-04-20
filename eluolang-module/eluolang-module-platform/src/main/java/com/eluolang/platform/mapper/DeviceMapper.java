package com.eluolang.platform.mapper;

import com.eluolang.common.core.pojo.*;
import com.eluolang.platform.vo.EllDeviceInfoVo;
import com.eluolang.platform.vo.EllDeviceLocationVo;
import com.eluolang.platform.vo.EllDeviceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMapper {
    /**
     * 新增设备数据
     *
     * @param ellDevice 实例对象
     * @return 影响行数
     */
    int insertDevice(@Param("ellDevice") EllLargeScreen ellDevice);

    /**
     * 查询设备数据
     *
     * @param ellDevice 查询条件
     * @return 对象列表
     */
    List<EllDeviceVo> queryAllByLimit(@Param("ellDevice") EllLargeScreen ellDevice);

    /**
     * 更新设备数据
     *
     * @param ellDevice 实例对象
     * @return 影响行数
     */
    int updateDevice(@Param("ellDevice") EllLargeScreen ellDevice);

    /**
     * 查询该设备信息发布
     */
    List<EllDeviceInfoVo> findDeviceInfo(String deviceId);

    /**
     * 查看该新闻发布的所有文件
     */
    List<String> selFileUrl(String infoId);

    /**
     * 在权限表添加设备的权限
     */
    int addJurisdiction(@Param("ellOperatorRatList") List<EllOperatorRat> ellOperatorRatList);

    /**
     * 查询上级账号
     */
    List<EllOperator> selOpt(int id);

    /**
     * 删除设备
     */
    int deleteDevice(String id);

    /**
     * 删除设备发布的内容
     */
    int deletePublishTarget(String deviceId);

    /**
     * 添加电量
     */
    int addElectricity(@Param("ellElectricQuantity") EllElectricQuantity ellElectricQuantity);

    /**
     * 添加警报
     */
    int addAlarm(@Param("ellDeviceAlarm") EllDeviceAlarm ellDeviceAlarm);

    /**
     * 查询设备坐标
     */
    List<EllDeviceLocationVo> selLocation(@Param("createBy") int createBy, @Param("deviceType") int deviceType);

    /**
     * 通过设备id获取rtsp
     */
    String selRtsp(String deviceId);
}
