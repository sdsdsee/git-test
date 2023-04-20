package com.eluolang.physical.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaceErrorVO {
    @ExcelProperty("上传失败的用户身份证")
    private String idCard;
}
