package com.eluolang.platform.fegin.fallBack;

import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.hardware.dto.CancelInfoDeliveryDto;
import com.eluolang.common.core.hardware.dto.InfoDeliveryDto;
import com.eluolang.common.core.hardware.dto.OnOffDeviceDto;
import com.eluolang.common.core.web.Result;
import com.eluolang.platform.fegin.TcpServerRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Component
@Slf4j
public class RemoteTcpServerFallBackFactory implements TcpServerRemoteService {
    @Override
    public Result infoDelivery(InfoDeliveryDto infoDeliveryDto) throws Exception {
        return new Result(HttpStatus.ERROR);
    }

    @Override
    public Result onOffDevice(OnOffDeviceDto onOffDeviceDto) throws Exception {
        return new Result(HttpStatus.ERROR);
    }

    @Override
    public Result cancelInfoDelivery(CancelInfoDeliveryDto cancelInfoDeliveryDto) throws Exception {
        return new Result(HttpStatus.ERROR);
    }

    @Override
    public Result sendTo(String message, String toUserId) throws IOException {
        return new Result(HttpStatus.ERROR);
    }
}
