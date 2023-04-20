package com.eluolang.device.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysScreenRecord {
    private String uuid;
    private String recordFileName;
    private String recordTime;
    private String userName;
    private String hostIp;
    private String recordFileAddr;
    private String createUser;
    private String createDateTime;
    private BigDecimal recordFileSize;
    private String recordFileFormat;
    private String recordDuration;

}