package com.eluolang.common.core.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 运动指南配置
 * @Author: jeecg-boot
 * @Date:   2021-11-02
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ell_sport_config对象", description="运动指南配置")
public class EllSportConfig implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
    @ApiModelProperty(value = "id")
    private String id;
	/**name*/
    @ApiModelProperty(value = "name")
    private String name;
	/**proId*/
    @ApiModelProperty(value = "proId")
    private String proIds;
	/**createTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createTime")
    private String createTime;
	/**isDelete*/
    @ApiModelProperty(value = "isDelete")
    private Integer isDelete;
}
