package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CopyGroupVo {
    private int id;
    private String ruleName;
    private String createById;
}
