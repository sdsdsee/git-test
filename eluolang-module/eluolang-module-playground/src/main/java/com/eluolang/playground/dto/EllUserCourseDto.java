package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllUserCourseDto {
    private String userName;
    private String className;
    private String studentCode;
    private String courseName;
    private String dateTime;
    private String times;
}
