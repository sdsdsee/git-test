package com.eluolang.platform.service;

import com.eluolang.common.core.pojo.EllInfoPublishTarget;
import com.eluolang.common.core.pojo.EllInformation;
import com.eluolang.common.core.pojo.EllLedPublishInfo;
import com.eluolang.platform.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface InfoService {
    int addInfo(EllInformationVo ellInformationVo) throws IOException;

    /**
     * 更新数据
     *
     * @param ellInformationVo 实例对象
     * @return 实例对象
     */
    int update(EllInformationVo ellInformationVo) throws IOException;

    /**
     * 查看
     */
    List<EllSelInfoVo> findInfo(EllInformation ellInformation);

    /**
     * 信息发部的设备
     */
    List<EllInfoPublishTarget> selPublishDevice(String infoId);

    /**
     * 发布数据
     *
     * @param ellInformationVo 实例对象
     * @return 实例对象
     */
    int upInfoState(EllInformationVo ellInformationVo,String deviceArray[] ) throws Exception;
    /**
     * 通过信息id查询设备id
     */
    List<EllDeviceIdAndNum> selDeviceByInfo( String infoId,  int deviceType);
    /**
     * 查看该新闻发布的所有文件
     */
    List<EllFIleVo> selFileUrl(String infoId);

    /**
     * 删除信息发布的设备与信息
     */
    int delInfoDevice(String infoId) throws Exception;

    /**
     * 图片储存
     *
     * @param files
     * @return
     */
    EllAddFileVo uploadImage(int type, MultipartFile files) throws IOException;

    /**
     * 根据设备id和发布id查询信息发布记录
     *
     * @param deviceId
     * @param infoId
     * @return
     */
    InfoDeliveryFileVo selInfoDeliveryFile(String deviceId, String infoId);

    /**
     * 根据发布id查询文件信息
     *
     * @param infoId
     * @return
     */
    List<DeliveryFileVo> selFileByInfoId(String infoId);

    /**
     * 添加led信息发布
     */
    int insertLedInfo(EllLedInformationVo ellLedInformationVo);

    /**
     * 更新LED数据
     */
    int updateLED(EllLedInformationVo ellLedInformationVo);

    /**
     * 查询led信息的发布
     */
    List<EllLedInformationPublishVo> selLedPublish(EllLedInformationPublishVo ellLedInformationPublishVo);

    /**
     * 查询Led信息发布
     *
     * @param ellLedInformationVo
     * @return
     */
    List<EllLedInformationVo> selLedInfo(EllLedInformationVo ellLedInformationVo);

    /**
     * 查询设备编号
     */
    List<EllDeviceIdAndNum> selDeviceId(String deviceArray[], int deviceType);

    /**
     * 查询LED文件
     */
    String selFileLed(String fileId);

    /**
     * 删除led已经发布信息
     */
    int delLedInfo(String ledInfoId,String deviceId);
    /**
     * 添加设备发布信息的绑定
     */
    int addPublishLedInfo(List<EllLedPublishInfo> ellLedPublishInfoList);
}
