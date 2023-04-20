package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OneScoreVo {
    private String orgId;
    private int account;
    private int proId;
    private String userName;
    private String planId;
    private String userId;
    private int page;
}
