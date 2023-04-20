package com.eluolang.module.report.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Vision {
    private String name;
    private String left;
    private String right;
    private String comment;
}
