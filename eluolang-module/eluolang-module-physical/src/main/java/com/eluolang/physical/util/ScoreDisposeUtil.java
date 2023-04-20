package com.eluolang.physical.util;


import com.alibaba.fastjson.JSONArray;
import com.eluolang.common.core.pojo.EllTestData;
import com.eluolang.physical.model.EllAppHistoryVo;
import com.eluolang.physical.model.EllScoreListGradeDto;

import javax.xml.crypto.Data;
import java.text.DecimalFormat;
import java.util.*;

public class ScoreDisposeUtil {
    /**
     * @param testData
     * @param score
     * @param eyesComment
     * @return
     */
    public static EllAppHistoryVo scoreReturn(List<EllTestData> testData, String score, String eyesComment) {
        EllAppHistoryVo ellAppHistoryVo = new EllAppHistoryVo();
        ellAppHistoryVo.setScore(score);
        //格式话数字小数点位数
        DecimalFormat dataFormat = new DecimalFormat("0.0");
        //计算身高体重的bmi值
        //Double BMI = Double.parseDouble(dataFormat.format(Double.parseDouble(testData.get(1).getData()) / Math.pow(Double.parseDouble(testData.get(0).getData()) / 100.0, 2)));
        //身高体重Bmi
        if (testData.size() == 3) {
            ellAppHistoryVo.setScore(testData.get(0).getScore());
            String data = "身高:" + testData.get(0).getData() + "cm,体重:" + testData.get(1).getData() + "kg,Bmi:" + testData.get(2).getData();
            ellAppHistoryVo.setData(data);
            ellAppHistoryVo.setUnit(testData.get(0).getUnit());
//            ellAppHistoryVo.setComment(scoreComment(Integer.parseInt(score)));
            return ellAppHistoryVo;
        }
        //视力
        if (testData.size() > 20) {
            ellAppHistoryVo.setScore("-");
            String data = "左眼裸眼视力:" + testData.get(0).getData() + ",右眼裸眼视力:" + testData.get(1).getData();
            data = data + ",左眼屈光正不正" + testData.get(testData.size() - 2).getData() + ",右眼屈光正不正" + testData.get(testData.size() - 1).getData();
            data = data + ",左眼串镜:-,右眼串镜:-";
            ellAppHistoryVo.setData(data);
            ellAppHistoryVo.setUnit(testData.get(0).getUnit());
            //视力评语
//            ellAppHistoryVo.setComment(eyesComment);
            return ellAppHistoryVo;
        } else if (testData.size() > 3 && testData.size() < 20) {
            ellAppHistoryVo.setScore(testData.get(0).getScore());
            ellAppHistoryVo.setData(testData.get(0).getData());
            ellAppHistoryVo.setUnit(testData.get(0).getUnit());
//            ellAppHistoryVo.setComment(scoreComment(Integer.parseInt(testData.get(0).getScore())));
            return ellAppHistoryVo;
        } else {
            ellAppHistoryVo.setScore(testData.get(0).getScore());
            ellAppHistoryVo.setData(testData.get(0).getData());
            ellAppHistoryVo.setUnit(testData.get(0).getUnit());
//            ellAppHistoryVo.setComment(scoreComment(Integer.parseInt(testData.get(0).getScore())));
        }
        return ellAppHistoryVo;
    }

    //判断成绩等级
    public static String scoreComment(int score) {
        String comment = "";
        //判断除视力以外的所有项目分数的等级
        if (score >= 60 && score < 70) {
            comment = "及格";
        } else if (score >= 70 && score < 90) {
            comment = "良";
        } else if (score >= 90) {
            comment = "优秀";
        } else if (score < 60 && score >= 0) {
            comment = "不及格";
        }
        return comment;
    }

    //判断成绩等级
    public static String scoreComment(Double score, int exam, double fullScore) {
        String comment = "";
        if (exam == 1) {
            if (score >= fullScore * 0.6 && score < fullScore * 0.8) {
                comment = "及格";
            } else if (score >= fullScore * 0.8 && score < fullScore * 0.9) {
                comment = "良";
            } else if (score >= fullScore * 0.9) {
                comment = "优秀";
            } else if (score < fullScore * 0.6 && score >= 0) {
                comment = "不及格";
            }
            return comment;
        }
        //判断除视力以外的所有项目分数的等级
        if (score >= 60 && score < 80) {
            comment = "及格";
        } else if (score >= 80 && score < 90) {
            comment = "良";
        } else if (score >= 90) {
            comment = "优秀";
        } else if (score < 60 && score >= 0) {
            comment = "不及格";
        }
        return comment;
    }

