package com.eluolang.platform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Param;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllFIleVo {

    private String fileName;
    private Long size;
    private String type;
    private String fileUrl;
    private String uploadTime;
    private String id;
    private String infoId;
}
