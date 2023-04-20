package com.eluolang.module.report.vo;

import java.util.List;

public class VisionTestVo {
    /** 评语 */
    private String comment;
    private TestSituationQueryVo testSituationQueryVo;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TestSituationQueryVo getTestSituationQueryVo() {
        return testSituationQueryVo;
    }

    public void setTestSituationQueryVo(TestSituationQueryVo testSituationQueryVo) {
        this.testSituationQueryVo = testSituationQueryVo;
    }
}
