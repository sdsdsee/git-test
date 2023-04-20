package com.eluolang.app.manage.util;

import com.eluolang.app.manage.vo.EllAppHistoryVo;
import com.eluolang.app.manage.vo.EllTestHistoryVo;
import com.eluolang.common.core.pojo.EllTestData;

import java.text.DecimalFormat;
import java.util.List;

public class ScoreDisposeUtil {
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
            ellAppHistoryVo.setComment(scoreComment(Integer.parseInt(score)));
        }
        //视力
        if (testData.size() > 20) {
            ellAppHistoryVo.setScore("-");
            String data = "左:" + testData.get(0).getData() + ",右:" + testData.get(1).getData();
          /*  String data = "左眼裸眼视力:" + testData.get(0).getData() + ",右眼裸眼视力:" + testData.get(1).getData();
            data = data + ",左眼屈光正不正" + testData.get(testData.size() - 2).getData() + ",右眼屈光正不正" + testData.get(testData.size() - 1).getData();
            data = data + ",左眼串镜:-,右眼串镜:-";*/
            ellAppHistoryVo.setData(data);
            ellAppHistoryVo.setUnit(testData.get(0).getUnit());
            //视力评语
            ellAppHistoryVo.setComment(eyesComment);
        } else {
            ellAppHistoryVo.setScore(testData.get(0).getScore());
            ellAppHistoryVo.setData(testData.get(0).getData());
            ellAppHistoryVo.setUnit(testData.get(0).getUnit());
            ellAppHistoryVo.setComment(scoreComment(Integer.parseInt(testData.get(0).getScore())));
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

    public static String endScore(String[] data) {
        String dataEnd = "";
        if (data.length == 3) {
            dataEnd = "身高:" + data[0] + "cm,体重:" + data[1] + "kg,Bmi:" + data[2];
        }
        if (data.length > 20) {
            dataEnd = "左:" + data[0] + ",右:" + data[1];
          /*  dataEnd = "左眼裸眼视力:" + data[0] + ",右眼裸眼视力:" + data[1];
            dataEnd = dataEnd + ",左眼屈光正不正:" + data[data.length - 2] + ",右眼屈光正不正:" + data[data.length - 1];
            dataEnd = dataEnd + ",左眼串镜:-,右眼串镜:-";*/
        }
        return dataEnd;
    }
}
