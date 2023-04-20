package com.eluolang.physical.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.eluolang.common.core.pojo.EllTestData;
import com.eluolang.common.core.web.Result;
import com.eluolang.physical.controller.ScoreController;
import com.eluolang.physical.model.EllHistoryVo;
import com.eluolang.physical.model.EllReturnScoreVo;
import com.eluolang.physical.model.ScoreVo;
import com.eluolang.physical.service.EllUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

public class ScoreListener extends AnalysisEventListener<ScoreVo> {
    public ScoreListener(ScoreController scoreController, EllUserService ellUserService) {
        this.scoreController = scoreController;
        this.ellUserService = ellUserService;
    }

    @Autowired
    private ScoreController scoreController;
    @Autowired
    private EllUserService ellUserService;
    int i = 1;
    int b = 1;

    @Override
    public void invoke(ScoreVo scoreVo, AnalysisContext analysisContext) {
        System.out.println(i);
        System.out.println("b" + b);
        Map<String, List<EllTestData>> testData = new HashMap<>();
        EllHistoryVo ellHistoryVo = new EllHistoryVo();
        List<EllReturnScoreVo> ellReturnScoreVos = ellUserService.findUserBy(scoreVo.getStudentCode(), 0);
        ellHistoryVo.setUserId(ellReturnScoreVos.get(0).getId());
        ellHistoryVo.setPlanId("116ac9f27c1946eda3f6a72f4cf02934");
        ellHistoryVo.setData(scoreVo.getData());
        List<EllTestData> ellTestData = new ArrayList<>();
        EllTestData ellTestData1 = new EllTestData();
        ellTestData1.setData(scoreVo.getData());
        ellTestData1.setIsRetest("1");
        ellTestData1.setTimestamp(new Date().getTime());
        ellTestData.add(ellTestData1);
        testData.put("1", ellTestData);
        ellHistoryVo.setTestData(testData);
        ellHistoryVo.setTestProject(56 + "");
        ellHistoryVo.setCheckInTimestamp(new Date().getTime());
        ellHistoryVo.setEndTimestamp(new Date().getTime());
        ellHistoryVo.setIsRetest(1);
        ellHistoryVo.setProjectName("50米跑");
        ellHistoryVo.setUserSex(ellReturnScoreVos.get(0).getUserSex() + "");
        ellHistoryVo.setEndTime(CreateTime.getTime());
        ellHistoryVo.setUserOrgId(ellReturnScoreVos.get(0).getOrgId());
        try {
            Result result = scoreController.submitScores(ellHistoryVo);
            i++;
        } catch (IOException e) {
            b++;
            throw new RuntimeException(e);

        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
