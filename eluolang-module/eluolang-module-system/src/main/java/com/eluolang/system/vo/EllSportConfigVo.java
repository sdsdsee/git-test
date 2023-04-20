package com.eluolang.system.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class EllSportConfigVo {

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
