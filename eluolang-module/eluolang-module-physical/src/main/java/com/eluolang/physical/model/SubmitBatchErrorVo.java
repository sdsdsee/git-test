package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitBatchErrorVo implements Serializable {
    private int code;
    private String message;
    private String planId;
    private String proId;
    private String userId;
    private String studentId;
    private String studentCode;
    private Long checkInTimestamp;
}
