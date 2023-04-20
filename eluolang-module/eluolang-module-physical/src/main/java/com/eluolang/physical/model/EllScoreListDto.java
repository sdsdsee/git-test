package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllScoreListDto {
    private String userId;
    private String userName;
    private String studentCode;
    private String studentId;
    private String sex;
    private String orgId;
    private int enGrade;
    private String enTime;
    private List<EllScoreListGradeDto> grade;
}
