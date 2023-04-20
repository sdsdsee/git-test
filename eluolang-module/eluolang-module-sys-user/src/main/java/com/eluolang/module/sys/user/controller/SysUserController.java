package com.eluolang.module.sys.user.controller;

import com.alibaba.excel.EasyExcel;
import com.eluolang.common.core.constant.*;
import com.eluolang.common.core.hardware.vo.PfDepartVo;
import com.eluolang.module.sys.user.Listener.ImportUserListener;
import com.eluolang.module.sys.user.model.EllDepartVo;
import com.eluolang.module.sys.user.model.PfOperatorVo;
import com.eluolang.module.sys.user.service.DepartService;
import com.eluolang.module.sys.user.util.PathGeneration;
import com.eluolang.module.sys.user.vo.DepartVo;
import com.github.pagehelper.PageInfo;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.exception.BaseException;
import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.util.*;
import com.eluolang.common.core.web.Result;
import com.eluolang.common.log.annotation.Log;
import com.eluolang.common.log.constant.OperType;
import com.eluolang.common.redis.service.RedisService;
import com.eluolang.common.security.service.TokenService;
import com.eluolang.module.sys.user.constant.SysUserConstant;
import com.eluolang.module.sys.user.dto.*;
import com.eluolang.module.sys.user.service.SysUserService;
import com.eluolang.module.sys.user.service.feign.UserRemoteService;
import com.eluolang.module.sys.user.util.DataGridView;
import com.eluolang.module.sys.user.util.TreeNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 用户管理控制层
 *
 * @author ZengxiaoQian
 * @createDate 2020/8/25
 */
@Api("用户管理")
@RestController
@RequestMapping("/sysUser/manage")
public class SysUserController {
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

    @Autowired
    private DepartService departService;

    @Resource
    private RedisService redisService;

    @Resource
    private UserRemoteService userRemoteService;

    @Resource
    private TokenService tokenService;

//    /**
//     * 根据手机号查询用户所有权限
//     * @param
//     * @return
//     */
//    @ApiOperation(value = "根据手机号查询用户所有权限")
//    @PostMapping(value = "/getUserAuthorityByPhone")
//    public Result getUserAuthorityByPhone(@RequestBody SysUserDto sysUserDto) throws Exception {
//        List<SysPermission> userAuthorityByPhone = sysUserService.getUserAuthorityByPhone(sysUserDto);
//        return new Result(HttpStatus.SUCCESS, "查询用户所有权限成功！",userAuthorityByPhone);
//    }

    /**
     * 根据用户名查询用户姓名
     * @param
     * @return
     */
    @ApiOperation(value = "根据用户名查询用户姓名")
    @GetMapping(value = "/getRealNameByName")
    public Result getRealNameByName(String name) {
        PfOperator realNameByName = sysUserService.getRealNameByName(name);
        return new Result(HttpStatus.SUCCESS, "根据用户名查询用户姓名成功！",realNameByName);
    }

    /**
     * 发送验证码
     * 输入邮箱和用户名调用邮箱API获取验证码存入redis中
     *
     * @param sysUserDto 用户名、邮箱
     * @return Result
     */
    @ApiOperation(value = "输入邮箱和用户名发送验证码")
    @GetMapping(value = "/getCode")
    public Result getCode(SysUserDto sysUserDto) throws Exception {
        //如果邮箱和用户名都不为空
        if(StringUtils.isNotEmpty(sysUserDto.getName()) && StringUtils.isNotEmpty(sysUserDto.getEmail())){
            //判断输入的邮箱和用户名是否正确
            int result = sysUserService.getCode(sysUserDto.getName(), sysUserDto.getEmail());
            //如果邮箱和用户名正确
            if (result > SysUserConstant.RETURN_AFFECTED_ROWS) {
                return new Result(HttpStatus.SUCCESS, "用户名和邮箱输入正确，验证码已发送至您的邮箱");
            }else{
                return new Result(HttpStatus.ERROR, "用户名或邮箱不匹配！");
            }
        }else{
            throw new BaseException("邮箱或者用户名为空！");
        }
    }

