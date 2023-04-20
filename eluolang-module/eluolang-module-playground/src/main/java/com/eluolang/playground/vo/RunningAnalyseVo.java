package com.eluolang.playground.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author dengrunsen
 * @date 2022年10月28日 9:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RunningAnalyseVo {
    /** 日期 */
    private String date;
    /** 平均速度 */
    private String averageSpeed;
    private List<DataBean> dataBeanList;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataBean{
        /** 分段速度 */
        private String sectionSpeed;
        /** 心率 */
        private String heart;
    }
}
