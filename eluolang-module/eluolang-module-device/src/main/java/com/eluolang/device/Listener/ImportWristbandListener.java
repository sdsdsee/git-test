package com.eluolang.device.Listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.eluolang.common.core.pojo.EllWristbandDevice;
import com.eluolang.common.core.util.DateUtils;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.device.service.WristbandDeviceService;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Slf4j
@Transactional
public class ImportWristbandListener extends AnalysisEventListener<EllWristbandDevice> {
    private WristbandDeviceService wristbandDeviceService;
    private List<EllWristbandDevice> list = new ArrayList<>();

    //导入信息不对的人员
    private List<EllWristbandDevice> listMistake = new ArrayList<>();

    public ImportWristbandListener(WristbandDeviceService wristbandDeviceService) {
        //这里是demo，所以随便new一个。实际使用如果到了spring请使用下面的有参构z造函数
        this.wristbandDeviceService = wristbandDeviceService;
    }

    @Override
    public void invoke(EllWristbandDevice ellWristbandDevice, AnalysisContext analysisContext) {
        list.add(ellWristbandDevice);
    }

    @SneakyThrows
    @Transactional
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        for (EllWristbandDevice e:list) {
            Integer count = wristbandDeviceService.selWristbandIsExist(e.getNfcId(),e.getMac());
            if (count > 0){
                throw new Exception("手环已存在"+e.getMac());
            }
            Integer count2 = wristbandDeviceService.selDepartIsExist(e.getDeptId());
            if (count2 == 0){
                throw new Exception("部门不存在,id为"+e.getDeptId());
            }
            e.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
        }
        wristbandDeviceService.addWristbandDeviceList(list);
    }
}

