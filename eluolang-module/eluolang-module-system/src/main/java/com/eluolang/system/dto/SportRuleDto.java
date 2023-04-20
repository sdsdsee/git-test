package com.eluolang.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2023年01月04日 18:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SportRuleDto {
    private String sportConfigId;
    private String proId;
    /**性别：1男；2女*/
    private String sex;
    /**评语(优秀，良好等)*/
    private String comment;
    /** 当前页码 */
    private Integer pageNum;
    /** 页面显示条数 */
    private Integer pageSize;
}
