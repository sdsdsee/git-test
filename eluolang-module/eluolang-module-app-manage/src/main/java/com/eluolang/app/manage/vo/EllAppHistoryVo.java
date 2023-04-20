package com.eluolang.app.manage.vo;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JSONType
public class EllAppHistoryVo {
    /**
     * 成绩
     */
    private String data;
    /**
     * 单位
     */
    private String unit;
    /**
     * 分数
     */
    private String score;
    /**
     * 评价
     */
    private String comment;
}
