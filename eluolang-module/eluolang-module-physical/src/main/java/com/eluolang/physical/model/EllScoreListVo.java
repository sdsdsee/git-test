package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllScoreListVo {
    private int page;
    private int size;
    private String orgId;
    private String planId;
    private String userName;
    private String studentCode;
    private int proId;
    private int userTypeSex;
    private int accountId;
}
