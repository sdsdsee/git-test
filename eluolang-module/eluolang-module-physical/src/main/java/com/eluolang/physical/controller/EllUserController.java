package com.eluolang.physical.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.Constants;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.GroupingUtil;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.common.core.util.PageHelperUtil;
import com.eluolang.common.core.web.Result;
import com.eluolang.common.log.annotation.Log;
import com.eluolang.common.log.constant.OperType;
import com.eluolang.physical.Listener.ImportUserListener;
import com.eluolang.physical.dto.EllExamineeDto;
import com.eluolang.physical.dto.EllSexNumDto;
import com.eluolang.physical.dto.GroupingDto;
import com.eluolang.physical.feign.ELLSocketServerFeign;
import com.eluolang.physical.model.*;
import com.eluolang.physical.redis.AvoidRedis;
import com.eluolang.physical.service.*;
import com.eluolang.physical.util.*;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(tags = "学生管理")


@RestController
@Transactional
public class EllUserController {
    @Autowired
    public DailyExerciseService dailyExerciseService;
    @Autowired
    private EllUserService ellUserService;
    @Autowired
    private EllAvoidTestService ellAvoidTestService;
    @Autowired
    private ScoreService scoreService;
    private ImportUserListener importUserListener = null;
    @Value("${RSA.publicFacility}")//设备公钥
    private String RsaPublicFacility;
    @Value("${RSA.privateFacility}")//设备私钥
    private String RsaPrivateFacility;
    @Value("${RSA.private}")//私钥
    private String RsaPrivate;
    @Autowired
    private ELLSocketServerFeign ellSocketServerFeign;
    @Autowired
    private AvoidRedis avoidRedis;
    @Autowired
    private PlanService planService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "姓名", dataType = "String", required = true),
            @ApiImplicitParam(name = "orgId", value = "所属部门标识", dataType = "String", required = true),
            @ApiImplicitParam(name = "studentId", value = "学号", dataType = "String", required = true),
            @ApiImplicitParam(name = "idCard", value = "身份证号", dataType = "String", required = true),
            @ApiImplicitParam(name = "homeAddress", value = "家庭地址", dataType = "String", required = true),
            @ApiImplicitParam(name = "createBy", value = "创建人ID", dataType = "String", required = true),
            @ApiImplicitParam(name = "enTime", value = "入学时间", dataType = "String", required = true),
            @ApiImplicitParam(name = "phone", value = "电话号", dataType = "String", required = false),

    })
    @ApiOperation("添加")
    @PostMapping("/createUser")
    public Result createUser(@RequestBody EllUserVo ellUserDto) throws ParseException, IOException {
        if (IsIdCardUtil.isIDNumber(ellUserDto.getIdCard())) {
            int i = ellUserService.createUser(ellUserDto);
            if (i == HttpStatus.ID_CARD_EXIST) {
                return new Result(HttpStatus.ID_CARD_EXIST, "身份证已经存在！");
            } else if (i == HttpStatus.STUDY_NUMBER_EXIST) {
                return new Result(HttpStatus.ID_CARD_EXIST, "学号/学籍号已经存在！");
            } else if (i == HttpStatus.PARAMETER_ERROR) {
                return new Result(HttpStatus.ID_CARD_ERROR, "身份证错误！");
            }
            if (i > 0) {
                //通知bi更改
                ellSocketServerFeign.sendToAll("更改");
                return Result.SUCCESS();
            }
        }
        return new Result(HttpStatus.ID_CARD_ERROR, "身份证错误！");
    }

  /*  public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public static void main(String[] args) {
        ListNode l1 = new ListNode(1, new ListNode(2, new ListNode(3)));
        ListNode l2 = new ListNode(1, new ListNode(2, new ListNode(3)));
        int n1 = 0;
        int index = 0;
        int n2 = 0;
        int n3 = 0;
        ListNode l6 = l1;
        ListNode l5 = l2;
        while (1 == 1) {
            if (l5 == null && l6 == null) {
                n3 = n1 + n2;
                ListNode l3 = null;
                int num = (n3 + "").length();
                for (int i = index; i >= 0; i--) {
                    System.out.println(n3 % (int)Math.pow(10, i));
                    ListNode l4 = new ListNode(n3 % (int) Math.pow(10, i), l3);
                    l3 = new ListNode(n3 % (int) Math.pow(10, i), l3);
                }
                System.out.println(l3);
                break;
            }
            n1 = n1 + l6.val * (int) Math.pow(10, index);
            n2 = n2 + l5.val * (int) Math.pow(10, index);
            index++;
            l6 = l6.next;
            l5 = l5.next;
        }
    }
*/

    @ApiOperation("通过条件查询某部门**测试使用")
    @PostMapping("/findUsers")
    public Result findUsers(@RequestBody FindEllUserVo findEllUserDto) {
        //PageHelper.startPage(findEllUserDto.getPage(), 10);
        List<EllUser> ellUserList = ellUserService.findUsers(findEllUserDto);
        // PageInfo info = new PageInfo(ellUserList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userList", ellUserList);
        // jsonObject.put("size", info.getTotal());
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }

    @ApiOperation("修改学生信息")
    @PutMapping("/updateUser")
    public Result updateUser(@RequestBody UpdateUserVo updateUserDto) {
        int i = ellUserService.updateUser(updateUserDto);
        if (i > 0) {
            return Result.SUCCESS();
        }
        return new Result(HttpStatus.ERROR, "请稍后重试");
    }

    @ApiOperation("通过条件查询")
    /*    @ApiImplicitParam(name = "orgId", value = "部门id", dataType = "String", required = true)*/
    @PostMapping("/findUser")
    public Result findUser(@RequestBody FindEllUserVo findEllUserDto) {
        //小程序注册
//        int numRegister = ellUserService.findNoRegisterNum(findEllUserDto.getOrgId());
        //人脸认证
//        int numAuthentication = ellUserService.findNotAuthenticated(findEllUserDto.getOrgId());
        List<UserInfoVo> ellUserList = ellUserService.findOrgUsers(findEllUserDto);
//        PageInfo info = new PageInfo(ellUserList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userList", ellUserList);
        jsonObject.put("size", ellUserService.findOrgUsersNum(findEllUserDto));
        List<EllSexNumDto> list = ellUserService.selSexUser(findEllUserDto);

        jsonObject.put("sexSize", list);
//        jsonObject.put("numRegister", numRegister);
//        jsonObject.put("numAuthentication", numAuthentication);
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }

    @ApiOperation("打印二维码学生信息（班级）")
    /*    @ApiImplicitParam(name = "orgId", value = "部门id", dataType = "String", required = true)*/
    @PostMapping("/printUser")
    public Result printUser(@RequestBody FindEllUserVo findEllUserDto) {
        int numRegister = ellUserService.findNoRegisterNum(findEllUserDto.getOrgId());
        int numAuthentication = ellUserService.findNotAuthenticated(findEllUserDto.getOrgId());
        List<UserInfoVo> ellUserList = ellUserService.findUser(findEllUserDto);
        PageInfo info = new PageInfo(ellUserList);
        //查询学校名称
        String schoolName = ellUserService.selSchoolName(Integer.parseInt(findEllUserDto.getOrgId()));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userList", ellUserList);
        jsonObject.put("size", info.getTotal());
        jsonObject.put("numRegister", numRegister);
        jsonObject.put("schoolName", schoolName);
        jsonObject.put("grade", scoreService.calculateGrade(ellUserList.get(0).getEnTime(), Integer.parseInt(ellUserList.get(0).getEnGrade())));
        jsonObject.put("numAuthentication", numAuthentication);
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }

    @ApiOperation("导出")
    @GetMapping("/deriveUser")
    public Result deriveUser(FindEllUserVo findEllUserDto, HttpServletResponse response) {
        List<UserInfoVo> ellUserList = ellUserService.findUser(findEllUserDto);
        String fileName = UUID.randomUUID() + ".xlsx";
        String path = "";
        //判断是否为windos环境
        if (FileUploadUtil.isLinux() == false) {
            path = WindowsSite.filePath + fileName;
        } else {
            path = LinuxSite.filePath + fileName;
        }
        //判断是否有文件路径
        PathGeneration.createPath(path);
        //身份解密
        for (int i = 0; i < ellUserList.size(); i++) {
            if (ellUserList.get(i).getIdCard() != null && !ellUserList.get(i).getIdCard().equals("")) {
                ellUserList.get(i).setIdCard(RSAUtils.decode(ellUserList.get(i).getIdCard(), RsaPrivate));
            }
        }
        EasyExcel.write(path, UserInfoVo.class).sheet("模板").doWrite(ellUserList);
        File file = new File(path);

        try (FileInputStream fileInputStream = new FileInputStream(file);OutputStream outputStream = response.getOutputStream()) {
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            IOUtils.copy(fileInputStream,outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();

        }
        return new Result(HttpStatus.SUCCESS, "导出成功");
    }


    @ApiOperation("导入")
    @PostMapping("/importUser")
    public Result importUser(@RequestParam("file") MultipartFile file, HttpServletResponse response, String orgId) throws ParseException {
        importUserListener = new ImportUserListener(ellUserService, orgId);

        String fileName = UUID.randomUUID() + ".xlsx";
        if (!file.isEmpty()) {
            try {
                //把文件写入到目标位置
                String path = "";
                if (FileUploadUtil.isLinux() == false) {
                    path = WindowsSite.importUserPath + fileName;
                } else {
                    path = LinuxSite.importUserPath + fileName;
                }
                File filepath = new File(path);
                //判断是否有文件路径
                PathGeneration.createPath(path);
                file.transferTo(filepath);//把文件写入目标文件地址
                EasyExcel.read(path, EllUserVo.class, importUserListener).sheet().doRead();
                //通知bi更改
                ellSocketServerFeign.sendToAll("更改");
            } catch (IOException e) {
                e.printStackTrace();
                return new Result(HttpStatus.ERROR, "失败", e);
            }
            //把错误的信息的表格进行返回
            //判断是否是拥有这个错误信息表格
            String fileUri = importUserListener.getFileUri();
            if (fileUri != null && fileUri != "") {

                return new Result(HttpStatus.PARAMETER_ERROR, "一些用户信息有误，错误信息已经生成表格请更改后上传！", fileUri);
            }
        }
        return new Result(HttpStatus.SUCCESS, "添加成功！");
    }

    @ApiOperation("人脸认证")
    @PostMapping("/faceIdentification")
    public Result faceIdentification(@RequestParam("image") MultipartFile image, String userId) throws ParseException {
        try {
            int i = ellUserService.createImageFace(image, userId);
            if (i > 0) {
                String fileUrl = ellUserService.findImageUrl(userId);
                //启动ell人脸认证生成ell人脸
                CmdUtil cmdUtil = new CmdUtil();
                Thread thread = new Thread(cmdUtil);
                thread.start();
                //删除纯在缓存中的人脸信息
                List<String> keys = avoidRedis.getKeys("face:");
                avoidRedis.deleteKey(keys);
                return new Result(HttpStatus.SUCCESS, "认证成功！", fileUrl);
            }
        } catch (Exception exception) {
            return new Result(HttpStatus.UNSUPPORTED_TYPE, "请上传正确的照片!");
        }
        return new Result(HttpStatus.ERROR, "认证失败！");
    }

    @ApiOperation("批量人脸认证")
    @PostMapping("/batchFaceAuthentication")
    public Result batchFaceAuthentication(@RequestParam("imageFile") MultipartFile imageFile) throws ParseException {
        String uri = ellUserService.batchFaceAuthentication(imageFile);
        if (uri == null) {
            //启动ell人脸认证生成ell人脸
            CmdUtil cmdUtil = new CmdUtil();
            Thread thread = new Thread(cmdUtil);
            thread.start();
            //删除纯在缓存中的人脸信息
            List<String> keys = avoidRedis.getKeys("face:");
            avoidRedis.deleteKey(keys);
            return Result.SUCCESS();
        }
        return new Result(HttpStatus.BAD_REQUEST, "部分用户认证失败,请查看返回的Excel！", uri);
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
        String fileName = "ImportUserTemplate.xlsx";
        File file = new File(template + fileName);
        String path = template + fileName;
        //判断是否有文件路径
        PathGeneration.createPath(path);
        //判断文件是否存在
        if (!file.exists()) {
            List<EllUserVo> ellUserList = new ArrayList<>();
            EllUserVo ellUserVo = new EllUserVo();
            ellUserVo.setUserName("xxx");
            ellUserVo.setEthnic("1");
            ellUserVo.setHomeAddress("北京市*****");
            ellUserVo.setStudentId("201811224");
            ellUserVo.setIdCard("510724********1213");
            ellUserVo.setEnTime("2018-09");
            ellUserList.add(ellUserVo);
            EasyExcel easyExcel = new EasyExcel();
            easyExcel.write(path, EllUserVo.class).registerWriteHandler(new EasyExcelColumnTypeUtil()).sheet("模板").doWrite(ellUserList);
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

    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id", value = "id", dataType = "String", required = true)
    @DeleteMapping("/deleteUser")
    public Result deleteUser(String id) throws IOException {
        int i = ellUserService.deleteUser(id);
        if (i > 0) {
            ellSocketServerFeign.sendToAll("更改");
            return Result.SUCCESS();
        }
        return new Result(HttpStatus.MOVED_PERM, "请稍后再试或该资源有可能已经被移除");
    }

    @ApiOperation("查询名族")
    @GetMapping(value = "/findEthnic")
    public Result findEthnic() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ethnic", ellUserService.findEthnic());
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }

    @ApiOperation("查询下级部门----测试下使用")
    @ApiImplicitParam(name = "orgId", value = "部门的id", dataType = "String", required = true)
    @DeleteMapping("/findDepart")
    public Result findDepart(String orgId) {
        List<DepartVo> departVoList = ellUserService.findDeparts(orgId);
        return new Result(HttpStatus.SUCCESS, "", departVoList);
    }

    @GetMapping("/findNotAuthenticated")
    public int findNotAuthenticated(String orgId) {
        return ellUserService.findNotAuthenticated(orgId);
    }

    /**
     * 考生按男女分组
     *
     * @return Result
     */
    @ApiOperation(value = "考生按学校和项目分组")
    @Log(title = "考生按学校和项目分组", operType = OperType.INSERT, content = "考生按学校和项目分组")
    @PostMapping(value = "/groupBySchoolAndPro")
    public Result groupBySchoolAndPro(@RequestBody GroupingDto groupingDto) {
        int count = ellUserService.selExamineeGroupByPlanId(groupingDto.getPlanId());
        if (count > 0) {
            return new Result(HttpStatus.ERROR, "该计划已存在考生分组，请先删除原有分组", null);
        }
        //选择相同项目的
        Map<String, List<EllOptionalUserProVo>> proUser = new HashMap<>();
        //每组学生
        Map<String, List<EllExamineeUser>> listMap = new HashMap<>();
        //学校组存储
        Map<String, EllExamineeGroup> listHashMap = new HashMap<>();
        //组
        List<EllExamineeGroup> ellExamineeGroups = new ArrayList<>();
        //查询计划信息
        EllPlan ellPlan = planService.selPlan(groupingDto.getPlanId());
        //查询本计划选择的部门
        List<EllUserPlanDepartment> userPlanDepartmentList = ellUserService.selUsePlanDep(groupingDto.getPlanId());
        for (int i = 0; i < userPlanDepartmentList.size(); i++) {
            //查询学校
            List<PfDepart> schoolList = ellUserService.selPlanSchool(groupingDto.getPlanId(), 3, String.valueOf(userPlanDepartmentList.get(i).getOrgId()));
            if (schoolList == null || schoolList.size() <= 0) {
                //查询班级
                List<PfDepart> classList = ellUserService.selPlanSchool(groupingDto.getPlanId(), 1, String.valueOf(userPlanDepartmentList.get(i).getOrgId()));
                //查询学校
                schoolList = ellUserService.selPlanSchool(groupingDto.getPlanId(), 2, String.valueOf(classList.get(0).getId()));
            }
            //根据不同的学校进行分组
            for (int j = 0; j < schoolList.size(); j++) {
                //查询此学校在此计划的所有人
//                List<EllUser> ellUserList = ellUserService.selPlanSchoolUser(groupingDto.getPlanId(), String.valueOf(schoolList.get(j).getId()));
                List<EllOptionalUserProVo> ellOptionalUserProVoList = ellUserService.selOptionalUserPro(groupingDto.getPlanId(), String.valueOf(schoolList.get(j).getId()));
                //自选项目相同的学生
                for (int k = 0; k < ellOptionalUserProVoList.size(); k++) {
                    //如果是自选项目的考试计划就将每个学校未选择的项目的单独一组
                    if (ellPlan.getIsExam() == 1 && ellPlan.getCustomizeTheNumber() > 0 && (ellOptionalUserProVoList.get(k).getProId() == null || ellOptionalUserProVoList.get(k).getProId() == "")) {
                        if (listHashMap.containsKey(ellPlan.getPlanTitle() + schoolList.get(j).getId() + "未选项目组")) {
                            //存在并且人数小于规定人数就添加
                            EllExamineeUser ellExamineeUser = new EllExamineeUser();
                            ellExamineeUser.setStuId(ellOptionalUserProVoList.get(k).getUserId());
                            ellExamineeUser.setTestNumber(GroupingUtil.randomTestNumber(ellOptionalUserProVoList.get(k).getUserSex()));
                            ellExamineeUser.setGroupId(listHashMap.get(ellPlan.getPlanTitle() + schoolList.get(j).getId() + "未选项目组").getId());
                            ellExamineeUser.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                            listMap.get(ellPlan.getPlanTitle() + schoolList.get(j).getId() + "未选项目组").add(ellExamineeUser);
                            continue;
                        } else {
                            //新增未选组
                            EllExamineeGroup ellExamineeGroup = new EllExamineeGroup();
                            ellExamineeGroup.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                            ellExamineeGroup.setGroupName(ellPlan.getPlanTitle() + schoolList.get(j).getDeptName() + "未选项目组");
                            ellExamineeGroup.setOptId(groupingDto.getOptId());
                            ellExamineeGroup.setPlanId(groupingDto.getPlanId());
                            ellExamineeGroups.add(ellExamineeGroup);
                            listHashMap.put(ellPlan.getPlanTitle() + schoolList.get(j).getId() + "未选项目组", ellExamineeGroup);
                            //给新组添加第一个人
                            EllExamineeUser ellExamineeUser = new EllExamineeUser();
                            ellExamineeUser.setStuId(ellOptionalUserProVoList.get(k).getUserId());
                            ellExamineeUser.setTestNumber(GroupingUtil.randomTestNumber(ellOptionalUserProVoList.get(k).getUserSex()));
                            ellExamineeUser.setGroupId(ellExamineeGroup.getId());
                            ellExamineeUser.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                            List<EllExamineeUser> ellExamineeUserList = new ArrayList<>();
                            ellExamineeUserList.add(ellExamineeUser);
                            listMap.put(ellPlan.getPlanTitle() + schoolList.get(j).getId() + "未选项目组", ellExamineeUserList);
                            continue;
                        }
                    }
                    //将相同的自选项的学生放在一起
                    if (proUser.containsKey(schoolList.get(j).getId() + ellOptionalUserProVoList.get(k).getProId())) {
                        proUser.get(schoolList.get(j).getId() + ellOptionalUserProVoList.get(k).getProId()).add(ellOptionalUserProVoList.get(k));
                    } else {
                        List<EllOptionalUserProVo> optionalUserProVoList = new ArrayList<>();
                        optionalUserProVoList.add(ellOptionalUserProVoList.get(k));
                        proUser.put(schoolList.get(j).getId() + ellOptionalUserProVoList.get(k).getProId(), optionalUserProVoList);
                    }
                }
                //组号
                int groupNum = 0;
                for (String key : proUser.keySet()) {
                    //判断自选项目是否相同
                    String groupName = ellPlan.getPlanTitle() + schoolList.get(j).getDeptName() + (groupNum + 1) + "组";
                    //组的key
                    String groupKey = ellPlan.getPlanTitle() + schoolList.get(j).getId() + proUser.get(key).get(0).getProId() + (groupNum + 1) + "组";
                    for (int k = 0; k < proUser.get(key).size(); k++) {
                        //判断每组人数
                        if (listMap.containsKey(groupKey) && listMap.get(groupKey).size() < groupingDto.getCount()) {
                            //存在并且人数小于规定人数就添加
                            EllExamineeUser ellExamineeUser = new EllExamineeUser();
                            ellExamineeUser.setStuId(proUser.get(key).get(k).getUserId());
                            ellExamineeUser.setTestNumber(GroupingUtil.randomTestNumber(ellOptionalUserProVoList.get(k).getUserSex()));
                            ellExamineeUser.setGroupId(listHashMap.get(groupKey).getId());
                            ellExamineeUser.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                            listMap.get(groupKey).add(ellExamineeUser);
                        } else {
                            //组号加1
                            groupNum++;
                            groupName = ellPlan.getPlanTitle() + schoolList.get(j).getDeptName() + groupNum + "组";
                            //组的key
                            groupKey = ellPlan.getPlanTitle() + schoolList.get(j).getId() + proUser.get(key).get(0).getProId() + (groupNum + 1) + "组";
                            //添加一个新组
                            EllExamineeGroup ellExamineeGroup = new EllExamineeGroup();
                            ellExamineeGroup.setGroupName(groupName);
                            ellExamineeGroup.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                            ellExamineeGroup.setPlanId(ellPlan.getId());
                            ellExamineeGroup.setOptId(groupingDto.getOptId());
                            listHashMap.put(groupKey, ellExamineeGroup);
                            ellExamineeGroups.add(ellExamineeGroup);
                            //给新组添加第一个人
                            EllExamineeUser ellExamineeUser = new EllExamineeUser();
                            ellExamineeUser.setStuId(proUser.get(key).get(k).getUserId());
                            ellExamineeUser.setTestNumber(GroupingUtil.randomTestNumber(ellOptionalUserProVoList.get(k).getUserSex()));
                            ellExamineeUser.setGroupId(ellExamineeGroup.getId());
                            ellExamineeUser.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
                            List<EllExamineeUser> ellExamineeUserList = new ArrayList<>();
                            ellExamineeUserList.add(ellExamineeUser);
                            listMap.put(groupKey, ellExamineeUserList);
                        }
                    }
                }
                //清空此学校选考项目相同的人员
                proUser = new HashMap<>();

            }
        }
        //保存在数据库
        //每个组
        ellUserService.insertBacthExamineeGroup(ellExamineeGroups);
        //每组学生
        for (String key : listMap.keySet()) {
            ellUserService.insertExamineeUser(listMap.get(key));
        }
        return new Result(HttpStatus.SUCCESS, "成功");
    }

    /**
     * 考生按男女分组
     *
     * @return Result
     */
    @ApiOperation(value = "考生按男女分组")
    @Log(title = "考生按男女分组", operType = OperType.INSERT, content = "考生按男女分组")
    @PostMapping(value = "/examineeGroupBySex")
    public Result examineeGroupBySex(@RequestBody GroupingDto groupingDto) {
        int count = ellUserService.selExamineeGroupByPlanId(groupingDto.getPlanId());
        if (count > 0) {
            return new Result(HttpStatus.ERROR, "该计划已存在考生分组，请先删除原有分组", null);
        }
        //查询本计划选择的部门
        List<EllUserPlanDepartment> userPlanDepartmentList = ellUserService.selUsePlanDep(groupingDto.getPlanId());
        for (int j = 0; j < userPlanDepartmentList.size(); j++) {
            //查询学校
            List<PfDepart> schoolList = ellUserService.selPlanSchool(groupingDto.getPlanId(), 3, String.valueOf(userPlanDepartmentList.get(j).getOrgId()));
            if (schoolList == null || schoolList.size() <= 0) {
                //查询班级
                List<PfDepart> classList = ellUserService.selPlanSchool(groupingDto.getPlanId(), 1, String.valueOf(userPlanDepartmentList.get(j).getOrgId()));
                //查询学校
                schoolList = ellUserService.selPlanSchool(groupingDto.getPlanId(), 2, String.valueOf(classList.get(0).getId()));
            }
            List<EllUser> ellUserList = ellUserService.selEllUserByDeptId(schoolList);
            if (ellUserList.size() == 0) {
                return new Result(HttpStatus.ERROR, "学生为空", null);
            }
            //将学生分为男女两组写在map中
            Map<Integer, List<EllUser>> map = GroupingUtil.sexGroup(ellUserList);
            //男生组
            List<EllUser> ellUsers = new ArrayList<>();
            //女生组
            List<EllUser> ellUsers1 = new ArrayList<>();
            //循环将map中的男女组分别写进list
            for (Integer key : map.keySet()) {
                if (key == Constants.MALE) {
                    for (int i = 0; i < map.get(key).size(); i++) {
                        ellUsers.add(map.get(key).get(i));
                    }
                } else {
                    for (int i = 0; i < map.get(key).size(); i++) {
                        ellUsers1.add(map.get(key).get(i));
                    }
                }
            }

            //判断男生或女生人数是否小于每组需要的人数
            if (ellUsers.size() < groupingDto.getCount() || ellUsers1.size() < groupingDto.getCount()) {
                return new Result(HttpStatus.ERROR, "人数小于每组需要人数", null);
            }

            //将男生按每组人数进行分组
            List<List<EllUser>> man = GroupingUtil.test(ellUsers, groupingDto.getCount());
            //查询分组完成后男生总人数
//        int sort = ((man.size() - 1) * groupingDto.getCount()) + man.get(man.size() - 1).size();
            int num = 0;
            for (int i = 0; i < man.size(); i++) {
                num += man.get(i).size();
            }
            if (ellUsers.size() > num) {
                return new Result(HttpStatus.ERROR, "该分组条件不合理", null);
            }
            boolean flag = ellUserService.ExamineeGroupBySex(man, groupingDto, Constants.MALE);
            if (flag == false) {
                return new Result(HttpStatus.ERROR, "分组失败", null);
            }

            //将女生按每组人数进行分组
            List<List<EllUser>> woman = GroupingUtil.test(ellUsers1, groupingDto.getCount());

            //查询分组完成后女生总人数
            int num1 = 0;
            for (int i = 0; i < woman.size(); i++) {
                num1 += woman.get(i).size();
            }
            if (ellUsers1.size() > num1) {
                return new Result(HttpStatus.ERROR, "该分组条件不合理", null);
            }
            boolean flag1 = ellUserService.ExamineeGroupBySex(woman, groupingDto, Constants.FEMALE);
            if (flag1 == false) {
                return new Result(HttpStatus.ERROR, "分组失败", null);
            }
        }
        return new Result(HttpStatus.SUCCESS, "分组成功", null);
    }

    /**
     * 考生打乱顺序分组
     *
     * @return Result
     */
    @ApiOperation(value = "考生打乱顺序分组")
    @Log(title = "考生打乱顺序分组", operType = OperType.INSERT, content = "考生打乱顺序分组")
    @PostMapping(value = "/upsetGroup")
    public Result upsetGroup(@RequestBody GroupingDto groupingDto) {
        int count = ellUserService.selExamineeGroupByPlanId(groupingDto.getPlanId());
        if (count > 0) {
            return new Result(HttpStatus.ERROR, "该计划已存在考生分组，请先删除原有分组", null);
        }
        //查询本计划选择的部门
        List<EllUserPlanDepartment> userPlanDepartmentList = ellUserService.selUsePlanDep(groupingDto.getPlanId());
        for (int j = 0; j < userPlanDepartmentList.size(); j++) {
            //查询学校
            List<PfDepart> schoolList = ellUserService.selPlanSchool(groupingDto.getPlanId(), 3, String.valueOf(userPlanDepartmentList.get(j).getOrgId()));
            if (schoolList == null || schoolList.size() <= 0) {
                //查询班级
                List<PfDepart> classList = ellUserService.selPlanSchool(groupingDto.getPlanId(), 1, String.valueOf(userPlanDepartmentList.get(j).getOrgId()));
                //查询学校
                schoolList = ellUserService.selPlanSchool(groupingDto.getPlanId(), 2, String.valueOf(classList.get(0).getId()));
            }
            List<EllUser> ellUserList = ellUserService.selEllUserByDeptId(schoolList);
            if (ellUserList.size() == 0) {
                return new Result(HttpStatus.ERROR, "学生为空", null);
            }
            //将考生按每组人数进行分组
            List<List<EllUser>> person = GroupingUtil.test(ellUserList, groupingDto.getCount());
            //查询分组完成后男生总人数
            int num = 0;
            for (int i = 0; i < person.size(); i++) {
                num += person.get(i).size();
            }
            //int sort = ((person.size() - 1) * groupingDto.getCount()) + person.get(person.size() - 1).size();
            if (ellUserList.size() > num) {
                return new Result(HttpStatus.ERROR, "该分组条件不合理", null);
            }
            boolean flag = ellUserService.upsetGroup(person, groupingDto);
            if (flag == false) {
                return new Result(HttpStatus.ERROR, "分组失败", null);
            }
        }
        return new Result(HttpStatus.SUCCESS, "分组成功", null);
    }

    /**
     * 查询已测人数
     */
    @GetMapping("/testProNum")
    public Result testProNum(int proId, String planId) {
        return new Result(HttpStatus.SUCCESS, "成功", scoreService.findScoreNum(proId, planId));
    }

    /**
     * 人脸识别用户检录
     *
     * @return Result
     */
    @ApiOperation(value = "人脸识别用户检录")
    @PostMapping(value = "/studentsFaceRegister")
    public Result studentsFaceRegister(@RequestBody UserRegisterVo userRegisterVo) {
        List<EllReturnScoreVo> ellReturnScoreVos = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            //设备id解密
            StringBuffer deviceId = new StringBuffer(RSAUtils.decode(userRegisterVo.getDeviceId(), RsaPrivateFacility));
            userRegisterVo.setDeviceId(deviceId.toString());
            //计划id解密
            StringBuffer planId = new StringBuffer(RSAUtils.decode(userRegisterVo.getPlanId(), RsaPrivateFacility));
            userRegisterVo.setPlanId(planId.toString());
            //人脸检录就不用解密标识
            String userId = "";
            //体考体测模式，等于0是兼容以前设备
            if (userRegisterVo.getMode() == 0 || userRegisterVo.getMode() == 3) {
                userId = ellUserService.findFeature(planId.toString(), userRegisterVo.getImage());
            } else {
                //课堂模式和锻炼模式
                //人脸检录就不用解密标识
                userId = dailyExerciseService.finUserId(userRegisterVo.getImage(), userRegisterVo.getDeviceId());
            }
            if (userId == null) {
                return new Result(HttpStatus.MATCHING_FAILURE, "未匹配到相识人脸");
            }
            if (userId.equals(HttpStatus.NON_LIVING_BODY + "")) {
                return new Result(HttpStatus.NON_LIVING_BODY, "非活体");
            }
            if (userId.equals(HttpStatus.MATCHING_FAILURE + "")) {
                return new Result(HttpStatus.MATCHING_FAILURE, "未匹配到相识人脸");
            }
            userRegisterVo.setIdentification(userId);
            //查询该项目已测试人数
            int num = scoreService.findScoreNum(userRegisterVo.getProId(), planId.toString());
            jsonObject.put("currentTestedNumber", num);
            //清空
            planId.setLength(0);
            deviceId.setLength(0);
        } catch (Exception e) {
            //解密失败
            return new Result(HttpStatus.DECODE_ERROR, "解密错误，请检查加密参数加密是否正确");
        }
        //增加正在测试人数

        //通过学号或id查询学生信息
        ellReturnScoreVos = ellUserService.findUserBy(userRegisterVo.getIdentification(), userRegisterVo.getOrgId());
        if (ellReturnScoreVos.size() <= 0) {
            return new Result(HttpStatus.ERROR, "未查询到！");
        } else if (userRegisterVo.getMode() != 0 && userRegisterVo.getMode() != 3) {//只要不是体考模式就不做下面的操作等于,0是兼容以前设备
            //通过学号或id查询学生信息
            jsonObject.put("user", ellReturnScoreVos.get(0));
            jsonObject.put("isRetest", 1);
            jsonObject.put("needTest", true);
            jsonObject.put("isResTestGroupUser", null);
            //增加测试人数
            avoidRedis.addTestNum(Integer.parseInt(ellReturnScoreVos.get(0).getOrgId()));
            return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
        }
        //增加测试人数
        avoidRedis.addTestNum(Integer.parseInt(ellReturnScoreVos.get(0).getOrgId()));
        //测试项目数量
        int userTestedProjectNumber = scoreService.selTestProNum(userRegisterVo.getIdentification(), userRegisterVo.getPlanId());
        int isRestGroupUser = ellUserService.findIsRetestGroupUser(userRegisterVo.getPlanId(), ellReturnScoreVos.get(0).getId());
        //查询是否有这个用户
        FindAvoidVo findAvoidVo = new FindAvoidVo();
        findAvoidVo.setPlanId(userRegisterVo.getPlanId());
        findAvoidVo.setUserId(ellReturnScoreVos.get(0).getId());
        findAvoidVo.setProId(userRegisterVo.getProId());
        //查询此人是否需要测试该项目
        int is = ellAvoidTestService.isProTest(findAvoidVo);
        int hasTest = ellUserService.hasTestProject(findAvoidVo);
        if (is > 0 || hasTest <= 0) {
            jsonObject.put("needTest", false);
        } else {
            jsonObject.put("needTest", true);
            //判断是否是重测
            EllHistoryVo ellHistoryVo = new EllHistoryVo();
            ellHistoryVo.setUserId(ellReturnScoreVos.get(0).getId());
            ellHistoryVo.setPlanId(userRegisterVo.getPlanId());
            ellHistoryVo.setTestProject(String.valueOf(userRegisterVo.getProId()));
            List<EllTestHistory> ellTestHistories = scoreService.findScore(ellHistoryVo);
            if (ellTestHistories.size() > 0) {
                jsonObject.put("isRetest", 2);
            } else {
                jsonObject.put("isRetest", 1);
            }
        }
        jsonObject.put("userTestedProjectNumber", userTestedProjectNumber);
        jsonObject.put("isResTestGroupUser", isRestGroupUser);
        jsonObject.put("planTotalNumber", planService.selPlanTotalNum(userRegisterVo.getPlanId()));
        //查询该学生的部门信息
        ellReturnScoreVos.get(0).setOrgName(ellUserService.findDeptNameById(Integer.parseInt(ellReturnScoreVos.get(0).getOrgId())));
        jsonObject.put("user", ellReturnScoreVos.get(0));
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }
    ////

    /**
     * 用户检录（体测检录/计划测试跑检录/日常测试自由跑检录）
     *
     * @return Result
     */
    @ApiOperation(value = "用户检录")
    @PostMapping(value = "/studentsRegister")
    public Result studentsRegister(@RequestBody UserRegisterVo userRegisterVo) {
        List<EllReturnScoreVo> ellReturnScoreVos = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            //设备id解密
            StringBuffer deviceId = new StringBuffer(RSAUtils.decode(userRegisterVo.getDeviceId(), RsaPrivateFacility));
            userRegisterVo.setDeviceId(deviceId.toString());
            //计划id解密
            StringBuffer planId = new StringBuffer(RSAUtils.decode(userRegisterVo.getPlanId(), RsaPrivateFacility));
            userRegisterVo.setPlanId(planId.toString());
            //标识解密
            StringBuffer identification = new StringBuffer(RSAUtils.decode(userRegisterVo.getIdentification(), RsaPrivateFacility));
            userRegisterVo.setIdentification(identification.toString());
            //清空
            identification.setLength(0);
            //查询该项目已测试人数
            int num = scoreService.findScoreNum(userRegisterVo.getProId(), userRegisterVo.getPlanId());
            jsonObject.put("currentTestedNumber", num);
            //清空
            planId.setLength(0);
            deviceId.setLength(0);
        } catch (Exception e) {
            //解密失败
            return new Result(HttpStatus.DECODE_ERROR, "解密错误，请检查加密参数加密是否正确");
        }
        //判断是userid,studentId还是身份证
        if (IsIdCardUtil.isIDNumber(userRegisterVo.getIdentification())) {
            //生成身份证标识
            String identificationString = IdCardUtil.identificationString(userRegisterVo.getIdentification());
            //查询出相同标识的人
            List<String> userIds = ellUserService.findIdentification(identificationString);
            for (int i = 0; i < userIds.size(); i++) {
                //判断身份证解密是否相同
                String idCardMys = ellUserService.findIdCardById(userIds.get(i));
                if (idCardMys != null && idCardMys != "" && RSAUtils.decode(idCardMys, RsaPrivate).equals(userRegisterVo.getIdentification())) {
                    //通过身份证查询学生信息
                    ellReturnScoreVos = ellUserService.findUserBy(userIds.get(i), userRegisterVo.getOrgId());
                    break;
                }
            }
            //万一有学号像身份证号就不会查询出来
            if (ellReturnScoreVos == null || ellReturnScoreVos.size() <= 0) {
                //通过学号或id查询学生信息
                ellReturnScoreVos = ellUserService.findUserBy(userRegisterVo.getIdentification(), userRegisterVo.getOrgId());
            }
        } else {
            //通过学号或id查询学生信息
            ellReturnScoreVos = ellUserService.findUserBy(userRegisterVo.getIdentification(), userRegisterVo.getOrgId());
        }
        //查询是否有这个用户
        if (ellReturnScoreVos.size() <= 0) {
            return new Result(HttpStatus.ERROR, "未查询到！");
        } else if (userRegisterVo.getMode() != 0 && userRegisterVo.getMode() != 3) {//只要不是体考模式就不做下面的操作等于,0是兼容以前设备
            //通过学号或id查询学生信息
            jsonObject.put("user", ellReturnScoreVos.get(0));
            jsonObject.put("isRetest", 1);
            jsonObject.put("needTest", true);
            jsonObject.put("isResTestGroupUser", null);
            //增加测试人数
            avoidRedis.addTestNum(Integer.parseInt(ellReturnScoreVos.get(0).getOrgId()));
            return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
        }
        //增加测试人数
        avoidRedis.addTestNum(Integer.parseInt(ellReturnScoreVos.get(0).getOrgId()));
        //测试项目数量
        int userTestedProjectNumber = scoreService.selTestProNum(userRegisterVo.getIdentification(), userRegisterVo.getPlanId());
        int isRestGroupUser = ellUserService.findIsRetestGroupUser(userRegisterVo.getPlanId(), ellReturnScoreVos.get(0).getId());
        FindAvoidVo findAvoidVo = new FindAvoidVo();
        findAvoidVo.setPlanId(userRegisterVo.getPlanId());
        findAvoidVo.setUserId(ellReturnScoreVos.get(0).getId());
        findAvoidVo.setProId(userRegisterVo.getProId());
        //查询此人是否需要测试该项目
        int is = ellAvoidTestService.isProTest(findAvoidVo);
        int hasTest = ellUserService.hasTestProject(findAvoidVo);
        //判断是不是自由跑自由跑不用判断重测和beedTest以及是否为重测组
//        if (userRegisterVo.getType() != 1) {
        if (is > 0 || hasTest <= 0) {
            jsonObject.put("needTest", false);
        } else {
            jsonObject.put("needTest", true);
            //判断是否是重测
            EllHistoryVo ellHistoryVo = new EllHistoryVo();
            ellHistoryVo.setUserId(ellReturnScoreVos.get(0).getId());
            ellHistoryVo.setPlanId(userRegisterVo.getPlanId());
            ellHistoryVo.setTestProject(String.valueOf(userRegisterVo.getProId()));
            List<EllTestHistory> ellTestHistories = scoreService.findScore(ellHistoryVo);
            if (ellTestHistories.size() > 0) {
                jsonObject.put("isRetest", 2);
            } else {
                jsonObject.put("isRetest", 1);
            }
        }
        jsonObject.put("userTestedProjectNumber", userTestedProjectNumber);
        jsonObject.put("isResTestGroupUser", isRestGroupUser);
//        } else {
//            //但是自由跑需要反回日常计划id
//            List<EllDailyExercisePlanVo> ellDailyExercisePlanList = dailyExerciseService.selDailyExercisePlanByName(ellReturnScoreVos.get(0).getOrgId(), "0");
//            if (ellDailyExercisePlanList == null && ellDailyExercisePlanList.size() <= 0) {
//                return new Result(HttpStatus.ERROR, "未查询到日常计划，请先通知管理员设置日常计划");
//            }
//            jsonObject.put("dailyId", ellDailyExercisePlanList.get(0).getId());
//        }
        //查询该学生的部门信息
        ellReturnScoreVos.get(0).setOrgName(ellUserService.findDeptNameById(Integer.parseInt(ellReturnScoreVos.get(0).getOrgId())));
        jsonObject.put("user", ellReturnScoreVos.get(0));
        jsonObject.put("planTotalNumber", planService.selPlanTotalNum(userRegisterVo.getPlanId()));
        return new Result(HttpStatus.SUCCESS, "成功", jsonObject);
    }

    /**
     * 查询某计划下所有考生分组
     *
     * @return Result
     */
    @ApiOperation(value = "查询某计划下所有考生分组")
    @GetMapping(value = "/selExamineeGroupAll")
    public Result selExamineeGroupByPlanId(String planId) {
        List<EllExamineeGroup> ellExamineeGroups = ellUserService.selectExamineeGroupAll(planId);
        return new Result(HttpStatus.SUCCESS, "查询成功", ellExamineeGroups);
    }

    /**
     * 按条件查询考生信息
     *
     * @return Result
     */
    @ApiOperation(value = "按条件查询考生信息")
    @GetMapping(value = "/selectExaminee")
    public Result selectExaminer(EllExamineeDto ellExamineeDto) {
        PageHelperUtil.startPage(ellExamineeDto.getPageNum(), ellExamineeDto.getPageSize());
        List<EllExamineeVo> ellExamineeVos = ellUserService.selectExaminee(ellExamineeDto);
        //将监考员数据进行分页
        PageInfo<EllExamineeVo> pageInfo = new PageInfo<>(ellExamineeVos);
        return new Result(HttpStatus.SUCCESS, "查询成功", pageInfo);
    }

    /**
     * 根据体测计划id删除考生分组
     *
     * @return Result
     */
    @ApiOperation(value = "根据体测计划id删除考生分组")
    @Log(title = "根据体测计划id删除考生分组", operType = OperType.DELETE, content = "根据体测计划id删除考生分组")
    @DeleteMapping(value = "/deleteExaminee")
    public Result deleteExaminee(String planId) {
        int count = ellUserService.deleteExamineeByPlanId(planId);
        if (count > 0) {
            return new Result(HttpStatus.SUCCESS, "删除成功", null);
        }
        return new Result(HttpStatus.ERROR, "删除失败", null);
    }

    @ApiOperation(value = "个人体质测试档案", notes = "导出最近一次成绩报告")
    @RequestMapping(value = "/expPersonHis", method = RequestMethod.GET)
    public Result expPersonHis(String planId, String userId, HttpServletRequest request, HttpServletResponse response) {
        return this.ellUserService.expPersonHis(planId, userId, request, response);
    }

    @ApiOperation(value = "个人体质档案批量导出zip", notes = "个人体质档案批量导出zip")
    @RequestMapping(value = "/expPersonHisZip", method = RequestMethod.GET)
    public Result expPersonHisZip(@RequestParam List<String> userIdList, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (userIdList.size() != 0) {
            return this.ellUserService.expPersonHisZip(null, userIdList, request, response);
        }
        return new Result(HttpStatus.ERROR, "导出失败,请选择学生", null);
    }

    /**
     * 部门逻辑删除
     *
     * @return Result
     */
    @ApiOperation(value = "部门逻辑删除")
    @Log(title = "部门逻辑删除", operType = OperType.DELETE, content = "部门逻辑删除")
    @PostMapping(value = "/updateDepartIdent")
    public Result updateDepartIdent(@RequestBody List<Integer> deptIdList) {
        List<EllUser> ellUserList = ellUserService.selUserByDeptIdList(deptIdList);
        if (ellUserList.size() != 0) {
            int count = ellUserService.updateUserStatusList(ellUserList);
            if (count <= 0) {
                return new Result(HttpStatus.ERROR, "删除失败", null);
            }
        }
        int row = ellUserService.updatePfDepartStatus(deptIdList);
        if (row > 0) {
            return new Result(HttpStatus.SUCCESS, "删除成功", null);
        }
        return new Result(HttpStatus.ERROR, "删除失败", null);
    }

    /**
     * 新增重测组
     *
     * @return Result
     */
    @ApiOperation(value = "新增重测组")
    @Log(title = "新增重测组", operType = OperType.DELETE, content = "新增重测组")
    @PostMapping(value = "/insertRetestGroup")
    public Result insertRetestGroup(@RequestBody List<EllRetestGroup> ellRetestGroupList) {
        int count = ellUserService.insertRetestGroup(ellRetestGroupList);
        if (count > 0) {
            return new Result(HttpStatus.SUCCESS, "新增成功", null);
        }
        return new Result(HttpStatus.ERROR, "新增失败", null);
    }

    @ApiOperation("准考证打印")
    @GetMapping("/AdmissionTicketPrinting")
    public Result AdmissionTicketPrinting(String planId, String userId) {
        EllAdmissionTicketPrintingVo ticketPrinting = ellUserService.AdmissionTicketPrinting(planId, userId);
        if (ticketPrinting == null) {
            return new Result(HttpStatus.ERROR, "自选项目数量不足");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ticketPrinting", ticketPrinting);
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }

    @ApiOperation("批量准考证打印")
    @GetMapping("/BatchPrinting")
    public Result BatchPrinting(String planId, String groupId) {
        List<EllAdmissionTicketPrintingVo> ellAdmissionTicketPrintingVos = new ArrayList<>();
        List<EllExamineeUser> ellExamineeUserList = ellUserService.selGroupUser(groupId);
        for (int i = 0; i < ellExamineeUserList.size(); i++) {
            EllAdmissionTicketPrintingVo ticketPrinting = ellUserService.AdmissionTicketPrinting(planId, ellExamineeUserList.get(i).getStuId());
            if (ticketPrinting == null) {
                return new Result(HttpStatus.ERROR, "自选项目数量不足");
            }
            ellAdmissionTicketPrintingVos.add(ticketPrinting);
        }
        if (ellAdmissionTicketPrintingVos.size() <= 0) {
            return new Result(HttpStatus.ERROR, "此分组为空");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ticketPrinting", ellAdmissionTicketPrintingVos);
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }

    @ApiOperation("BI总数判断")
    @GetMapping("/sumTotal")
    public Result sumTotal(String planId, String userId) {
        EllAdmissionTicketPrintingVo ticketPrinting = ellUserService.AdmissionTicketPrinting(planId, userId);
        if (ticketPrinting == null) {
            return new Result(HttpStatus.ERROR, "自选项目数量不足");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ticketPrinting", ticketPrinting);
        return new Result(HttpStatus.SUCCESS, "查询成功", jsonObject);
    }

    /**
     * 准考证批量打印
     */
    @ApiOperation("准考证批量打印")
    @GetMapping("/batchAdmissionTicketPrinting")
    public Result batchAdmissionTicketPrinting(String planId, String orgId) {
        return null;
    }

    /**
     * 设备获取时间
     */
    @GetMapping("/currentTime")
    public Result currentTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", sdf.format(d));
        return new Result(HttpStatus.SUCCESS, "成功!", jsonObject);
    }
}
