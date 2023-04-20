package com.eluolang.physical.model;

import com.eluolang.common.core.pojo.EllPlanScoresOf;
import com.eluolang.common.core.pojo.EllTestAppointment;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlanVo {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private String id;
    /**
     * 计划标题
     */
    @ApiModelProperty(value = "计划标题")
    private String planTitle;
    /**
     * 计划实行地址
     */
    @ApiModelProperty(value = "计划实行地址")
    private String planAddr;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private String dateBegin;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private String dateEnd;
    /**
     * 是否需要预约
     */
    @ApiModelProperty(value = "是否需要预约,1是2否")
    private String isSubscribe;
    /**
     * 测试项目id
     */
    @ApiModelProperty(value = "测试项目例如:1,2,3")
    private String proId;
    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    private String planRemark;
    @ApiModelProperty(value = "修改人id")
    private String updateById;
    @ApiModelProperty(value = "修改状态")
    private String updateState;
    /**
     * 是否需要重测1是2否
     */
    private String isResurvey;
    /**
     * 是否需要监考员登陆1是2否
     */
    private String isAuthentication;
    /**
     * 预约人数
     */
    private String authNum;
    /**
     * 计划使用部门
     */
    private String useOrgId;
    /**
     * 是否是考试
     */
    private int isExam;
    /**
     * 自选项目数量
     */
    private int cusTheNumber;
    /**
     * 机会
     */
    private List<CreateChanceVo> createChanceVos;
    /**
     * 预约时间
     */
    private List<EllTestAppointment> ellTestAppointments;
    /**
     * 计划算分占比
     */
    private List<EllPlanScoresOfVo> ellPlanScoresOfs;
    /***
     * 小程序是否允许查看成绩
     */
    private int isView;
}
