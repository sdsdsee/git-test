package com.eluolang.physical.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.Constants;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.text.UUID;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.common.core.web.Result;
import com.eluolang.physical.Listener.BatchOptionalListener;
import com.eluolang.physical.dto.DepartTreeDto;
import com.eluolang.physical.model.*;
import com.eluolang.physical.service.EllUserService;
import com.eluolang.physical.service.PlanService;
import com.eluolang.physical.util.CreateTime;
import com.eluolang.physical.util.PathGeneration;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

@Api(tags = "计划管理")
@RestController
@Transactional
public class PlanController {
    @Autowired
    private PlanService planService;
    @Autowired
    private EllUserService ellUserService;
    @Value("${RSA.public}")//公钥
    private String RsaPublic;
    @Value("${RSA.private}")//私钥
    private String RsaPrivate;

    @ApiOperation("添加计划")
    @PostMapping("/createPlan")
    public Result createPlan(@RequestBody PlanVo planVo) throws ParseException {
        int c = planVo.getDateEnd().compareTo(planVo.getDateBegin());
        //判断男女的自选项够不够
        int numGirl = 0;
        int numBoy = 0;
        if (planVo.getCusTheNumber() > 0) {
            for (int i = 0; i < planVo.getCreateChanceVos().size(); i++) {
                if (planVo.getCreateChanceVos().get(i).getEssential().equals("2")) {
                    boolean statusBoy = planVo.getCreateChanceVos().get(i).getUseSex().contains("1");
                    boolean statusGirl = planVo.getCreateChanceVos().get(i).getUseSex().contains("2");
                    if (statusBoy) {
                        numBoy++;
                    }
                    if (statusGirl) {
                        numGirl++;
                    }
                }
            }
            if (numBoy < planVo.getCusTheNumber() || numGirl < planVo.getCusTheNumber()) {
                return new Result(HttpStatus.INSUFFICIENT_DEFICIENCY, "男女自选项不足，请添加");
            }
        }

        if (c > 0 && planVo.getDateBegin().compareTo(CreateTime.timeDay()) > -1) {
//            int count = planService.findProceedPlan(planVo.getPlanState() + "", planVo.getDateBegin(), planVo.getOrgId());
            int i = planService.createPlan(planVo, planVo.getCreateChanceVos());
            if (i > 0) {
                System.out.println("创建成功！");
                return Result.SUCCESS();
            } else {
                System.out.println("创建失败！");
                return Result.ERROR();
            }
        } else {
            System.out.println("结束时间小于开始时间");
            return new Result(HttpStatus.BAD_REQUEST, "开始时间大于结束时间或开始时间小于当前时间", null);
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "计划id", dataType = "String", required = false),
            @ApiImplicitParam(name = "orgId", value = "使用路径例如/1/1001", dataType = "String", required = false),
            @ApiImplicitParam(name = "state", value = "1待发布2正在进行3已结束", dataType = "String", required = false),
            @ApiImplicitParam(name = "isSubscribe", value = "是否预约1是2否", dataType = "String", required = false)
    })
    @ApiOperation("查询计划管理员")
    @GetMapping("/findPlan")
    public Result findPlan(String id, String orgId, String state, String isSubscribe) {
        List<EllPlan> ellPlans = planService.findPlan(id, orgId, state, isSubscribe);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("plan", ellPlans);
        return new Result(HttpStatus.SUCCESS, "条件查询成功!", ellPlans);
    }

    @ApiOperation("查询计划(视频下载器)")
    @GetMapping("/findPlanVideo")
    public Result findPlanVideo(String id, String orgId, String state, String isSubscribe, Integer yearItem) {
        int[] yearNum = {0, 0, 3, 5, -1};
        //是否通过id查找
        if (id != null && id != "") {
            yearNum = new int[]{-2, -2, -2, -2};
        }
        List<EllPlan> ellPlans = planService.findPlanVideo(id, orgId, state, isSubscribe, yearNum[yearItem], yearNum[yearItem] >= 5 ? 0 : yearNum[yearItem + 1]);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("plan", ellPlans);
        return new Result(HttpStatus.SUCCESS, "条件查询成功!", ellPlans);
    }

    @ApiOperation("查询计划用户")
    @GetMapping(value = "findOrgPlan" ,produces = {"application/xml;charset=UTF-8"})
    @ApiImplicitParams({@ApiImplicitParam(name = "orgId", value = "部门id", dataType = "String", required = false), @ApiImplicitParam(name = "page", value = "页码", dataType = "int", required = true), @ApiImplicitParam(name = "isExam", value = "是否考试", dataType = "int", required = true)})
    public Result findOrgPlan(String orgId, int page, int isExam, int accountOrgId, Integer status) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        List<EllPlanVo> list = planService.findOrgPlan(page, orgId, isExam, accountOrgId, status);
        for (int i = 0; i < list.size(); i++) {
//开始之前先判断计划是否添加使用部门
            List<EllUseOrg> orgList = planService.findPlanUseOrg(list.get(i).getId(), 0);
            if (orgList.size() < 1) {
                //为完成
                list.get(i).setConfigState(2);
            } else {
                //已完成
                list.get(i).setConfigState(1);
            }
        }
        HashMap<String, List<EllTestAppointment>> stringListHashMap = new HashMap<>();
        //返回计划算分占比
        Map<String, List<EllPlanScoresOfVo>> listPlanScoresOfMap = new HashMap<>();
        Map<String, List<EllPlanProjectChance>> listMap = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            //判断是否是测试计划查询不同的东西
            if (isExam == 2) {
                //判断测试计划是否有预约时间
                if (list.get(i).getIsSubscribe().equals("1")) {
                    //测试计划查询预约时间
                    List<EllTestAppointment> ellTest = planService.findAppointment(list.get(i).getId());
                    if (ellTest.size() > 0) {
                        stringListHashMap.put(ellTest.get(0).getPlanId(), ellTest);
                    }
                }
            } else {
                //考试计划获取这次计划的考试计划分数占比
                List<EllPlanScoresOfVo> ellPlanScoresOfVos = planService.findEllPlanScoreOf(list.get(i).getId());
                listPlanScoresOfMap.put(list.get(i).getId(), ellPlanScoresOfVos);
            }
            List<EllPlanProjectChance> chance = planService.findProChance(list.get(i).getId(), true);
            listMap.put(list.get(i).getId(), chance);
        }

        PageInfo pageInfo = new PageInfo<>(list);
        List<EllPlanStatusNumVo> statusNum = planService.selPlanStatusNum(orgId, isExam, accountOrgId, null);
        jsonObject.put("listPlanScoresOfMap", listPlanScoresOfMap);
        jsonObject.put("plan", list);
        jsonObject.put("chance", listMap);
        jsonObject.put("size", pageInfo.getTotal());
        jsonObject.put("ellTestAppointment", stringListHashMap);
        jsonObject.put("statusNum", statusNum);
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }

    @ApiOperation("查询计划用户项目次数")
    @GetMapping("/findProChance")
    @ApiImplicitParams({@ApiImplicitParam(name = "planId", value = "部门id", dataType = "String", required = true),})

    public Result findProChance(String planId) throws ParseException {
        //判断查询视力项目不
        List<EllPlanProjectChance> list = planService.findProChance(planId, true);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("chance", list);
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }

    @ApiOperation("修改计划")
    @PutMapping("/updatePlan")
    public Result updatePlan(@RequestBody UpdatePlanVo updatePlanVo) throws ParseException, SQLException {
        //判断男女的自选项够不够
        int numGirl = 0;
        int numBoy = 0;
        if (updatePlanVo.getCusTheNumber() > 0) {
            for (int i = 0; i < updatePlanVo.getCreateChanceVos().size(); i++) {
                if (updatePlanVo.getCreateChanceVos().get(i).getEssential().equals("2")) {
                    boolean statusBoy = updatePlanVo.getCreateChanceVos().get(i).getUseSex().contains("1");
                    boolean statusGirl = updatePlanVo.getCreateChanceVos().get(i).getUseSex().contains("2");
                    if (statusBoy) {
                        numBoy++;
                    }
                    if (statusGirl) {
                        numGirl++;
                    }
                }
            }
            if (numBoy < updatePlanVo.getCusTheNumber() || numGirl < updatePlanVo.getCusTheNumber()) {

                return new Result(HttpStatus.INSUFFICIENT_DEFICIENCY, "男女自选项不足，请添加");
            }
        }
        int i = planService.updatePlan(updatePlanVo);
        return Result.SUCCESS();
    }

    @ApiOperation("修改机会次数")
    @PostMapping("/updateProChance")
    public Result updateProChance(String id, int chance, String updateById, String useSex, int time, String essential, String proportion) throws ParseException {
        int i = planService.updateProChance(id, chance, updateById, useSex, time, essential, proportion);
        if (i > 0) {
            new Result(HttpStatus.SUCCESS, "修改成功");
        }
        return new Result(HttpStatus.ERROR, "修改失败");
    }

    @ApiOperation("物理删除计划")
    @DeleteMapping("/deletePlanSys")
    public Result deletePlanSys(int id) {
        int i = planService.deletePlanSys(id);
        if (i > 0) {
            return Result.SUCCESS();
        }
        return Result.ERROR();
    }

    @ApiOperation("逻辑删除计划")
    @DeleteMapping("/deletePlan")
    public Result deletePlan(String id, String createById) {
        int i = planService.deletePlan(id, createById);
        if (i > 0) {
            return Result.SUCCESS();
        }
        return Result.ERROR();
    }

    @ApiOperation("修改状态")
    @PostMapping("/finishPlan")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", required = true), @ApiImplicitParam(name = "planState", value = "1.待发布2.正在进行3.结束状态", dataType = "String", required = true),})
    public Result finishPlan(String id, String planState, String createById, String orgId) throws ParseException {
        //发布先判断发布时间是否大于现在时间
        List<EllPlan> ellPlans = planService.findPlan(id, null, null, null);
        if (ellPlans != null && ellPlans.size() > 0 && ellPlans.get(0).getDateEnd().compareTo(CreateTime.timeDay()) <= 0) {
            return new Result(HttpStatus.ERROR, "计划测试时间应该大于现在的时间");
        }
        //开始之前先判断计划是否添加使用部门
        List<EllUseOrg> orgList = planService.findPlanUseOrg(id, 0);
        if (orgList.size() < 1) {
            return new Result(HttpStatus.PLAN_NOT_SET, "未添加使用部门以及部门所使用对应的规则!");
        }
        int i = planService.finishPlan(id, planState, createById, orgId);
       /* if (i == HttpStatus.ALREADY_EXISTS) {
            return new Result(HttpStatus.ALREADY_EXISTS, "已经拥有发布计划");
        }*/
        if (i > 0) {
            return Result.SUCCESS();
        }
        return Result.ERROR();
    }

    @ApiOperation("修改使用部门")
    @PostMapping("/updateUseOrg")
    public Result updateUseOrg(@RequestBody List<EllUseOrgVo> ellUseOrgs) throws ParseException {/*
        StringBuffer stringBuffer = new StringBuffer();
        int count = 0;
        for (int i = 0; i < ellUseOrgs.size(); i++) {
            int num = planService.findPlanIssue(ellUseOrgs.get(i).getPlanId(), String.valueOf(ellUseOrgs.get(i).getOrgId()));
            if (num > 0) {
                count++;
                stringBuffer.append("/" + ellUseOrgs.get(i).getOrgName());
            }
        }
        if (count > 0) {
            return new Result(HttpStatus.ALREADY_EXISTS, stringBuffer.toString() + "以及存在其他的正在计划中");
        }*/

        int i = planService.updateUseOrg(ellUseOrgs);
        if (i > 0) {
            return Result.SUCCESS();
        }
        return Result.ERROR();
    }

    @ApiOperation("添加使用部门:测试使用")
    @PostMapping("/addUseOrg")
    public Result addUseOrg(String planId, String orgId, String createById, String useRule, String parentId) throws ParseException {
        int i = planService.addUseOrg(planId, orgId, createById, useRule, parentId);
        if (i > 0) {
            return Result.SUCCESS();
        }

        return Result.ERROR();
    }

    @ApiOperation("查询使用部门使用的规则")
    @PostMapping("/findOrgPlanRules")
    @ApiImplicitParams({@ApiImplicitParam(name = "planId", value = "计划id", required = true),})
    public Result findOrgPlan(String planId) throws ParseException {
        List<EllUseOrg> list = planService.findPlanUseOrg(planId, 1);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("usePlanOrg", list);
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }

    @ApiOperation("查询所有使用部门")
    @PostMapping("/findAllOrgPlan")
    @ApiImplicitParams({@ApiImplicitParam(name = "planId", value = "计划id", required = true),})
    public Result findAllOrgPlan(String planId) throws ParseException {
        List<EllUseOrg> list = planService.findPlanUseOrg(planId, 0);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("usePlanOrg", list);
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }

    @ApiOperation("查询自选项目")
    @GetMapping("/findOptionalProject")
    public Result findOptionalProject(String planId, String sex) {
        List<FindOptionalProjectVo> optionalProjectVoList = planService.findOptionalProject(planId, sex, 0);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("optionalProjectVoList", optionalProjectVoList);
        if (optionalProjectVoList.size() > 0) {
            return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
        }
        return new Result(HttpStatus.ERROR, "失败");
    }

    @ApiOperation("后台导入学生选择项目")
    @PostMapping("/batchOptionalPro")
    public Result batchOptionalPro(@RequestParam("file") MultipartFile file, String planId) throws ParseException {
        BatchOptionalListener batchOptionalListener = new BatchOptionalListener(planService, planId, ellUserService, RsaPrivate);
        String fileName = CreateTime.timeDay() + IdUtils.fastSimpleUUID() + ".xlsx";
        if (!file.isEmpty()) {
            try {
                //把文件写入到目标位置
                String path = "";
                if (FileUploadUtil.isLinux() == false) {
                    path = WindowsSite.BATCH_OPTIONAL_PROJECT_PATH + fileName;
                } else {
                    path = LinuxSite.BATCH_OPTIONAL_PROJECT_PATH + fileName;
                }
                File filepath = new File(path);
                //判断是否有文件路径
                PathGeneration.createPath(path);
                file.transferTo(filepath);//把文件写入目标文件地址
                EasyExcel.read(path, BatchOptionalVo.class, batchOptionalListener).sheet().doRead();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e);
                return new Result(HttpStatus.ERROR, "失败", e);
            }
        }
        JSONObject jsonObject = new JSONObject();
        if (batchOptionalListener.getFileUri() != null) {
            jsonObject.put("dataError", batchOptionalListener.getFileUri());
            return new Result(HttpStatus.PARTIAL_DATA_ERROR, "部分数据错误，已经返回为excel！", batchOptionalListener.getFileUri());
        }
        return new Result(HttpStatus.SUCCESS, "成功");
    }

    @ApiOperation("后台导入学生选择项目模板")
    @PostMapping("/batchOptionalProTemplate")
    public Result batchOptionalProTemplate(HttpServletResponse response) {

        String path = "";
        if (FileUploadUtil.isLinux() == false) {
            path = WindowsSite.BATCH_MODEL;
        } else {
            path = LinuxSite.BATCH_MODEL;
        }
        File file = new File(path + "template.xlsx");
        //判断是否有文件路径
        PathGeneration.createPath(path + "template.xlsx");
        //判断文件是否存在
        if (!file.exists()) {
            EasyExcel.write(file, BatchOptionalVo.class).sheet("模板").doWrite(null);
        }
        try (InputStream inputStream = new FileInputStream(file); OutputStream outputStream = response.getOutputStream();) {
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(HttpStatus.SUCCESS, "成功");
    }

    @ApiOperation("查询已选项目")
    @GetMapping("/findSelectedOptionalProject")
    public Result findSelectedOptionalProject(String planId, String userId) {
        List<EllFindSelectedPro> optionalProjectVoList = planService.findProjectOptional(planId, userId, 0);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("optionalProjectVoList", optionalProjectVoList);
        if (optionalProjectVoList.size() > 0) {
            return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
        }
        return new Result(HttpStatus.ERROR, "失败");
    }

    @ApiOperation("查询计划使用部门")
    @GetMapping("/getPlanDept")
    public Result getPlanDept(String planId, String accountId) {
        List<EllUserPlanDepartment> userPlanDepartmentList = planService.selPlanUseDepartment(accountId, planId);
        List<DepartTreeDto> listList = new ArrayList<>();
        Map<Integer, String> duplicateRemoval = new HashMap<>();
        for (int i = 0; i < userPlanDepartmentList.size(); i++) {
            //不添加父根和别人子根一样的情况
            if (!duplicateRemoval.containsKey(userPlanDepartmentList.get(i).getOrgId())) {
                List<DepartTreeDto> res = planService.selPlanDepart(accountId, planId, userPlanDepartmentList.get(i).getOrgId());
                List<DepartTreeDto> departVos = planService.selPlanDepart(accountId, planId, userPlanDepartmentList.get(i).getOrgId());
                List<DepartTreeDto> dest = new ArrayList<>();
                dest.add(departVos.get(0));
                genTreeByDg(res, dest);
                listList.add(dest.get(0));
                //查询到下级部门保存进行去重
                List<EllUseOrgVo> ellUseOrgVoList = planService.selPfDepart(userPlanDepartmentList.get(i).getOrgId());
                for (int j = 0; j < ellUseOrgVoList.size(); j++) {
                    duplicateRemoval.put(ellUseOrgVoList.get(j).getOrgId(), ellUseOrgVoList.get(j).getOrgName());
                }
            }
        }
        return new Result(HttpStatus.SUCCESS, "获取组织机构信息成功", listList);
    }

    private void genTreeByDg(List<DepartTreeDto> res, List<DepartTreeDto> dest) {

        if (dest.isEmpty()) {
            return;
        }

        renderLastLevel(res);
        renderLastLevel(dest);
        DepartTreeDto.genTreeByDg(res, dest);
    }

    private void renderLastLevel(List<DepartTreeDto> dest) {
        for (DepartTreeDto dv : dest) {
            if ("1".equals(dv.getLastLevel())) {
                dv.setLastLevelFmt("是");
            } else {
                dv.setLastLevelFmt("否");
            }
        }
    }

    @ApiOperation("查询计划是否选考")
    @GetMapping("/getPlanSelectBy")
    public Result getPlanSelectBy(String planId, String userId) {
        EllPlan ellPlan = planService.selPlan(planId);
        if (ellPlan == null || ellPlan.getCustomizeTheNumber() <= 0) {
            return new Result(HttpStatus.ERROR, "此考试不用选考项目");
        }
        EllUser ellUser = ellUserService.selUserById(userId);
        //必考
        List<EllPlanProjectChance> ellPlanProjectChanceList = planService.findMustProject(planId, null);
        //选考
        List<FindOptionalProjectVo> optionalProjectVoList = planService.findOptionalProject(planId, String.valueOf(ellUser.getUserSex()), 0);
        for (int i = 0; i < optionalProjectVoList.size(); i++) {
            optionalProjectVoList.get(i).setNum(planService.selOptionPeopleNum(planId, optionalProjectVoList.get(i).getProId()));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("option", optionalProjectVoList);
        jsonObject.put("must", ellPlanProjectChanceList);
        return new Result(HttpStatus.SUCCESS, "获取组织机构信息成功", jsonObject);
    }

    @ApiOperation("选考")
    @PostMapping("/getPlanSelectOption")
    public Result getPlanSelectOption(@RequestBody BatchOptionalVo batchOptionalVo) {
        List<EllReturnScoreVo> ellReturnScoreVo = ellUserService.findUserById(batchOptionalVo.getUserId());
        List<BatchOptionalVo> batchOptionalVoList = new ArrayList<>();
        //分割上传自选的项目
        String[] porId;
        if (batchOptionalVo.getProId().contains(",")) {
            porId = batchOptionalVo.getProId().split(",");
        } else {
            porId = new String[1];
            porId[0] = batchOptionalVo.getProId();
        }
        for (int j = 0; j < porId.length; j++) {
            //查看此人可以选择该项项目吗
            List<FindOptionalProjectVo> optionalProjectVoList = planService.findOptionalProject(batchOptionalVo.getPlanId(), String.valueOf(ellReturnScoreVo.get(0).getUserSex()), Integer.parseInt(porId[j]));
            //代表可选项中有这个项目
            if (optionalProjectVoList.size() > 0) {
                //判断自选项已经足够
                int selectedNum = planService.findSelectedNum(batchOptionalVo.getPlanId(), batchOptionalVo.getUserId());
                //判断已选项目选择项目数是否小于等于可选择数量
                if ((selectedNum + porId.length) <= optionalProjectVoList.get(0).getCustomizeTheNumber()) {
                    //判断是否已经选择改项目
                    List<EllFindSelectedPro> ellFindSelectedPros = planService.findProjectOptional(batchOptionalVo.getPlanId(), batchOptionalVo.getUserId(), Integer.parseInt(porId[j]));
                    if (ellFindSelectedPros.size() > 0) {
                        batchOptionalVo.setProId(porId[j] + "---项目已经选择请更换项目！");
                        return new Result(HttpStatus.ERROR, "项目已经选择请更换项目");
                    } else {
                        BatchOptionalVo batchOptional = new BatchOptionalVo();
                        batchOptional.setUserId(batchOptionalVo.getUserId());
                        batchOptional.setProId(porId[j]);
                        batchOptionalVoList.add(batchOptional);
                        continue;
                    }
                } else {
                    batchOptionalVo.setProId(porId[j] + "---选择项目已经多于自选数量请不要多选！");
                    return new Result(HttpStatus.ERROR, "选择项目已经多于自选数量请不要多选！");
                }
            } else {
                batchOptionalVo.setProId(porId[j] + "---请输入正确的项目标识或者此用户不能选择该项目");
                return new Result(HttpStatus.ERROR, "请输入正确的项目标识或者此用户不能选择该项目！");
            }
        }
        //添加
        planService.addOptionalProject(batchOptionalVoList, batchOptionalVo.getPlanId());
        return new Result(HttpStatus.SUCCESS, "成功");
    }

    @ApiOperation("查询该计划的所有学生的视频")
    @GetMapping("/getPlanUserVideo")
    public Result getPlanUserVideo(String planId) {
        List<EllTestHistory> ellTestHistories = planService.selHistory(planId);
        List<EllVideoVo> videoVoList = new ArrayList<>();
        //查询相应学生视频
        for (int i = 0; i < ellTestHistories.size(); i++) {
            List<EllVideoVo> fileMgrList = planService.selVideoUser(planId, ellTestHistories.get(i).getUserId());
            for (int j = 0; j < fileMgrList.size(); j++) {
                videoVoList.add(fileMgrList.get(j));
            }
        }
        return new Result(HttpStatus.SUCCESS, "成功", videoVoList);
    }
}
