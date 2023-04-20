package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllDeviceVersions {
    private String id;
    private String fileUrl;
    private String deviceVersions;
    private String deviceDescription;
    private int parentId;
}
