package com.eluolang.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description: 运动指南配置
 * @Author: jeecg-boot
 * @Date:   2021-11-02
 * @Version: V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="ell_sport_config对象", description="运动指南配置")
public class EllSportConfigDto {

	/**id*/
    private String id;
	/**name*/
    private String name;
	/**proId*/
    private String proIds;
	/**createTime*/
    private String createTime;
	/**isDelete*/
    private Integer isDelete;
}
