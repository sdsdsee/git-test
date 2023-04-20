package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllIndividualUserDto {
    //学生姓名
    private String userName;
    //年级
    private String grade;
    //入学年级
    private Integer enGrade;
    //入学时间
    private String enTime;
    //班级
    private String className;
    //学号
    private String studentCode;
    //部门id
    private Integer orgId;
}
