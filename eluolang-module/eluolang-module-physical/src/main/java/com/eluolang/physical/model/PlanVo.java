package com.eluolang.physical.model;

import com.eluolang.common.core.pojo.EllPlanScoresOf;
import com.eluolang.common.core.pojo.EllTestAppointment;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanVo {
    //@ApiParam在swagger上传参隐藏
    @ApiParam(hidden = true)
    private String id;
    /**
     * 计划标题
     */
    private String planTitle;
    /**
     * 计划实行地址
     */
    private String planAddr;
    /**
     * 开始时间
     */
    private String dateBegin;
    /**
     * 结束时间
     */
    private String dateEnd;
    /**
     * 删除标记
     */
    @ApiParam(hidden = true)
    private Integer isDelete;
    /**
     * 状态(1待发布2正在进行3已结束
     */

    private int planState;
    /**
     * 修改时间
     */
    @ApiParam(hidden = true)
    private String updateTime;
    /**
     * 说明
     */
    private String planRemark;
    /**
     * 测试项目
     */
    private String proId;
    /**
     * 使用的规则
     */
    private String useRules;
    /**
     * 是否需要重测1是2否
     */
    private int isResurvey;
    /**
     * 是否需要监考员登陆1是2否
     */
    private int isAuthentication;
    /**
     * 预约人数
     */
    private int authNum;
    /**
     * 账号id
     */
    private String orgId;
    /**
     * 是否需要预约
     */
    private String isSubscribe;
    /**
     * 创建人id
     */
    private String createById;
    /**
     * 使用的部门
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
