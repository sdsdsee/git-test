package com.eluolang.device.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllBodyErrorScreenshotDto implements Serializable {
    /**
     * 项目id
     */
    private int projectId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 计划id
     */
    private String planId;
    /**
     * 错误原因
     */
    private String errorTraces;


}