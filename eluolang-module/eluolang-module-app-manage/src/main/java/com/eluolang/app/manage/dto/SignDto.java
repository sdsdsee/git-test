package com.eluolang.app.manage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年11月15日 16:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignDto {
    /** 时间戳 */
    private String date;
    /** 电话号码 */
    private String phoneNumber;
}
