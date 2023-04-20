package com.eluolang.device.controller;

import com.alibaba.excel.EasyExcel;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.pojo.EllDevice;
import com.eluolang.common.core.pojo.EllWristbandDevice;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.common.core.util.PageHelperUtil;
import com.eluolang.common.core.util.StringUtils;
import com.eluolang.common.core.web.Result;
import com.eluolang.device.Listener.ImportWristbandListener;
import com.eluolang.device.dto.EllWristbandDeviceDto;
import com.eluolang.device.service.DeviceService;
import com.eluolang.device.service.WristbandDeviceService;
import com.eluolang.device.util.EasyExcelColumnTypeUtil;
import com.eluolang.device.util.PathGeneration;
import com.github.pagehelper.PageInfo;
import com.netflix.ribbon.proxy.annotation.Http;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.util.*;

/**
 * @author dengrunsen
 * @date 2022年11月09日 9:59
 */
@Api(tags = "手环设备控制器")
@RestController
@RequestMapping("/wristband")
public class WristbandDeviceController {

    @Autowired
    WristbandDeviceService wristbandDeviceService;

    /**
     * 添加手环设备
     *
     * @return Result
     */
    @ApiOperation(value = "添加手环设备")
    @PostMapping(value = "/addWristbandDevice")
    public Result addWristbandDevice(@RequestBody EllWristbandDevice ellWristbandDevice) {
        Integer count = wristbandDeviceService.selWristbandIsExist(ellWristbandDevice.getNfcId(),ellWristbandDevice.getMac());
        if (count > 0){
            return new Result(HttpStatus.ERROR,"该手环已存在",count);
        }
        ellWristbandDevice.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
        Integer row = wristbandDeviceService.addWristbandDevice(ellWristbandDevice);
        if (row > 0){
            return new Result(HttpStatus.SUCCESS,"添加成功",row);
        }
        return new Result(HttpStatus.ERROR,"添加失败",row);
    }

    /**
     * 删除手环设备
     *
     * @return Result
     */
    @ApiOperation(value = "删除手环设备")
    @DeleteMapping(value = "/deleteWristbandDevice")
    public Result deleteWristbandDevice(String id) {
        int count = wristbandDeviceService.deleteWristbandDevice(id);
        if (count > 0){
            return new Result(HttpStatus.SUCCESS,"删除成功",count);
        }
        return new Result(HttpStatus.ERROR,"删除失败",count);
    }

    /**
     * 修改手环设备信息
     *
     * @return Result
     */
    @ApiOperation(value = "修改手环设备信息")
    @PostMapping(value = "/updateWristbandDevice")
    public Result updateWristbandDevice(@RequestBody EllWristbandDevice ellWristbandDevice) {
        Integer count = wristbandDeviceService.selWristbandIsExist(ellWristbandDevice.getNfcId(),ellWristbandDevice.getMac());
        if (count > 0){
            return new Result(HttpStatus.ERROR,"该手环已存在",count);
        }
        Integer row = wristbandDeviceService.updateWristbandDevice(ellWristbandDevice);
        if (row > 0){
            return new Result(HttpStatus.SUCCESS,"修改成功",row);
        }
        return new Result(HttpStatus.ERROR,"修改失败",row);
    }

    /**
     * 按条件查询手环设备信息
     *
     * @return Result
     */
    @ApiOperation(value = "按条件查询手环设备信息")
    @PostMapping(value = "/selectWristbandDevice")
    public Result selectWristbandDevice(@RequestBody EllWristbandDeviceDto ellWristbandDeviceDto) {
        PageHelperUtil.startPage(ellWristbandDeviceDto.getPageNum(), ellWristbandDeviceDto.getPageSize());
        List<EllWristbandDevice> ellWristbandDeviceList = wristbandDeviceService.selectWristbandDevice(ellWristbandDeviceDto);
        //将设备数据进行分页
        PageInfo<EllWristbandDevice> pageInfo = new PageInfo<>(ellWristbandDeviceList);
        return new Result(HttpStatus.SUCCESS, "查询成功", pageInfo);
    }