    /**
     * 判断输入的邮箱验证码和缓存在redis中邮箱验证码是否一致
     *
     * @param sysUserDto 邮箱验证码
     * @return Result
     */
    @ApiOperation(value = "验证邮箱验证码是否正确")
    @GetMapping(value = "/verifyCode")
    public Result verifyCode(SysUserDto sysUserDto) {
        try{
            //如果输入的邮箱验证码不为空
            if (StringUtils.isNotEmpty(sysUserDto.getCode())){
                //将验证码从redis中取出来
                String redisCode = redisService.getCacheObject(sysUserDto.getEmail());
                //若验证码没有过期
                if (StringUtils.isNotEmpty(redisCode)) {
                    //再判断输入的验证码和redis中的验证码是否正确
                    if (StringUtils.equals(sysUserDto.getCode(), redisCode)) {
                        //输入的验证码正确
                        return new Result(HttpStatus.SUCCESS, "邮箱验证码正确！");
                    }
                    //输入的验证码错误
                    else {
                        return new Result(HttpStatus.ERROR, "邮箱验证码错误！");
                    }
                }
                else {
                    return new Result(HttpStatus.ERROR, "邮箱验证码已过期！");
                }
            }
            else{
                throw new BaseException("邮箱验证码为空！");
            }
        //若调用redis服务出现异常则捕获异常
        }catch (Exception e){
            LogUtils.printLog(e);
            throw new BaseException("调用redis服务异常！");
        }
    }

    /**
     * 根据正确的验证码修改密码
     * 用户忘记原密码根据邮箱验证码修改密码
     *
     * @param sysUserDto 新密码 用户名
     * @return Result
     */
    @ApiOperation(value = "根据正确的验证码修改密码")
    @PostMapping(value = "/disRememberModifyPwd")
    @Log(title = "根据验证码修改密码",operType = OperType.UPDATE,content = "忘记原密码根据邮箱验证码修改密码")
    public Result disRememberModifyPwd(SysUserDto sysUserDto, HttpServletRequest request) {
        //如果输入的新密码和用户名不为空
        if (StringUtils.isNotEmpty(sysUserDto.getName()) && StringUtils.isNotEmpty(sysUserDto.getNewPwd())){
            //调用用户管理逻辑层的修改密码的方法
            int disRememberModifyPwd = sysUserService.modifyPwd(sysUserDto.getName(), sysUserDto.getNewPwd());
            //如果修改密码成功
            if(disRememberModifyPwd>SysUserConstant.RETURN_AFFECTED_ROWS){
                //调用退出登录的接口，清除页面缓存
                userRemoteService.logout(request);
                return new Result(HttpStatus.SUCCESS, "修改密码成功！");
            }else{
                return new Result(HttpStatus.ERROR, "修改密码失败！");
            }
        }
        else{
            //新密码或者用户名为空，直接调用公共模块抛异常的方法抛出异常
            throw new BaseException("用户名或者新密码为空！");
        }
    }

    /**
     * 根据原密码修改密码
     * 用户根据原密码修改密码
     *
     * @param modifyPwdDto 用户原密码、用户名、新密码
     * @return Result
     */
    @ApiOperation(value = "根据原密码修改密码")
    @PostMapping(value = "/rememberModifyPwd")
    @Log(title = "修改密码",operType = OperType.UPDATE,content = "用户根据原密码修改密码")
    public Result rememberModifyPwd(ModifyPwdDto modifyPwdDto, HttpServletRequest request) {
        System.out.println(modifyPwdDto);
        //如果用户原密码、用户名、新密码不为空
        if (StringUtils.isNotEmpty(modifyPwdDto.getOldPwd()) && StringUtils.isNotEmpty(modifyPwdDto.getOptName()) && StringUtils.isNotEmpty(modifyPwdDto.getNewPwd())){
            //根据用户名和用户原密码判断是否是该用户对应的密码和用户名
            int count = sysUserService.selectUserPwd(modifyPwdDto.getOldPwd(),modifyPwdDto.getOptName());
            //如果该用户有对应的用户名和原密码，则修改密码
            if (count > SysUserConstant.RETURN_AFFECTED_ROWS) {
                //修改密码操作
                int result = sysUserService.modifyPwd(modifyPwdDto.getOptName(),modifyPwdDto.getNewPwd());
                //如果修改密码成功
                if (result > SysUserConstant.RETURN_AFFECTED_ROWS) {
                    userRemoteService.logout(request);
                    return new Result(HttpStatus.SUCCESS, "修改成功！");
                } else {
                    return new Result(HttpStatus.ERROR, "修改失败！");
                }
            } else {
                return new Result(HttpStatus.ERROR, "没有对应的用户名和密码匹配！");
            }
        }else{
            throw new BaseException("原密码或用户名或新密码为空！");
        }
    }

