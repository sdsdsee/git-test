package com.eluolang.app.manage.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentPlanVo {
    private String userId;
    private String planId;
    private String dayTime;
    private int subDateId;
}
