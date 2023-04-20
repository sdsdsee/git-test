package com.eluolang.playground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllGetUserByCourseIdDto {
    private String id;
    private String name;
    private String studentId;
    private String studentCode;
    private String orgId;
    private String orgName;
    private String fileUrl;
    private String sex;
}