    /**
     * 查询用户列表
     *
     * @param path
     * @return Result
     */
    @ApiOperation(value = "根据条件查询用户列表")
    @GetMapping(value = "/selectUser")
//    @PreAuthorize(hasPermi = "auth:user:list")
    public Result selectUser(String path) {
//        //开启分页
//        PageHelperUtil.startPage(sysUserDto.getPageNum(), sysUserDto.getPageSize());
        //获取查询的用户列表
        List<PfOperator> pfOperatorList = sysUserService.selectUser(path);
//        List<PfOperator> pfOperatorList1 = new ArrayList<>();
//        //根据查询出来的用户循环判断
//        for (int i = 0; i < pfOperatorList.size(); i++) {
//            //判断父级id为1的用户加入list1
//            if (pfOperatorList.get(i).getParentId() == 1){
//                pfOperatorList1.add(pfOperatorList.get(i));
//            }
//        }
//        //循环判断list1
//        for (int i = 0; i < pfOperatorList1.size(); i++) {
//            List<PfOperator> pfOperatorList2 = new ArrayList<>();
//            //循环判断查询出来的用户
//            for (int j = 0; j < pfOperatorList.size(); j++) {
//                //如果查询出来的用户的父级id与list1的当前id相同则往list2新增数据
//                if (pfOperatorList.get(j).getParentId() == pfOperatorList1.get(i).getOptId()){
//                    pfOperatorList2.add(pfOperatorList.get(j));
//                    List<PfOperator> pfOperatorList3 = new ArrayList<>();
//                    //如下循环同上
//                    for (int k = 0; k < pfOperatorList.size(); k++) {
//                        if (pfOperatorList.get(k).getParentId() == pfOperatorList.get(j).getOptId()){
//                            pfOperatorList3.add(pfOperatorList.get(k));
//                        }
//                    }
//                    pfOperatorList.get(j).setPfOperatorList(pfOperatorList3);
//                }
//            }
//            pfOperatorList1.get(i).setPfOperatorList(pfOperatorList2);
//        }
        List<PfOperator> pfOperatorList1 = sysUserService.selPfOperatorByPath(path);
        PfOperator.genTreeByDg(pfOperatorList,pfOperatorList1);
        return new Result(HttpStatus.SUCCESS, "查询用户成功！", pfOperatorList1);
    }

    /**
     *
     * 根据手机号查询操作员
     * @param phone
     * @return Result
     */
    @ApiOperation(value = "根据手机号查询操作员")
    @GetMapping(value = "/selPfOperatorByPhone")
    public Result selPfOperatorByPhone(String phone) {
        List<PfOperator> pfOperatorList = sysUserService.selPfOperatorByPhone(phone);
        return new Result(HttpStatus.SUCCESS, "查询成功", pfOperatorList);
    }

    /**
     *
     * 根据路径查询该路径下所有操作员ID
     * @param path
     * @return Result
     */
    @ApiOperation(value = "根据路径查询该路径下所有操作员ID")
    @GetMapping(value = "/selectUserId")
//    @PreAuthorize(hasPermi = "auth:user:list")
    public Result selectOptId(String path) {
        List<PfOperator> pfOperatorList = sysUserService.selectUser(path);
        return new Result(HttpStatus.SUCCESS, "查询用户成功！", pfOperatorList);
    }

