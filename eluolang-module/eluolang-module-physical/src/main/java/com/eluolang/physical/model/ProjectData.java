package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectData {
    private int one;
    private int two;
    private int three;
    private int four;
    private int sum;
    private int sumBoy;
    private int sumGirl;
    //满分
    private int FullMark;
}
