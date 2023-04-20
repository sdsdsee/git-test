package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorScreenShotVo {
    private int proId;
    private String proName;
    private String userId;
    private String errorImageUri;
    private String errorTraces;
    private List<String> errorTraceList;
    private int isRetest;
}
