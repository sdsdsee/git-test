package com.eluolang.common.core.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;

import com.eluolang.common.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.util.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * excel 工具类
 *
 */
@Slf4j
public class EasyExcelUtils {

    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";
    // 文件名分割后的数组长度
    private static final int FILE_SPLIT_LENGTH = 2;

    /**
     * @param is 输入文件的流对象
     * @param clazz 每行数据转换成的对象类
     * @param sheetNo 第几个sheet
     * @param excelListener excel的监听类
     * @return 是否成功, 如果成功可以再监听类里面取数据
     */
    public static Boolean readExcel(InputStream is, Class<?> clazz,
            int sheetNo, AnalysisEventListener excelListener) {
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(is);
            // 解析每行结果在listener中处理
            ExcelReader excelReader = EasyExcelFactory.getReader(bis, excelListener);
            // sheetNo --> 读取哪一个 表单
            /*
             * headLineMun --> 从哪一行开始读取
             * 不包括定义的这一行
             * 比如 headLineMun为1 ，因为excel读取是从0开始，那么headLineMun = 1 实际是从第三行的数据开始读取
             */
            excelReader.read(new Sheet(sheetNo, 1, (Class<? extends BaseRowModel>) clazz));
            // 释放资源
            bis = null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(is);
        }
        return true;
    }

    /**
     * 检查文件类型是否为excel
     *
     * @param fileName fileName
     * @return boolean
     */
    public static boolean checkFileType(String fileName) {
        String[] split = StringUtils.split(fileName, ".");
        if (split.length < FILE_SPLIT_LENGTH) {
            return false;
        }
        String fileType = split[1];
        if (!XLS.equalsIgnoreCase(fileType) && !XLSX.equalsIgnoreCase(fileType)) {
            return false;
        }
        return true;
    }

    /**
     * 动态列头+合并列头
     * @param outputStream
     * @param resultData
     * @param headList
     * @param sheetName
     * @param clazz
     * @param customHandler
     * @throws Exception
     */
    public static void writeExcelAutoHead(OutputStream outputStream, List<List<Object>> resultData, List<List<String>> headList,
            String sheetName, Class clazz, CellWriteHandler customHandler) throws Exception {
        WriteCellStyle headWriteCellStyle = setHeadCellStyle();
        WriteCellStyle contentWriteCellStyle = setContentCellStyle();

        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle,
                contentWriteCellStyle);
        ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcel.write(outputStream, clazz)
                .excelType(ExcelTypeEnum.XLSX).sheet(sheetName).head(headList)
                .registerWriteHandler(horizontalCellStyleStrategy);
        if (customHandler != null) {
            excelWriterSheetBuilder.registerWriteHandler(customHandler);
        }
        excelWriterSheetBuilder.doWrite(resultData);
    }

    /**
     * 导出
     * @param response
     * @param data
     * @param fileName
     * @param sheetName
     * @param clazz
     * @throws Exception
     */
    public static void writeExcel(HttpServletResponse response, List<? extends Object> data, String fileName,
            String sheetName, Class clazz, CellWriteHandler customHandler) throws Exception {
        WriteCellStyle headWriteCellStyle = setHeadCellStyle();
        WriteCellStyle contentWriteCellStyle = setContentCellStyle();

        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle,
                contentWriteCellStyle);

        ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcel.write(getOutputStream(fileName, response), clazz)
                .excelType(ExcelTypeEnum.XLSX).sheet(sheetName)
                .registerWriteHandler(horizontalCellStyleStrategy);
        if (customHandler != null) {
            excelWriterSheetBuilder.registerWriteHandler(customHandler);
        }
        excelWriterSheetBuilder.doWrite(data);

    }

    private static WriteCellStyle setContentCellStyle() {
        //内容样式
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.
        // 头默认了 FillPatternType所以可以不指定
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        // 设置内容字体
        WriteFont contentFont = new WriteFont();
        contentFont.setFontHeightInPoints((short) 10);
        contentWriteCellStyle.setWriteFont(contentFont);
        //设置内容靠左对齐
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        //设置内容自动换行
        contentWriteCellStyle.setWrapped(false);
        //设置边框样式
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        return contentWriteCellStyle;
    }

    private static WriteCellStyle setHeadCellStyle() {
        //表头样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为白色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        // 表头的字体属性设置
        WriteFont headFont = new WriteFont();
        // 表头字体及大小
        // headFont.setFontName(DEFAULT_FONT_ARIAL);
        headFont.setFontHeightInPoints((short) 10);
        // 是否加粗
        headFont.setBold(true);
        headWriteCellStyle.setWriteFont(headFont);
        //设置表头居中对齐
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        return headWriteCellStyle;
    }

    public static OutputStream getOutputStream(String fileName, HttpServletResponse response) throws Exception {
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        return response.getOutputStream();
    }
}
