package com.eluolang.physical.Listener;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.eluolang.common.core.pojo.EllTestData;
import com.eluolang.common.core.pojo.EllTestProject;
import com.eluolang.common.core.web.Result;
import com.eluolang.physical.controller.ScoreController;
import com.eluolang.physical.model.EllHistoryVo;
import com.eluolang.physical.model.EllReturnScoreVo;
import com.eluolang.physical.model.FindScoreConditionVo;
import com.eluolang.physical.service.EllUserService;
import com.eluolang.physical.service.PhysicalDataService;
import com.eluolang.physical.service.ScoreService;
import com.eluolang.physical.util.CreateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

public class ScoreListener extends AnalysisEventListener<HashMap<String, Object>> {
    public ScoreListener(ScoreController scoreController, EllUserService ellUserService, ScoreService scoreService, String planId) {
        this.scoreController = scoreController;
        this.ellUserService = ellUserService;
        this.scoreService = scoreService;
        this.planId = planId;
    }

    //表头下标
    private Map<String, Integer> headMapSubscript;
    //表头
    private Map<Integer, String> headMaps;
    //计划项目男
    private List<EllTestProject> ellTestProjectBoyList;
    //计划项目女
    private List<EllTestProject> ellTestProjectGirlList;
    private ScoreController scoreController;
    private PhysicalDataService physicalDataService;
    private EllUserService ellUserService;
    private ScoreService scoreService;
    private String planId;
    //数据保存
    private List<EllHistoryVo> ellHistoryVoList;

    //读取表头数据
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        //先清空
        headMapSubscript = new HashMap<>();
        ellHistoryVoList = new ArrayList<>();
        headMaps = headMap;
        //把表头作为key，下标作为value
        for (Integer key : headMap.keySet()) {
            headMapSubscript.put(headMap.get(key), key);
        }
        ellTestProjectBoyList = scoreService.selPro(planId, "1");
        ellTestProjectGirlList = scoreService.selPro(planId, "2");
    }

    @Override
    public void invoke(HashMap<String, Object> scoreVo, AnalysisContext analysisContext) {
        List<EllTestProject> ellTestProjectList = null;
        if (scoreVo.get(headMapSubscript.get("性别")).equals("1")) {
            ellTestProjectList = ellTestProjectBoyList;
        } else {
            ellTestProjectList = ellTestProjectGirlList;
        }

        //查询userId
        List<EllReturnScoreVo> ellReturnScoreVos = ellUserService.findUserBy(scoreVo.get(headMapSubscript.get("学籍号")).toString(), 0);
        if (ellReturnScoreVos != null && ellReturnScoreVos.size() > 0) {

            for (int i = 0; i < ellTestProjectList.size(); i++) {
                Map<String, List<EllTestData>> testData = new HashMap<>();
                EllHistoryVo ellHistoryVo = new EllHistoryVo();
                ellHistoryVo.setUserId(ellReturnScoreVos.get(0).getId());
                ellHistoryVo.setPlanId(planId);
                //成绩
                if (scoreVo.get(headMapSubscript.get(ellTestProjectList.get(i).getProName())) != null) {
                    String data = scoreVo.get(headMapSubscript.get(ellTestProjectList.get(i).getProName())).toString();
                    //没有成绩就不添加
                    if (data == null || data.equals("")) {
                        break;
                    }
                    //时间的两种
                    if (data.contains(":")) {
                        data = Integer.parseInt(data.split(":")[0]) * 60 + Integer.parseInt(data.split(":")[1]) + "";
                    }
                    if (data.contains("'")) {
                        data = Integer.parseInt(data.split("'")[0]) * 60 + Integer.parseInt(data.split("'")[1].replace("\"", "")) + "";
                    }
                    //获取该项成绩
                    ellHistoryVo.setData(data);
                    //保存为历史数据
                    List<EllTestData> ellTestData = new ArrayList<>();
                    EllTestData ellTestData1 = new EllTestData();
                    ellTestData1.setData(data);
                    ellTestData1.setIsRetest("1");
                    ellTestData1.setTimestamp(new Date().getTime());
                    ellTestData.add(ellTestData1);
                    testData.put("1", ellTestData);
                    ellHistoryVo.setTestData(testData);
                    ellHistoryVo.setTestProject(ellTestProjectList.get(i).getId().toString());
                    ellHistoryVo.setCheckInTimestamp(new Date().getTime());
                    ellHistoryVo.setEndTimestamp(new Date().getTime());
                    ellHistoryVo.setIsRetest(1);
                    ellHistoryVo.setProjectName(ellTestProjectList.get(i).getProName());
                    ellHistoryVo.setUserSex(ellReturnScoreVos.get(0).getUserSex() + "");
                    ellHistoryVo.setEndTime(CreateTime.getTime());
                    ellHistoryVo.setUserOrgId(ellReturnScoreVos.get(0).getOrgId());
                    //保存数据
                    ellHistoryVoList.add(ellHistoryVo);
                }
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        for (int i = 0; i < ellHistoryVoList.size(); i++) {
            int count = scoreService.addStudentScoreMapper(ellHistoryVoList.get(i));
            if (count > 0) {
                FindScoreConditionVo findScoreConditionVo = new FindScoreConditionVo();
                findScoreConditionVo.setPlanId(planId);
                findScoreConditionVo.setUserId(ellHistoryVoList.get(i).getUserId());
                //查询学生成绩
                List<EllReturnScoreVo> findUser = scoreService.findUserReturn(findScoreConditionVo, null);
                //生成最终分数
                scoreService.createEndScore(findScoreConditionVo, findUser);
            }
        }
    }
}
