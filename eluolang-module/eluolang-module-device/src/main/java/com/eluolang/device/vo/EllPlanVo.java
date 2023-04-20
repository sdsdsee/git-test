package com.eluolang.device.vo;

import com.eluolang.common.core.pojo.*;

import java.util.List;

public class EllPlanVo {
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
    private int isDelete;
    /**
     * 状态(1待发布2正在进行3已结束
     */
    private int planState;
    /**
     * 创建人id
     */
    private String createById;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
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
//    private String useRules;
    /**
     * 是否需要重测1是2否
     */
    private int isRetest;
    /**
     * 重测是否需要认证1是2否空为不要重测
     */
    private int isAuthentication;
    /**
     * 重测次数，为空就无限
     */
    private String authNum;
    /**
     * 是否需要预约
     */
    private String isSubscribe;
    /**
     * 部门id
     */
    private String orgId;
    /**
     * 计划类型（1:考试 2:测试）
     */
    private int isExam;

    /**
     * 总人数
     */
    private Integer totalNumber;
    /**
     * 男生人数
     */
    private Integer manNumber;
    /**
     * 女生人数
     */
    private Integer womanNumber;

    /** 体测项目集合 */
    private List<TestProject> testProjects;

    /** 项目测试次数集合 */
    private List<EllPlanProjectChanceVo> planProjectChances;

    /** 计划、规则关联部门集合 */
    private List<EllUsePlanDepartment> departments;

    /** 学生信息集合 */
    private List<EllUserVo> UserVoList;

    /** 体测计划分组集合 */
    private List<EllTestRules> ellTestRulesList;

    /** 体测计划规则集合 */
    private List<EllTestRulesScore> scoreRules;

    /** 重测申请理由集合 */
    private List<SysDataDictionary> reasons;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public String getPlanAddr() {
        return planAddr;
    }

    public void setPlanAddr(String planAddr) {
        this.planAddr = planAddr;
    }

    public String getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(String dateBegin) {
        this.dateBegin = dateBegin;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public int getPlanState() {
        return planState;
    }

    public void setPlanState(int planState) {
        this.planState = planState;
    }

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getPlanRemark() {
        return planRemark;
    }

    public void setPlanRemark(String planRemark) {
        this.planRemark = planRemark;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public int getIsRetest() {
        return isRetest;
    }

    public void setIsRetest(int isRetest) {
        this.isRetest = isRetest;
    }

    public int getIsAuthentication() {
        return isAuthentication;
    }

    public void setIsAuthentication(int isAuthentication) {
        this.isAuthentication = isAuthentication;
    }

    public String getAuthNum() {
        return authNum;
    }

    public void setAuthNum(String authNum) {
        this.authNum = authNum;
    }

    public String getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public int getIsExam() {
        return isExam;
    }

    public void setIsExam(int isExam) {
        this.isExam = isExam;
    }

    public Integer getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public Integer getManNumber() {
        return manNumber;
    }

    public void setManNumber(Integer manNumber) {
        this.manNumber = manNumber;
    }

    public Integer getWomanNumber() {
        return womanNumber;
    }

    public void setWomanNumber(Integer womanNumber) {
        this.womanNumber = womanNumber;
    }

    public List<TestProject> getTestProjects() {
        return testProjects;
    }

    public void setTestProjects(List<TestProject> testProjects) {
        this.testProjects = testProjects;
    }

    public List<EllPlanProjectChanceVo> getPlanProjectChances() {
        return planProjectChances;
    }

    public void setPlanProjectChances(List<EllPlanProjectChanceVo> planProjectChances) {
        this.planProjectChances = planProjectChances;
    }

    public List<EllUsePlanDepartment> getDepartments() {
        return departments;
    }

    public void setDepartments(List<EllUsePlanDepartment> departments) {
        this.departments = departments;
    }

    public List<EllUserVo> getUserVoList() {
        return UserVoList;
    }

    public void setUserVoList(List<EllUserVo> userVoList) {
        UserVoList = userVoList;
    }

    public List<EllTestRules> getEllTestRulesList() {
        return ellTestRulesList;
    }

    public void setEllTestRulesList(List<EllTestRules> ellTestRulesList) {
        this.ellTestRulesList = ellTestRulesList;
    }

    public List<EllTestRulesScore> getScoreRules() {
        return scoreRules;
    }

    public void setScoreRules(List<EllTestRulesScore> scoreRules) {
        this.scoreRules = scoreRules;
    }

    public List<SysDataDictionary> getReasons() {
        return reasons;
    }

    public void setReasons(List<SysDataDictionary> reasons) {
        this.reasons = reasons;
    }
}