package com.eluolang.physical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllScoreHistoryDto {

    private String userName;
    private String userSex;
    private String className;
    private String endScore;
    private String proName;
    private String data;
    private String score;
    private String id;
    private String testNumber;
    private int sortNum;

    @Override
    public String toString() {
        return testNumber + "," + "," + userName + "," + userSex + "," + className + "," + endScore + "," + proName + "," + data + "," + score;
    }

    public String toMerge() {
        return "," + proName + "," + data + "," + score;
    }
}