    /**
     * 用户根据id查询出基本信息
     * @param
     * @return
     */
    @ApiOperation(value = "用户根据id查询出基本信息")
    @GetMapping(value = "/getPersonalInfo")
    public Result getPersonalInfo(SysUserDto sysUserDto) {
        //如果用户id不为空
        if (StringUtils.isNotNull(sysUserDto.getId())) {
            //根据用户id查询用户详情
            PfOperatorVo pfOperator = sysUserService.getPersonalInfo(sysUserDto.getId());
            if (StringUtils.isNotBlank(pfOperator.getFileId())){
                FileMgr fileMgr = sysUserService.getFileInfo(pfOperator.getFileId());
                if (fileMgr != null) {
                    pfOperator.setBase64Photo(FileUtils.getBaseImg(fileMgr.getFileUrl()));
                }
            }
            return new Result(HttpStatus.SUCCESS, "查询用户成功！", pfOperator);
        }else{
            throw new BaseException("用户id为空！");
        }
    }


    /**
     * 根据用户id修改背景图路径
     * @param
     * @return
     */
    @ApiOperation(value = "根据用户id修改背景图路径")
    @PostMapping(value = "/modifyBgmUrl")
    public Result modifyBgmUrl(@RequestBody ModifyUserDto modifyUserDto){
        Integer modifyBgmUrl = sysUserService.modifyBgmUrl(modifyUserDto);
        if (modifyBgmUrl > SysUserConstant.RETURN_AFFECTED_ROWS){
            return new Result(HttpStatus.SUCCESS, "修改用户背景图成功！");
        }
        return new Result(HttpStatus.ERROR, "修改用户背景图失败！");
    }

        /**
         * 用户根据用户id修改本人信息
         *
         * @param sysUserDto 用户真实姓名、手机号、邮箱、用户id（必传）
         * @return Result
         */
        @Log(title = "基本资料",operType = OperType.UPDATE,content = "用户编辑基本资料")
        @ApiOperation(value = "修改本人信息")
        @PostMapping(value = "/modifyPersonalInfo")
        public Result modifyPersonalInfo (@RequestBody SysUserDto sysUserDto){
            //如果用户id不为空
            if(StringUtils.isNotNull(sysUserDto.getId())){
                //如果用户真实姓名、邮箱不为空
                if(StringUtils.isNotEmpty(sysUserDto.getRealName()) && StringUtils.isNotEmpty(sysUserDto.getEmail())){
                    //用户修改本人信息
                    int result = sysUserService.modifyPersonalInfo(sysUserDto);
                    if (result>SysUserConstant.RETURN_AFFECTED_ROWS) {
                        return new Result(HttpStatus.SUCCESS, "用户修改本人信息成功");
                    }else{
                        return new Result(HttpStatus.ERROR, "用户修改本人信息失败");
                    }
                }else{
                    throw new BaseException("用户信息为空");
                }
            }else{
                throw new BaseException("用户id为空");
            }
        }


    /**
     * 根据操作员ID查询其所拥有的菜单权限
     *
     * @return Result
     */
    @ApiOperation(value = "根据操作员ID查询其所拥有的菜单权限")
    @GetMapping(value = "/selPfMenuById")
    public Result selPfMenuById(Integer id) {
        List<PfMenu> pfMenuList = sysUserService.selPfMenuById(id);

        return new Result(HttpStatus.SUCCESS, "获取菜单信息成功",pfMenuList);
    }

    /**
     * 根据操作员ID查询其所拥有的部门权限
     *
     * @return Result
     */
    @ApiOperation(value = "根据操作员ID查询其所拥有的部门权限")
    @GetMapping(value = "/selPfDepartById")
    public DataGridView selPfDepartById(Integer id) {
        List<PfDepart> pfMenuList = sysUserService.selPfDepartById(id);
        List<TreeNode> treeNodes = new ArrayList<>();
        //将区域放入treeNodes中，组装成json
        for (PfDepart pfDepart : pfMenuList) {
            treeNodes.add(new TreeNode(pfDepart.getId(), pfDepart.getParentId(), pfDepart.getDeptName(),pfDepart.getLastLevel()));
        }
        return new DataGridView(treeNodes);
    }

