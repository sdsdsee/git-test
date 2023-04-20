package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllScoreDetails {
    private int excellent;
    private int good;
    private int pass;
    private int fail;
    private int excellentBoy;
    private int goodBoy;
    private int passBoy;
    private int failBoy;
    private int excellentGirl;
    private int goodGirl;
    private int passGirl;
    private int failGirl;
}
