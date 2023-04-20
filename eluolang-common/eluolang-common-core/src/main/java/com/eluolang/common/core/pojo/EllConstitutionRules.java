package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllConstitutionRules {
    //id
    private String id;
    //学校id
    private Integer schoolId;
    //年级
    private Integer grade;
    //规则id
    private Integer rulesId;
}