    /**
     * 根据操作员ID查询其所拥有的所有部门权限
     *
     * @return Result
     */
    @ApiOperation(value = "根据操作员ID查询其所拥有的所有部门权限")
    @GetMapping(value = "/selDepartByOptId")
    public Result selDepartByOptId(Integer id) {
        List<DepartVo> res = sysUserService.selDepartByOptId(id);
        List<DepartVo> departVos = sysUserService.selDepartByOptId(id);
        List<DepartVo> dest = new ArrayList<>();
        dest.add(departVos.get(0));
        genTreeByDg(res, dest);
        if (id == 1){
            return new Result(HttpStatus.SUCCESS, "获取组织机构信息成功",dest);
        }
        return new Result(HttpStatus.SUCCESS, "获取组织机构信息成功",dest.get(0).getTreeList());
    }

    private void genTreeByDg(List<DepartVo> res, List<DepartVo> dest) {

        if(dest.isEmpty()){
            return;
        }

        renderLastLevel(res);
        renderLastLevel(dest);
        DepartVo.genTreeByDg(res, dest);
    }
    private void renderLastLevel(List<DepartVo> dest) {
        for(DepartVo dv : dest){
            if("1".equals(dv.getLastLevel())){
                dv.setLastLevelFmt("是");
            }else{
                dv.setLastLevelFmt("否");
            }
        }
    }

