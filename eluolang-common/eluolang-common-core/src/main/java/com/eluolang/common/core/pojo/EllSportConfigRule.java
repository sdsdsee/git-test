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
 * @Description: 运动指南规则配置
 * @Author: jeecg-boot
 * @Date:   2021-11-02
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ell_sport_config_rule对象", description="运动指南规则配置")
public class EllSportConfigRule implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
    @ApiModelProperty(value = "id")
    private String id;
	/**isDelete*/
    @ApiModelProperty(value = "isDelete")
    private Integer isDelete;
	/**度量符号(>,=,<等等)*/
    @ApiModelProperty(value = "度量符号(>,=,<等等)")
    private String measure;
	/**性别：1男；2女*/
    @ApiModelProperty(value = "性别：1男；2女")
    private Integer sex;
	/**评语(优秀，良好等)*/
    @ApiModelProperty(value = "评语(优秀，良好等)")
    private String comment;
	/**运动建议*/
    @ApiModelProperty(value = "运动建议")
    private String guide;
	/**createTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createTime")
    private String createTime;
	@ApiModelProperty(value = "运动指南ID")
	private String sportConfigId;
	@ApiModelProperty(value = "项目ID")
	private Integer proId;
}