    public static String endScore(String[] data) {
        String dataEnd = "";
        if (data.length == 3) {
            dataEnd = "身高:" + data[0] + "cm,体重:" + data[1] + "kg,Bmi:" + data[2];
        }
        if (data.length > 20) {
            dataEnd = "左眼裸眼视力:" + data[0] + ",右眼裸眼视力:" + data[1];
            dataEnd = dataEnd + ",左眼屈光正不正:" + data[data.length - 2] + ",右眼屈光正不正:" + data[data.length - 1];
            dataEnd = dataEnd + ",左眼串镜:-,右眼串镜:-";
        }
        return dataEnd;
    }

    public static List<EllScoreListGradeDto> scoreList(List<EllScoreListGradeDto> gradeList, List<String> proIds, List<String> optionProIds) {
        List<EllScoreListGradeDto> grade = new ArrayList<>();
        for (int i = 0; i < gradeList.size(); i++) {
            //判断是否需要测试该项目
            if (proIds.contains(String.valueOf(gradeList.get(i).getProId())) || optionProIds.contains(String.valueOf(gradeList.get(i).getProId()))) {
                switch (gradeList.get(i).getProId()) {
                    case 35:
                        if (gradeList.get(i).getGrade() != null && gradeList.get(i).getGrade() != "" && !gradeList.get(i).getGrade().equals("未测试")) {
                            String[] strings = gradeList.get(i).getGrade().split(",");
                            grade.add(new EllScoreListGradeDto(73, strings[0], "身高", 35, "", "cm"));
                            grade.add(new EllScoreListGradeDto(74, strings[1], "体重", 35, "", "kg"));
                        } else {
                            grade.add(new EllScoreListGradeDto(73, "未测试", "身高", 35, "", "cm"));
                            grade.add(new EllScoreListGradeDto(74, "未测试", "体重", 35, "", "kg"));
                        }
                        break;
                    case 43:
                        if (gradeList.get(i).getGrade() != null && gradeList.get(i).getGrade() != "" && !gradeList.get(i).getGrade().equals("未测试")) {
                            String[] strings = gradeList.get(i).getGrade().split(",");
                            grade.add(new EllScoreListGradeDto(67, strings[0], "左眼裸眼视力", 43, "", ""));
                            grade.add(new EllScoreListGradeDto(68, strings[1], "右眼裸眼视力", 43, "", ""));
                        } else {
                            grade.add(new EllScoreListGradeDto(67, "未测试", "左眼裸眼视力", 43, "", ""));
                            grade.add(new EllScoreListGradeDto(68, "未测试", "右眼裸眼视力", 43, "", ""));
                        }
                        break;
                    default:
                        if (gradeList.get(i).getGrade() == null || gradeList.get(i).getGrade() == "" && gradeList.get(i).getGrade() != "未测试") {
                            gradeList.get(i).setGrade("未测试");
                        }
                        grade.add(gradeList.get(i));
                        break;
                }
            } else {
                switch (gradeList.get(i).getProId()) {
                    case 35:
                        grade.add(new EllScoreListGradeDto(73, "", "身高", 35, "", "cm"));
                        grade.add(new EllScoreListGradeDto(74, "", "体重", 35, "", "kg"));
                        break;
                    case 43:
                        grade.add(new EllScoreListGradeDto(67, "", "左眼裸眼视力", 43, "", ""));
                        grade.add(new EllScoreListGradeDto(68, "", "右眼裸眼视力", 43, "", ""));
                        break;
                    default:
                        gradeList.get(i).setGrade("");
                        grade.add(gradeList.get(i));
                        break;
                }
            }
        }

        return grade;
    }

    public static Map<String, Object> updateScore(String data, Map<String, Object> testDataMap) {
        Boolean b = data.contains(",");
        List<EllTestData> testDataList = new ArrayList<>();
        EllTestData testData = null;
        if (b) {
            String datas[] = data.split(",");
            for (int i = 0; i < datas.length; i++) {
                testData = new EllTestData();
                testData.setData(datas[i]);
                testData.setScore("");
                testData.setTimestamp(new Date().getTime());
                testData.setIsRetest("2");
                testDataList.add(testData);
            }
            testDataMap.put(testDataMap.size() + 1 + "", JSONArray.toJSON(testDataList));
        } else {
            testData = new EllTestData();
            testData.setData(data);
            testData.setScore("");
            testData.setTimestamp(new Date().getTime());
            testData.setIsRetest("2");
            testDataList.add(testData);
            testDataMap.put(testDataMap.size() + 1 + "", JSONArray.toJSON(testDataList));
        }
        return testDataMap;
    }

    public static void main(String[] args) {
        System.out.println("2022-07-01 07:43:58".compareTo("2023-09-29 07:43:58"));
    }
}
