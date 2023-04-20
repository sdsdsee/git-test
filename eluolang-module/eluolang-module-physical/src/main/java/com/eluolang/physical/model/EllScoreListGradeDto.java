package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllScoreListGradeDto {
    private int proId;
    private String grade;
    private String proName;
    private int parentId;
    private String historyId;
    private String unit;
}
