package com.eluolang.common.core.pojo;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 具体成绩
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JSONType
public class TestData implements Serializable {
    private String remark;
    /**
     * 是否重测成绩
     */
    private String isRetest;
    private String data;
    private String score;
    /**
     * 重测理由
     */
    private String reason;
    /**
     * 单位
     */
    private String unit;
    /**
     * 测试时间蹉
     */
    private Long timestamp;
}
