package com.eluolang.physical.util;

import com.eluolang.common.core.pojo.EllTestData;
import com.eluolang.common.core.pojo.EllTestProject;
import com.eluolang.common.core.pojo.EllUser;
import com.eluolang.physical.model.EllHistoryVo;

import java.text.DecimalFormat;
import java.util.*;

/**
 * 随机生成成绩
 */
public class RandomGeneration {
    public static EllHistoryVo heightAndWeight(EllUser user, EllTestProject ellTestProject, EllHistoryVo ellHistoryVo) {
        //格式话数字小数点位数
        try {
            DecimalFormat dataFormat = new DecimalFormat("0.0");
            //去掉单位并且把数子变为小数
            if (!user.getAppHeight().contains(".")) {
                user.setAppHeight(user.getAppHeight().replaceAll("cm", ""));
                user.setAppHeight(user.getAppHeight().replaceAll(" ", ""));
                user.setAppHeight(user.getAppHeight().replaceAll("厘米", ""));
                user.setAppHeight(user.getAppHeight() + ".00");
            }
            if (!user.getAppWeight().contains(".")) {
                user.setAppWeight(user.getAppWeight().replaceAll("kg", ""));
                user.setAppWeight(user.getAppWeight().replaceAll("公斤", ""));
                user.setAppWeight(user.getAppWeight().replaceAll(" ", ""));
                user.setAppWeight(user.getAppWeight() + ".00");
            }
            //计算身高体重的bmi值
            Double BMI = Double.parseDouble(dataFormat.format(Double.parseDouble(user.getAppWeight()) / Math.pow(Double.parseDouble(user.getAppHeight()) / 100.0, 2)));
            Map<String, List<EllTestData>> testData = new HashMap<>();
            ellHistoryVo.setData(user.getAppHeight() + "," + user.getAppWeight() + "," + BMI);
            List<EllTestData> ellTestDataList = new ArrayList<>();
            EllTestData ellTestDataHeight = new EllTestData();
            ellTestDataHeight.setData(user.getAppHeight());
            ellTestDataHeight.setIsRetest("1");
            ellTestDataHeight.setRemark("身高第1次");
            ellTestDataHeight.setTimestamp(new Date().getTime());
            ellTestDataList.add(ellTestDataHeight);
            EllTestData ellTestDataWeight = new EllTestData();
            ellTestDataWeight.setData(user.getAppWeight());
            ellTestDataWeight.setIsRetest("1");
            ellTestDataWeight.setRemark("体重第1次");
            ellTestDataWeight.setTimestamp(new Date().getTime());
            ellTestDataList.add(ellTestDataWeight);
            EllTestData ellTestDataBmi = new EllTestData();
            ellTestDataBmi.setData(String.valueOf(BMI));
            ellTestDataBmi.setIsRetest("1");
            ellTestDataBmi.setRemark("BMI第1次");
            ellTestDataBmi.setTimestamp(new Date().getTime());
            ellTestDataList.add(ellTestDataBmi);
            testData.put("1", ellTestDataList);
            ellHistoryVo.setTestData(testData);

        } catch (Exception e) {
            System.out.println(e);
        }
        return ellHistoryVo;
    }