    /**
     * 根据NFC查询手环信息
     *
     * @return Result
     */
    @ApiOperation(value = "根据NFC查询手环信息")
    @GetMapping(value = "/selWristbandByNfcId")
    public Result selWristbandByNfcId(String nfcId) {
        EllWristbandDevice ellWristbandDevice = wristbandDeviceService.selWristbandByNfcId(String.valueOf(StringUtils.hexDesToDemical(nfcId)));
        if (ellWristbandDevice != null){
            return new Result(HttpStatus.SUCCESS, "查询成功", ellWristbandDevice);
        }
        return new Result(HttpStatus.ERROR,"查询失败",null);
    }

    @ApiOperation("模板")
    @GetMapping("/template")
    public Result template(HttpServletResponse response) throws ParseException {
        String template = "";
        if (FileUploadUtil.isLinux() == false) {
            template = WindowsSite.template;
        } else {
            template = LinuxSite.template;
        }
        String fileName = "ImportWristbandTemplate2.xlsx";
        File file = new File(template + fileName);
        String path = template + fileName;
        //判断是否有文件路径
        PathGeneration.createPath(path);
        //判断文件是否存在
        if (!file.exists()) {
            List<EllWristbandDevice> ellWristbandDeviceList = new ArrayList<>();
//            EllWristbandDevice ellWristbandDevice = new EllWristbandDevice();
//            ellWristbandDevice.setId("1");
//            ellWristbandDevice.setName("手环xx号");
//            ellUserVo.setEthnic("1");
            /* ellUserVo.setHomeAddress("北京市*****");*/
//            ellUserVo.setStudentId("201811224");
//            ellUserVo.setIdCard("510724********1213");
//            ellWristbandDevice.setMac("xxxxxx");
//            ellWristbandDevice.setNfcId("xxxxx");
//            ellWristbandDevice.setDeptId(12);
//            ellWristbandDeviceList.add(ellWristbandDevice);
            EasyExcel easyExcel = new EasyExcel();
            easyExcel.write(path, EllWristbandDevice.class).registerWriteHandler(new EasyExcelColumnTypeUtil()).sheet("模板").doWrite(ellWristbandDeviceList);
        }
        try (InputStream inputStream = new FileInputStream(file); OutputStream outputStream = response.getOutputStream();) {
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(HttpStatus.SUCCESS, "导出成功");
    }

    private ImportWristbandListener importWristbandListener = null;
    @ApiOperation("批量导入手环信息")
    @PostMapping("/importWristband")
    public Result importWristband(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws Exception {
        try {
            importWristbandListener = new ImportWristbandListener(wristbandDeviceService);
            String fileName = UUID.randomUUID() + ".xlsx";
            if (!file.isEmpty()) {

                //把文件写入到目标位置
                String path = "";
                if (FileUploadUtil.isLinux() == false) {
                    path = WindowsSite.importWristbandPath + fileName;
                } else {
                    path = LinuxSite.importWristbandPath + fileName;
                }
                File filepath = new File(path);
                //判断是否有文件路径
                PathGeneration.createPath(path);
                file.transferTo(filepath);//把文件写入目标文件地址
                EasyExcel.read(path, EllWristbandDevice.class, importWristbandListener).sheet().doRead();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(HttpStatus.ERROR, "失败", e.getCause().getMessage());
        }
        //把错误的信息的表格进行返回
        //判断是否是拥有这个错误信息表格
//            String fileUri = importUserListener.getFileUri();
//            if (fileUri != null && fileUri != "") {
//
//                return new Result(HttpStatus.PARAMETER_ERROR, "一些用户信息有误，错误信息已经生成表格请更改后上传！", fileUri);
//            }
        return new Result(HttpStatus.SUCCESS, "添加成功！");
    }
}
