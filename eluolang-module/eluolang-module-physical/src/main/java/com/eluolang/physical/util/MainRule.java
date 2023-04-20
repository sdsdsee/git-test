package com.eluolang.physical.util;

import com.eluolang.common.core.util.StringUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MainRule {
    public static Map<String, String> getRule(String ruleText, String date, HashMap<String, String> scoreMap) {
        DecimalFormat dataFormat = new DecimalFormat("0.00");
        String text = ruleText;
        String num = date;
        StringBuffer ruleMysql = new StringBuffer();
        //分数与等级
        Map<String, String> scoreComment = new HashMap<>();
        //如果没有规则就返回0
        if (ruleText == null || ruleText.equals("")) {
            scoreComment.put("score", "0.00");
            scoreComment.put("comment", "0");
            return scoreComment;
        }
        ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
//等级的下标
        int comment = 0;
        for (String rule : text.split("\n")) {
            rule = rule.trim();
            if (rule.length() == 0) continue;
            //数据库的规则
            ruleMysql.append(rule);
            rule = rule.replaceAll("num", num);
            try {
                /*System.out.println(rule);
                System.out.println(jse.eval(rule));*/
                if (jse.eval(rule).equals(true)) {
                    scoreComment.put("score", dataFormat.format(Double.parseDouble(scoreMap.get(ruleMysql.toString()))));
                    scoreComment.put("comment", String.valueOf(comment));
                    return scoreComment;
                } else {
                    ruleMysql.setLength(0);
                    comment++;
                }
            } catch (Exception t) {
                t.printStackTrace();
            }
        }
        scoreComment.put("score", "0.00");
        scoreComment.put("comment", "0");
        return scoreComment;
    }

    public static void main(String[] args) {
        System.out.println(StringUtils.isNumericSpace("4.96"));
    }
}
