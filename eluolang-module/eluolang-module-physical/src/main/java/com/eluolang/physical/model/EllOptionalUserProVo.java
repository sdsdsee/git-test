package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllOptionalUserProVo {
    private String userId;
    private String userName;
    private String proId;
    private int userSex;
}
