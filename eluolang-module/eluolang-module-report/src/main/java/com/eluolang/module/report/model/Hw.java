package com.eluolang.module.report.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Hw {
    private String height;
    private String weight;
    private String bmi;
    private String comment;
}
