package com.eluolang.playground.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年10月17日 10:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardNumVo {
    /** 计划总人数 */
    private Integer totalNum;
    /** 达标总人数 */
    private Integer standardTotal;
    /** 达标男生人数 */
    private Integer standardMan;
    /** 达标女生人数 */
    private Integer standardWoman;
}