    public static EllHistoryVo pro50(EllUser user, EllTestProject ellTestProject, EllHistoryVo ellHistoryVo) {
        Map<String, List<EllTestData>> testData = new HashMap<>();
        Double socre=6.2+Math.random()*3.3;
        if (user.getUserSex().equals("2")){
            socre=7.0+Math.random()*3.5;
        }
        DecimalFormat dataFormat = new DecimalFormat("0.0");
        ellHistoryVo.setData(dataFormat.format(socre));
        List<EllTestData> ellTestDataList = new ArrayList<>();
        EllTestData ellTestData50 = new EllTestData();
        ellTestData50.setData(dataFormat.format(socre));
        ellTestData50.setIsRetest("1");
        ellTestData50.setRemark("短跑(50米)");
        ellTestData50.setTimestamp(new Date().getTime());
        ellTestDataList.add(ellTestData50);
        testData.put("1", ellTestDataList);
        ellHistoryVo.setTestData(testData);
        return ellHistoryVo;
    }
    public static EllHistoryVo pro800(EllUser user, EllTestProject ellTestProject, EllHistoryVo ellHistoryVo) {
        Map<String, List<EllTestData>> testData = new HashMap<>();
        int socre=(int)(185+Math.random()*93);
        ellHistoryVo.setData(String.valueOf(socre));
        List<EllTestData> ellTestDataList = new ArrayList<>();
        EllTestData ellTestData50 = new EllTestData();
        ellTestData50.setData(String.valueOf(socre));
        ellTestData50.setIsRetest("1");
        ellTestData50.setRemark("长跑(800米)");
        ellTestData50.setTimestamp(new Date().getTime());
        ellTestDataList.add(ellTestData50);
        testData.put("1", ellTestDataList);
        ellHistoryVo.setTestData(testData);
        return ellHistoryVo;
    }
    public static EllHistoryVo pro1000(EllUser user, EllTestProject ellTestProject, EllHistoryVo ellHistoryVo) {
        Map<String, List<EllTestData>> testData = new HashMap<>();
        int socre=(int)(190+Math.random()*94);
        ellHistoryVo.setData(String.valueOf(socre));
        List<EllTestData> ellTestDataList = new ArrayList<>();
        EllTestData ellTestData50 = new EllTestData();
        ellTestData50.setData(String.valueOf(socre));
        ellTestData50.setIsRetest("1");
        ellTestData50.setRemark("长跑(1000米)");
        ellTestData50.setTimestamp(new Date().getTime());
        ellTestDataList.add(ellTestData50);
        testData.put("1", ellTestDataList);
        ellHistoryVo.setTestData(testData);
        return ellHistoryVo;
    }
    public static EllHistoryVo proLdty(EllUser user, EllTestProject ellTestProject, EllHistoryVo ellHistoryVo) {
        Map<String, List<EllTestData>> testData = new HashMap<>();
        int socre=(int)(210+Math.random()*80);
        if (user.getUserSex().equals("2")){
            socre= (int)(160+Math.random()*80);
        }
        ellHistoryVo.setData(String.valueOf(socre));
        List<EllTestData> ellTestDataList = new ArrayList<>();
        EllTestData ellTestData50 = new EllTestData();
        ellTestData50.setData(String.valueOf(socre));
        ellTestData50.setIsRetest("1");
        ellTestData50.setRemark("立定跳远");
        ellTestData50.setTimestamp(new Date().getTime());
        ellTestDataList.add(ellTestData50);
        testData.put("1", ellTestDataList);
        ellHistoryVo.setTestData(testData);
        return ellHistoryVo;
    }
    public static EllHistoryVo proFeihuoliang(EllUser user, EllTestProject ellTestProject, EllHistoryVo ellHistoryVo) {
        Map<String, List<EllTestData>> testData = new HashMap<>();
        int socre=(int)(3000+Math.random()*2000);
        if (user.getUserSex().equals("2")){
            socre= (int)(1800+Math.random()*1800);
        }
        ellHistoryVo.setData(String.valueOf(socre));
        List<EllTestData> ellTestDataList = new ArrayList<>();
        EllTestData ellTestData50 = new EllTestData();
        ellTestData50.setData(String.valueOf(socre));
        ellTestData50.setIsRetest("1");
        ellTestData50.setRemark("肺活量");
        ellTestData50.setTimestamp(new Date().getTime());
        ellTestDataList.add(ellTestData50);
        testData.put("1", ellTestDataList);
        ellHistoryVo.setTestData(testData);
        return ellHistoryVo;
    }
    public static EllHistoryVo proYtxs(EllUser user, EllTestProject ellTestProject, EllHistoryVo ellHistoryVo) {
        Map<String, List<EllTestData>> testData = new HashMap<>();
        int socre=(int)(10+Math.random()*9);
        ellHistoryVo.setData(String.valueOf(socre));
        List<EllTestData> ellTestDataList = new ArrayList<>();
        EllTestData ellTestData50 = new EllTestData();
        ellTestData50.setData(String.valueOf(socre));
        ellTestData50.setIsRetest("1");
        ellTestData50.setRemark("引体向上");
        ellTestData50.setTimestamp(new Date().getTime());
        ellTestDataList.add(ellTestData50);
        testData.put("1", ellTestDataList);
        ellHistoryVo.setTestData(testData);
        return ellHistoryVo;
    }
    public static EllHistoryVo proYwqz(EllUser user, EllTestProject ellTestProject, EllHistoryVo ellHistoryVo) {
        Map<String, List<EllTestData>> testData = new HashMap<>();
        int socre=(int)(35+Math.random()*40);
        ellHistoryVo.setData(String.valueOf(socre));
        List<EllTestData> ellTestDataList = new ArrayList<>();
        EllTestData ellTestData50 = new EllTestData();
        ellTestData50.setData(String.valueOf(socre));
        ellTestData50.setIsRetest("1");
        ellTestData50.setRemark("仰卧起坐");
        ellTestData50.setTimestamp(new Date().getTime());
        ellTestDataList.add(ellTestData50);
        testData.put("1", ellTestDataList);
        ellHistoryVo.setTestData(testData);
        return ellHistoryVo;
    }
    public static EllHistoryVo proTqq(EllUser user, EllTestProject ellTestProject, EllHistoryVo ellHistoryVo) {
        Map<String, List<EllTestData>> testData = new HashMap<>();
        Double socre=(0.7+Math.random()*20.5);
        if (user.getUserSex().equals("2")){
            socre= (4.4+Math.random()*22.0);
        }
        DecimalFormat dataFormat = new DecimalFormat("0.0");
        ellHistoryVo.setData(dataFormat.format(socre));
        List<EllTestData> ellTestDataList = new ArrayList<>();
        EllTestData ellTestData50 = new EllTestData();
        ellTestData50.setData(dataFormat.format(socre));
        ellTestData50.setIsRetest("1");
        ellTestData50.setRemark("坐位体前屈");
        ellTestData50.setTimestamp(new Date().getTime());
        ellTestDataList.add(ellTestData50);
        testData.put("1", ellTestDataList);
        ellHistoryVo.setTestData(testData);
        return ellHistoryVo;
    }
    public static void main(String[] args) {
        DecimalFormat dataFormat = new DecimalFormat("0.0");
//        Double BMI = Double.parseDouble(dataFormat.format(Double.parseDouble("168") / Math.pow(Double.parseDouble("110") / 100.0, 2)));
        System.out.println(3500-1900);
        Double d=6.7+Math.random()*5.3;
        System.out.println(dataFormat.format(d));
    }
}
