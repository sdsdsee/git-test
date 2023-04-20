package com.eluolang.module.socket.server.service;

import com.eluolang.common.core.hardware.dto.CancelInfoDeliveryDto;
import com.eluolang.common.core.hardware.dto.DeleteDeviceDto;
import com.eluolang.common.core.hardware.dto.InfoDeliveryDto;
import com.eluolang.common.core.hardware.dto.OnOffDeviceDto;


/**
 * @author dengrunsen
 * @date 2022年05月20日 9:50
 */
public interface PlatformCallHardwareService {

    /**
     * 信息发布
     *
     * @param infoDeliveryDto
     * @return
     */
    Integer infoDelivery(InfoDeliveryDto infoDeliveryDto);

    /**
     * 信息发布
     *
     * @param cancelInfoDeliveryDto
     * @return
     */
    Integer cancelInfoDelivery(CancelInfoDeliveryDto cancelInfoDeliveryDto);

    /**
     * 开关机设备
     * @param onOffDeviceDto
     * @return
     */
    Integer onOffDevice(OnOffDeviceDto onOffDeviceDto);

    /**
     * 删除设备
     * @param deleteDeviceDto
     * @return
     */
    Integer deleteDevice(DeleteDeviceDto deleteDeviceDto);
}
