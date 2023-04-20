package com.eluolang.platform.mapper;

import com.eluolang.common.core.pojo.*;
import com.eluolang.platform.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InfoMapper {
    /**
     * 添加新闻
     */
    int addInfo(@Param("ellInformationVo") EllInformationVo ellInformationVo);

    /**
     * 图片储存
     *
     * @param ellFIleVoList
     * @return
     */
    int uploadImage(@Param("ellFIleVoList") List<EllFIleVo> ellFIleVoList);

    /**
     * 查看
     */
    List<EllSelInfoVo> findInfo(@Param("ellInformation") EllInformation ellInformation);

    /**
     * 信息发部的设备
     */
    List<EllInfoPublishTarget> selPublishDevice(String infoId);

    /**
     * 查看该新闻发布的所有文件地址
     */
    List<EllFIleVo> selFileUrl(String infoId);

    /**
     * 查看该新闻发布的所有文件
     */
    List<EllInfoFileVo> selFile(String infoId);

    /**
     * 查询LED文件
     */
    String selFileLed(String fileId);

    /**
     * 更新
     */
    int updateInfo(@Param("ellInformation") EllInformationVo ellInformation);

    /**
     * 批量新增信息发布到的那个设备
     *
     * @param entities List<EllInfoPublishTarget> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EllInfoPublishTarget> entities);

    /**
     * 通过新闻id/设备id删除数据
     *
     * @param infoId
     * @return 影响行数
     */
    int deleteByInfoId(@Param("infoId") String infoId, @Param("deviceId") String deviceId);

    /**
     * 通过信息id查询设备id
     */
    List<EllDeviceIdAndNum> selDeviceByInfo(@Param("infoId") String infoId, @Param("deviceType") int deviceType);

    /**
     * 删除信息的文件
     */
    int delInfoFile(String infoId);

    /**
     * 删除信息发布的设备
     */
    int delInfoDevice(String infoId);

    /**
     * 批量新增新闻文件
     *
     * @param entities List<EllInfoFile> 实例对象列表
     * @return 影响行数
     */
    int insertBatchInfoFile(@Param("entities") List<EllInfoFile> entities);

    /**
     * 查询该信息在该设备的发布顺序
     */
    int findInfoOrder(String deviceId);

    /**
     * 查询设备编号
     */
    List<EllDeviceIdAndNum> selDeviceId(@Param("deviceArray") String deviceArray[], @Param("deviceType") int deviceType);

    /**
     * 根据设备id和发布id查询信息发布记录
     *
     * @param deviceId
     * @param infoId
     * @return
     */
    InfoDeliveryFileVo selInfoDeliveryFile(@Param("deviceId") String deviceId, @Param("infoId") String infoId);

    /**
     * 根据发布id查询文件信息
     *
     * @param infoId
     * @return
     */
    List<DeliveryFileVo> selFileByInfoId(@Param("infoId") String infoId);

    /**
     * 添加led信息发布
     */
    int insertLedInfo(@Param("ellLedInformationVo") EllLedInformationVo ellLedInformationVo);

    /**
     * 查询Led信息发布
     *
     * @param ellLedInformationVo
     * @return
     */
    List<EllLedInformationVo> selLedInfo(@Param("ellLedInformationVo") EllLedInformationVo ellLedInformationVo);

    /**
     * 更新LED数据
     */
    int updateLED(@Param("ellLedInformationVo") EllLedInformationVo ellLedInformationVo);

    /**
     * 查询led信息的发布
     */
    List<EllLedInformationPublishVo> selLedPublish(@Param("ellLedInformationPublishVo") EllLedInformationPublishVo ellLedInformationPublishVo);
    /**
     * 添加设备发布信息的绑定
     */
    int addPublishLedInfo(@Param("ellLedPublishInfoList") List<EllLedPublishInfo> ellLedPublishInfoList);

    /**
     * 删除led已经发布信息
     */
    int delLedInfo(@Param("ledInfoId") String ledInfoId, @Param("deviceId") String deviceId);
}
