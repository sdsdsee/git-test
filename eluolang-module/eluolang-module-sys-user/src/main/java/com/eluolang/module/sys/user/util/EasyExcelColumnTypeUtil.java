package com.eluolang.module.sys.user.util;

import com.alibaba.excel.constant.BuiltinFormats;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.AbstractVerticalCellStyleStrategy;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.BorderStyle;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.Locale;

public class EasyExcelColumnTypeUtil extends AbstractVerticalCellStyleStrategy {
    private static WriteHandler writeHandler;

    @Override
    protected WriteCellStyle headCellStyle(Head head) {
        WriteCellStyle writeCellStyle = new WriteCellStyle();
        //字体
        WriteFont writeFont = new WriteFont();
        writeFont.setFontHeightInPoints((short) 11);
        writeFont.setBold(true);
        writeFont.setColor(IndexedColors.RED.getIndex());
        writeCellStyle.setWriteFont(writeFont);
        //边框
        writeCellStyle.setBorderBottom(BorderStyle.THIN);
        writeCellStyle.setBorderLeft(BorderStyle.THIN);
        writeCellStyle.setBorderRight(BorderStyle.THIN);
        //前景色
        writeCellStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        //是否换行
        writeCellStyle.setWrapped(true);
        writeCellStyle.setLocked(true);
        return writeCellStyle;
    }

    //这里单独设置某一列的头部格式
    @Override
    protected WriteCellStyle contentCellStyle(Head head) {
        Integer columnIndex = head.getColumnIndex();
        //这里只单独设置第5列
        if (columnIndex == 5) {
            //设置单元格格式
            WriteCellStyle writeCellStyle = new WriteCellStyle();
            //设置单元格格式
            writeCellStyle.setDataFormat((short) 49);
            return writeCellStyle;
        }
        return new WriteCellStyle();
    }

    //设置每行的数据的格式
    public HorizontalCellStyleStrategy setHorizontalType() {
        //设置单元格格式
        WriteCellStyle writeCellStyle = new WriteCellStyle();
        //设置单元格格式
        writeCellStyle.setDataFormat((short) 49);
        return new HorizontalCellStyleStrategy(writeCellStyle, writeCellStyle);
    }

    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            String c = BuiltinFormats.getBuiltinFormat(i, "YYYY-MM", new Locale("宋体"));
            System.out.println(c);
        }

    }
}
