package com.eluolang.playground.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllBodyProblemDto {
    //bodyId
    private Integer bodyId;
    //部位名称
    private String bodyName;
}
