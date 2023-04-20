package com.eluolang.system.controller;

import com.alibaba.fastjson.JSON;
import com.eluolang.common.core.pojo.FileMgr;
import com.github.pagehelper.PageInfo;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.pojo.OperLog;
import com.eluolang.common.core.util.DateUtils;
import com.eluolang.common.core.util.ExportUtil;
import com.eluolang.common.core.util.PageHelperUtil;
import com.eluolang.common.core.web.Result;
import com.eluolang.common.log.constant.OperObj;
import com.eluolang.common.log.constant.OperType;
import com.eluolang.system.dto.OperLogDto;
import com.eluolang.system.excel.OperLogExcel;
import com.eluolang.system.service.SystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Api(tags = "系统配置")
@RestController
@RequestMapping("/system")
public class SystemController {
    @Autowired
    SystemService systemService;

    @Value("${export.path}")
    private String exportPath;

    /**
     * 保存操作日志
     *
     * @param operLog
     * @return
     */
    @ApiOperation(value = "保存操作日志", notes = "保存操作日志")
    @RequestMapping(value = "/insertLog", method = RequestMethod.POST)
    public Result insertLog(@RequestBody OperLog operLog) {
        int count = systemService.insertLog(operLog);
        if (count > 0) {
            return new Result(HttpStatus.SUCCESS, "保存成功", operLog);
        } else {
            return new Result(HttpStatus.ERROR, "保存失败", operLog);
        }
    }

//    /**
//     * 根据id查询value值
//     *
//     * @param id
//     * @return
//     */
//    @ApiOperation(value = "根据id查询value值", notes = "根据id查询value值")
//    @RequestMapping(value = "/getConfigById", method = RequestMethod.POST)
//    public Result getConfigById(@RequestParam("id") Integer id) {
//        String value = systemService.getConfigById(id);
//        return new Result(HttpStatus.SUCCESS, null, value);
//    }

    /**
     * 按条件查询操作日志
     *
     * @param operLogDto
     * @return
     */
    @ApiOperation(value = "按条件查询操作日志", notes = "按条件查询操作日志")
    @RequestMapping(value = "/getOperLogAll", method = RequestMethod.POST)
    public Result getOperLogAll(OperLogDto operLogDto) {
        //开启分页
        PageHelperUtil.startPage(operLogDto.getPageNum(), operLogDto.getPageSize());
        List<OperLog> operLogList = systemService.getOperLogAll(operLogDto);
        //将数据进行分页
        PageInfo<OperLog> pageInfo = new PageInfo<>(operLogList);
        return new Result(HttpStatus.SUCCESS, null, pageInfo);
    }

    @ApiOperation(value = "导出操作日志")
    @GetMapping(value = "/exportOperLogAll")
    public Result exportOperLogAll(OperLogDto operLogDto) {
        String pathName = exportPath + "操作日志" + System.currentTimeMillis() + ".xlsx";
        List<OperLog> operLogList = systemService.getOperLogAll(operLogDto);
        List<OperLogExcel> operLogExcelList = new ArrayList<>();
        for (OperLog operLog : operLogList) {
            OperLogExcel operLogExcel = new OperLogExcel();
            operLogExcel.setOperName(operLog.getOperName());
            operLogExcel.setOperRealName(operLog.getOperRealName());
            String operTime = DateUtils.timeStamp2Date(operLog.getOperTime() + "", "");
            if (operLog.getOperTime() == null) {
                operLogExcel.setOperTime("");
            } else {
                operLogExcel.setOperTime(operTime);
            }
            operLogExcel.setOperIp(operLog.getOperIp());
            if (operLog.getOperType().equals(OperType.OTHER.ordinal())) {
                operLogExcel.setOperType("其它");
            }
            if (operLog.getOperType().equals(OperType.LOGIN.ordinal())) {
                operLogExcel.setOperType("登录");
            }
            if (operLog.getOperType().equals(OperType.LOGOUT.ordinal())) {
                operLogExcel.setOperType("退出");
            }
            if (operLog.getOperType().equals(OperType.INSERT.ordinal())) {
                operLogExcel.setOperType("新增");
            }
            if (operLog.getOperType().equals(OperType.UPDATE.ordinal())) {
                operLogExcel.setOperType("修改");
            }
            if (operLog.getOperType().equals(OperType.DELETE.ordinal())) {
                operLogExcel.setOperType("删除");
            }
            operLogExcel.setOperResult(operLog.getOperResult());
            operLogExcel.setOperContent(operLog.getOperContent());
            if (operLog.getOperObj().equals(OperObj.SYSTEM.ordinal())) {
                operLogExcel.setOperObj("系统");
            }
            if (operLog.getOperObj().equals(OperObj.DEVICE.ordinal())) {
                operLogExcel.setOperObj("设备");
            }
            operLogExcelList.add(operLogExcel);
        }
        ExportUtil.exportExcel(operLogExcelList, pathName, OperLogExcel.class);
        return new Result(HttpStatus.SUCCESS, "导出操作日志成功！", pathName);
    }

//    private Lock lock = new ReentrantLock();
//
//    /**
//     * 获取指令标识ident
//     *
//     * @return
//     */
//    @ApiOperation(value = "获取指令标识ident", notes = "获取指令标识ident")
//    @RequestMapping(value = "/getIdent", method = RequestMethod.POST)
//    public Result getIdent() {
//        lock.lock();
//        Integer count = 0;
//        try {
//            count = systemService.getIdent();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            lock.unlock();
//            return new Result(HttpStatus.SUCCESS, "获取成功", count);
//        }
//    }
    /**
     * 根据文件id查询文件信息
     *
     * @param fileId
     * @return
     */
    @ApiOperation(value = "根据文件id查询文件信息", notes = "根据文件id查询文件信息")
    @RequestMapping(value = "/getFileInfo", method = RequestMethod.POST)
    public Result getFileInfo(String fileId) {
        return new Result(HttpStatus.SUCCESS, null, JSON.toJSONString(systemService.getFileInfo(fileId)));
    }

    /**
     * 保存文件信息
     *
     * @param fileMgr
     * @return
     */
    @ApiOperation(value = "保存文件信息", notes = "保存文件信息")
    @RequestMapping(value = "/saveFileInfo", method = RequestMethod.POST)
    public Result saveFileInfo(@RequestBody FileMgr fileMgr) {
        return new Result(HttpStatus.SUCCESS, null, systemService.saveFileInfo(fileMgr));
    }
    private Lock lock = new ReentrantLock();
    /**
     * 获取指令标识ident
     *
     * @return
     */
    @ApiOperation(value = "获取指令标识ident", notes = "获取指令标识ident")
    @RequestMapping(value = "/getIdent", method = RequestMethod.POST)
    public Result getIdent() {
        lock.lock();
        Integer count = 0;
        try {
            count = systemService.getIdent();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            return new Result(HttpStatus.SUCCESS, "获取成功", count);
        }
    }
}
