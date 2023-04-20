package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllProRankUserDto {
    //排名
    private Integer rank;
    //排名变化
    private Integer change;
    //学生姓名
    private String userName;
    //学生性别
    private Integer userSex;
    //成绩
    private float score;
    //用户id
    private String userId;
}