    /**
     * 上传logo
     *
     * @return
     */
    @ApiOperation(value = "上传logo", notes = "上传logo")
    @ResponseBody
    @RequestMapping(value = "/logoUpload", method = RequestMethod.POST)
    public Result logoUpload(@RequestPart MultipartFile file) throws IOException {
        try {
            if (file.isEmpty()) {
                return new Result(HttpStatus.ERROR, "文件为空", null);
            }
            String oldFileName = file.getOriginalFilename();
            String suffixName = oldFileName.substring(oldFileName.lastIndexOf("."));
            //生成文件名称
            String fileId = IdUtils.fastUUID();
            String fileName = fileId + suffixName;
            //设置文件上传的路径
            String filePath;
            if (FileUploadUtil.isLinux() == false) {
                filePath = Constants.DEPART_WINDOWS_PHOTO_ADDRESS + File.separator;
            } else {
                filePath = Constants.DEPART_LINUX_PHOTO_ADDRESS + File.separator;
            }
            File dest = new File(filePath + fileName);
            //检测上传的文件目录是否存在
            if (!dest.getParentFile().exists()) {
                //不存在则新建文件夹
                dest.getParentFile().mkdirs();
            }
            //上传文件
            file.transferTo(dest);
            //设置文件信息
            FileMgr fileMgr = new FileMgr();
            fileMgr.setId(fileId);
            fileMgr.setType(FileConstants.FILE_TYPE_PHOTO);
            fileMgr.setFileUrl(filePath + fileName);
            fileMgr.setUploadTime(DateUtils.nowDate());
            fileMgr.setFileName(fileName);
            fileMgr.setSize(file.getSize());
            //调用保存文件方法
            Integer count  = sysUserService.saveFileInfo(fileMgr);
            if (count <= 0) {
                File faceFile = new File(filePath + fileName);
                faceFile.delete();
                return new Result(HttpStatus.ERROR, "上传失败", null);
            } else {
                return new Result(HttpStatus.SUCCESS, "上传成功", fileMgr);
            }

        } catch (MaxUploadSizeExceededException e) {
            return new Result(HttpStatus.ERROR, "文件大小超过200MB,请重新上传", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(HttpStatus.ERROR, "上传失败", null);
    }

    /**
     * 添加操作员
     *
     * @return Result
     */
    @ApiOperation(value = "添加操作员")
    @Log(title = "添加操作员",operType = OperType.INSERT,content = "添加")
    @PostMapping(value = "/addPfOperator")
    public Result addPfOperator(@RequestBody PfOperatorDto pfOperatorDto) {
        System.out.println(pfOperatorDto);
        //判断添加的操作员账号是否已存在
        int count = sysUserService.pfOperatorIsExist(pfOperatorDto.getOptName());
        if (count > 0){
            return new Result(HttpStatus.ERROR,"该账号已存在",null);
        }
        //为操作员密码进行MD5加密
        pfOperatorDto.setPsw(MD5Uitls.MD5Lower(pfOperatorDto.getPsw(), pfOperatorDto.getOptName()));
        //添加操作员
        int row = sysUserService.addPfOperator(pfOperatorDto);
        if (row > 0) {
            //判断添加的操作员权限是否为空
            if (pfOperatorDto.getPfOperatorRatList().size() != 0)  {
                //如果添加操作员成功且权限不为空,则为操作员的权限集合set操作员的ID
                for (int i = 0; i < pfOperatorDto.getPfOperatorRatList().size(); i++) {
                    pfOperatorDto.getPfOperatorRatList().get(i).setId(IdUtils.fastSimpleUUID());
                    pfOperatorDto.getPfOperatorRatList().get(i).setOptId(pfOperatorDto.getId());
                }
                //添加操作员权限
                int counts = sysUserService.addPfOperatorRat(pfOperatorDto.getPfOperatorRatList());
                if (counts > 0) {
                    return new Result(HttpStatus.SUCCESS, "添加成功", pfOperatorDto.getId());
                }
                return new Result(HttpStatus.ERROR, "添加失败", counts);
            }
            return new Result(HttpStatus.SUCCESS,"添加成功",pfOperatorDto.getId());
        }
        return new Result(HttpStatus.ERROR,"添加失败",row);
    }

    /**
     * 修改操作员
     *
     * @return Result
     */
    @ApiOperation(value = "修改操作员")
    @Log(title = "修改",operType = OperType.UPDATE,content = "修改")
    @PostMapping(value = "/updatePfOperator")
    public Result updatePfOperator(@RequestBody PfOperatorDto pfOperatorDto) {
        if (!pfOperatorDto.getPsw().equals("")) {
            //为操作员密码进行MD5加密
            pfOperatorDto.setPsw(MD5Uitls.MD5Lower(pfOperatorDto.getPsw(), pfOperatorDto.getOptName()));
        }
        //修改操作员信息
        int count = sysUserService.updatePfOperator(pfOperatorDto);
        if (count > 0){
            //如果操作员id为1或者2则为管理员，不能进行权限修改
            if (pfOperatorDto.getId() != 1 || pfOperatorDto.getId() != 2) {
                //如果操作员权限不为空,则先删除操作员原来的权限,新增要修改的权限
                if (pfOperatorDto.getPfOperatorRatList().size() != 0) {
                    //如果修改操作员信息成功且权限不为空,则为操作员的权限集合set操作员的ID
                    for (int i = 0; i < pfOperatorDto.getPfOperatorRatList().size(); i++) {
                        pfOperatorDto.getPfOperatorRatList().get(i).setId(IdUtils.fastSimpleUUID());
                        pfOperatorDto.getPfOperatorRatList().get(i).setOptId(pfOperatorDto.getId());
                    }
                    //根据管理员id删除权限
                    int count1 = sysUserService.delPfOperatorRat(pfOperatorDto.getId());
                    if (count1 >= 0) {
                        //添加权限
                        int count2 = sysUserService.addPfOperatorRat(pfOperatorDto.getPfOperatorRatList());
                        if (count2 > 0) {
                            return new Result(HttpStatus.SUCCESS, "修改成功", count2);
                        }
                        return new Result(HttpStatus.ERROR, "修改失败", count2);
                    }
                    return new Result(HttpStatus.ERROR, "修改失败", count1);
                }
            }
            return new Result(HttpStatus.SUCCESS,"修改成功",count);
        }
        return new Result(HttpStatus.ERROR,"修改失败",count);
    }

    /**
     * 根据操作员id删除操作员信息
     *
     * @return Result
     */
    @ApiOperation(value = "根据操作员id删除操作员信息")
    @Log(title = "删除操作员",operType = OperType.DELETE,content = "删除")
    @PostMapping(value = "/delPfOperator")
    public Result delPfOperator(Integer optId) {
        System.out.println(optId);
        //如果optId为1或者2则是管理员账号不可删除
        if (optId != 1 || optId != 2) {
            int row = sysUserService.selPfOperatorByParentId(optId);
            if (row == 0) {
                //删除账号权限
                int count = sysUserService.delPfOperatorRat(optId);
                if (count >= 0) {
                    //删除账号信息
                    int count1 = sysUserService.delPfOperator(optId);
                    if (count1 > 0) {
                        //判断要删除的操作员是否已登录，已登录则让账号退出
                        tokenService.delLoginPfOperator(optId);
                        return new Result(HttpStatus.SUCCESS, "删除成功", count1);
                    }
                    return new Result(HttpStatus.ERROR, "删除失败", count1);
                }
                return new Result(HttpStatus.ERROR, "删除失败", count);
            }
            return new Result(HttpStatus.ERROR,"请先删除该账号下的子账号",row);
        }
        return new Result(HttpStatus.ERROR,"无法删除管理员账号",null);
    }

    /**
     * 按类型查询数据字典
     *
     * @return Result
     */
    @ApiOperation(value = "按类型查询数据字典")
    @PostMapping(value = "/selSysDataDictionaryByType")
    public Result selSysDataDictionaryByType(SysDataDictionaryDto sysDataDictionaryDto) {
        //开启分页
        PageHelperUtil.startPage(sysDataDictionaryDto.getPageNum(), sysDataDictionaryDto.getPageSize());
        List<SysDataDictionary> sysDataDictionaryList = sysUserService.selSysDataDictionaryByType(sysDataDictionaryDto.getType());
        //将数据进行分页
        PageInfo<SysDataDictionary> pageInfo = new PageInfo<>(sysDataDictionaryList);
        return new Result(HttpStatus.SUCCESS,"查询成功",pageInfo);
    }

    /**
     * 按类型查询数据字典
     *
     * @return Result
     */
    @ApiOperation(value = "按类型查询数据字典")
    @PostMapping(value = "/selDataDictionaryByType")
    public Result selDataDictionaryByType(Integer type) {
        List<SysDataDictionary> sysDataDictionaryList = sysUserService.selSysDataDictionaryByType(type);
        return new Result(HttpStatus.SUCCESS,"查询成功",sysDataDictionaryList);
    }


    /**
     * 添加数据字典
     *
     * @return Result
     */
    @ApiOperation(value = "添加数据字典")
    @PostMapping(value = "/addSysDataDictionary")
    public Result addSysDataDictionary(SysDataDictionary sysDataDictionary) {
        int count = sysUserService.selSysDataDictionaryIsExist(sysDataDictionary);
        if (count == 0){
            int count1 = sysUserService.addSysDataDictionary(sysDataDictionary);
            if (count1 > 0){
                return new Result(HttpStatus.SUCCESS,"添加成功",count1);
            }
            return new Result(HttpStatus.ERROR,"添加失败",count1);
        }
        return new Result(HttpStatus.ERROR,"已存在相同数据",count);
    }

    /**
     * 根据ID修改字典数据
     *
     * @return Result
     */
    @ApiOperation(value = "根据ID修改字典数据")
    @PostMapping(value = "/updateSysDataDictionary")
    public Result updateSysDataDictionary(SysDataDictionary sysDataDictionary) {
        int count = sysUserService.selSysDataDictionaryIsExist(sysDataDictionary);
        if (count == 0){
            int count1 = sysUserService.updateSysDataDictionary(sysDataDictionary);
            if (count1 > 0){
                return new Result(HttpStatus.SUCCESS,"修改成功",count1);
            }
            return new Result(HttpStatus.ERROR,"修改失败",count1);
        }
        return new Result(HttpStatus.ERROR,"已存在相同数据",count);
    }


    /**
     * 根据id删除数据字典
     *
     *
     *
     * @return Result
     */
    @ApiOperation(value = "根据id删除数据字典")
    @PostMapping(value = "/delSysDataDictionary")
    public Result delSysDataDictionary(Integer id) {
        int count = sysUserService.delSysDataDictionary(id);
        if (count > 0){
            return new Result(HttpStatus.SUCCESS,"删除成功",count);
        }
        return new Result(HttpStatus.ERROR,"删除失败",count);
    }
    private ImportUserListener importUserListener = null;
    @ApiOperation("批量导入班级学校")
    @PostMapping("/importUser")
    public Result importUser(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws Exception {
            try {
                importUserListener = new ImportUserListener(sysUserService,departService);
                String fileName = UUID.randomUUID() + ".xlsx";
                if (!file.isEmpty()) {

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
                    EasyExcel.read(path, EllDepartVo.class, importUserListener).sheet().doRead();
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