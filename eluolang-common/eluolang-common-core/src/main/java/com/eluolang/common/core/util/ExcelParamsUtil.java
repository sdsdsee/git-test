package com.eluolang.common.core.util;

import cn.afterturn.easypoi.excel.entity.ImportParams;

/**
 * @description: 导入导出的params设置
 * @author: xiongde
 * @create: 2021-04-27
 **/
public class ExcelParamsUtil {

    /**
     * 标题行为0，头行1，即从第二行开始解析
     * @return
     */
    public static ImportParams buildNormalImportParams() {
        return buildImportParamsFromPointRow(0, 1, true);
    }

    /**
     * 备注行为0，标题行为1，头行2，即从第三行开始解析
     * @return
     */
    public static ImportParams buildImportParamsFromThreeRow() {
       return buildImportParamsFromPointRow(1, 1, true);
    }

    /**
     * 标题行为0，头行1，即从第二行开始解析
     * @return
     */
    private static ImportParams buildImportParamsFromPointRow(int titleRows, int headRows, boolean needVerfiy) {
        ImportParams params = new ImportParams();
        //设置标题行
        params.setTitleRows(titleRows);
        //设置头行
        params.setHeadRows(headRows);
        //设置校验
        params.setNeedVerfiy(needVerfiy);
        return params;
    }
}
