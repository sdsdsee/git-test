package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllAdmissionTicketPrintingVo {
    private String planTitle;
    private String examNumber;
    private String schoolName;
    private String className;
    private String dateBegin;
    private String dateEnd;
    private List<String> proName;
    private String imgUrl;
    private String userName;
    private int sex;
    private String id;
    private String studentCode;
}
