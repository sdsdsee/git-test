package com.eluolang.physical.util;

import com.eluolang.common.core.pojo.EllPlanProjectChance;
import com.eluolang.physical.model.FindScoreVo;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataProcessingUtil {
    public static List<FindScoreVo> scoreProcessingUtil(String dataArray[], FindScoreVo score, Map<Integer, List<EllPlanProjectChance>> integerListMap) {
        List<FindScoreVo> scoreVoList = new ArrayList<>();
        //它的子项目
        List<EllPlanProjectChance> ellPlanProjectChanceList = integerListMap.get(score.getTestProject());
        int pattern = 0;
        if (dataArray.length == 3) {
            pattern = 1;
        } else if (dataArray.length > 6) {
            pattern = 2;
        }
        switch (pattern) {
            //身高体重
            case 1:
                for (int i = 0; i < ellPlanProjectChanceList.size(); i++) {
                    switch (ellPlanProjectChanceList.get(i).getProNameAbbreviation()) {
                        //身高
                        case "sg":
/*//要注释这是把数子变整数并且把m转换为cm
                            if (Double.valueOf(dataArray[0]).intValue() < 1) {
                                dataArray[0] = (Double.valueOf(dataArray[0]).intValue() * 100) + "";
                            } else {
                                dataArray[0] = Double.valueOf(dataArray[0]).intValue() + "";
                            }*/
                            FindScoreVo scoreSg = SerializationUtils.clone(score);
                            scoreSg.setTestProject(ellPlanProjectChanceList.get(i).getProId());
                            scoreSg.setData(dataArray[0]);
                            scoreSg.setScore(score.getScore());
                            scoreVoList.add(scoreSg);
                            continue;
                            //体重
                        case "tz":
/*//要注释这是把数子变整数并且把斤变为公斤
                            if (Double.valueOf(dataArray[1]).intValue() > 100) {
                                dataArray[1] = (Double.valueOf(dataArray[1]).intValue() / 2) + "";
                            } else {
                                dataArray[1] = Double.valueOf(dataArray[1]).intValue() + "";
                            }*/
                            FindScoreVo scoreTz = SerializationUtils.clone(score);
                            scoreTz.setTestProject(ellPlanProjectChanceList.get(i).getProId());
                            scoreTz.setData(dataArray[1]);
                            scoreTz.setScore(score.getScore());
                            scoreVoList.add(scoreTz);
                            continue;
                        case "bmi":
                            FindScoreVo scoreBMI = SerializationUtils.clone(score);
                            scoreBMI.setTestProject(ellPlanProjectChanceList.get(i).getProId());
                            scoreBMI.setData(dataArray[2]);
                            scoreBMI.setScore(score.getScore());
                            scoreVoList.add(scoreBMI);
                            break;
                    }
                    break;
                }
                //视力
            case 2:
                for (int i = 0; i < ellPlanProjectChanceList.size(); i++) {
                    switch (ellPlanProjectChanceList.get(i).getProNameAbbreviation()) {
                        //左眼裸眼视力
                        case "zylysl":
                            FindScoreVo scoreZylysl = SerializationUtils.clone(score);
                            scoreZylysl.setTestProject(ellPlanProjectChanceList.get(i).getProId());
                            scoreZylysl.setData(dataArray[0]);
                            scoreVoList.add(scoreZylysl);
                            continue;
                            //右眼裸眼视力
                        case "yylysl":
                            FindScoreVo scoreYylysl = SerializationUtils.clone(score);
                            scoreYylysl.setTestProject(ellPlanProjectChanceList.get(i).getProId());
                            scoreYylysl.setData(dataArray[1]);
                            scoreVoList.add(scoreYylysl);
                            continue;
                            //左眼串镜
                        case "zycj":
                            FindScoreVo scoreZycj = SerializationUtils.clone(score);
                            scoreZycj.setTestProject(ellPlanProjectChanceList.get(i).getProId());
                            scoreZycj.setData("");
                            scoreVoList.add(scoreZycj);
                            continue;
                            //右眼串镜
                        case "yycj":
                            FindScoreVo scoreYycj = SerializationUtils.clone(score);
                            scoreYycj.setTestProject(ellPlanProjectChanceList.get(i).getProId());
                            scoreYycj.setData(" ");
                            scoreVoList.add(scoreYycj);
                            continue;
                            //左眼屈光不正
                        case "zyqgbz":
                            FindScoreVo scoreZyqgbz = SerializationUtils.clone(score);
                            scoreZyqgbz.setTestProject(ellPlanProjectChanceList.get(i).getProId());
                            scoreZyqgbz.setData(dataArray[dataArray.length - 2]);
                            scoreVoList.add(scoreZyqgbz);
                            continue;
                            //右眼屈光不正
                        case "yyqgbz":
                            FindScoreVo scoreYyqgbz = SerializationUtils.clone(score);
                            scoreYyqgbz.setTestProject(ellPlanProjectChanceList.get(i).getProId());
                            scoreYyqgbz.setData(dataArray[dataArray.length - 1]);
                            scoreVoList.add(scoreYyqgbz);
                            continue;
                    }
                    break;
                }
                break;
        }
        return scoreVoList;
    }
}
