package com.eluolang.physical.model;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRuleVo {
    @ApiParam(hidden = true)
    private int id;
    private String ruleName;
    private String parentId;
    private String createById;
    private String proId;
}
