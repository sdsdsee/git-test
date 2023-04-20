package com.eluolang.module.sys.user.controller;

import com.alibaba.excel.EasyExcel;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.pojo.PfDepart;
import com.eluolang.common.core.util.DateUtils;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.common.core.web.Result;
import com.eluolang.module.sys.user.Listener.ImportDepartListener;
import com.eluolang.module.sys.user.dto.DepartDto;
import com.eluolang.module.sys.user.model.PfOperatorVo;
import com.eluolang.module.sys.user.service.DepartService;
import com.eluolang.module.sys.user.service.OperatorRatService;
import com.eluolang.module.sys.user.service.SysUserService;
import com.eluolang.module.sys.user.util.EasyExcelColumnTypeUtil;
import com.eluolang.module.sys.user.util.PathGeneration;
import com.eluolang.module.sys.user.vo.DepartVo;
import com.eluolang.module.sys.user.vo.EllDeriveDepart;
import com.eluolang.module.sys.user.vo.EllImportDepartVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * 用户管理控制层
 *
 * @author ZengxiaoQian
 * @createDate 2020/8/25
 */
@Api("用户管理")
@RestController
@RequestMapping("/depart/mgr")
public class DepartController {

    private static final String MODULE = 2 + "";

    @Resource
    private DepartService departService;

    @Resource
    private OperatorRatService operatorRatService;

    @Resource
    private SysUserService sysUserService;

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static void main(String[] args) {
        System.out.println("isNumeric is " + isNumeric("abc"));
        System.out.println("isNumeric is " + isNumeric("123"));
        String parentPath = getParentPath("/a/");
        System.out.println("parentPath is " + parentPath);
    }

    /**
     * 通过path获取到父path
     *
     * @param path
     * @return
     */
    private static String getParentPath(String path) {
        String res = path.substring(0, path.lastIndexOf("/"));
        return res.substring(0, res.lastIndexOf("/") + 1);
    }

    /**
     * 添加或修改组织机构
     *
     * @param departDto
     * @return
     */
    @ApiOperation(value = "添加或修改组织机构")
    @PostMapping(value = "/regOrUpdateDepart")
    public Result regOrUpdateDepart(@RequestBody DepartDto departDto) throws Exception {
        int count = 0;
        // 先去数据库中查询是否存在该数据
        if (isNumeric(departDto.getId())) {
            count = departService.checkExistById(departDto.getId());

        } else {
            count = departService.checkExistByTid(departDto.getId());
        }
        if (count > 0) {
            return updateDepart(departDto);
        } else {
            return regDepart(departDto);
        }
    }

    @ApiOperation("导入部门")
    @PostMapping("/importDepart")
    public Result importUser(@RequestParam("file") MultipartFile file, HttpServletResponse response, String orgId) throws ParseException {
        ImportDepartListener importDepartListener = new ImportDepartListener(departService, orgId);
        String fileName = UUID.randomUUID() + ".xlsx";
        if (!file.isEmpty()) {
            try {
                //把文件写入到目标位置
                String path = "";
                if (FileUploadUtil.isLinux() == false) {
                    path = WindowsSite.importDepartPath + fileName;
                } else {
                    path = LinuxSite.importDepartPath + fileName;
                }
                File filepath = new File(path);
                //判断是否有文件路径
                PathGeneration.createPath(path);
                file.transferTo(filepath);//把文件写入目标文件地址
                EasyExcel.read(path, EllImportDepartVo.class, importDepartListener).sheet().doRead();
            } catch (IOException e) {
                e.printStackTrace();
                return new Result(HttpStatus.ERROR, "失败", e);
            }
        }
        return new Result(HttpStatus.SUCCESS, "添加成功！");
    }

    @ApiOperation("导出部门")
    @PostMapping("/deriveDepart")
    public Result deriveDepart(int accountOrgId, String deptId) throws ParseException {
        //只支持查询班级,年级,学校顺序的三级
        List<EllDeriveDepart> ellDeriveDepartList = departService.selDepart(accountOrgId, 0, deptId);
        //查询所有部门
        List<EllDeriveDepart> ellDeriveDepart = departService.selDepart(accountOrgId, 1, null);
        Map<Integer, EllDeriveDepart> departName = new HashMap<>();
        AtomicInteger count = new AtomicInteger();
        for (int i = 0; i < ellDeriveDepartList.size(); i++) {
            //判断查询过没有有就不用循环第二次了
            if (!departName.containsKey(ellDeriveDepartList.get(i).getParentId())) {
                for (int j = 0; j < ellDeriveDepart.size(); j++) {
                    //年级
                    if (ellDeriveDepart.get(j).getId() == ellDeriveDepartList.get(i).getParentId()) {
                        departName.put(ellDeriveDepart.get(j).getId(), ellDeriveDepart.get(j));
                        //学校
                        if (!departName.containsKey(ellDeriveDepart.get(j).getParentId())) {
                            for (int k = 0; k < ellDeriveDepart.size(); k++) {
                                departName.put(ellDeriveDepart.get(k).getId(), ellDeriveDepart.get(k));
                            }
                        }
                    }
                }

            }
          /*  System.out.println(count.get());
            count.getAndIncrement();*/

            try {
                //年级
                ellDeriveDepartList.get(i).setGrade(departName.get(ellDeriveDepartList.get(i).getParentId()).getDeptName());
                //学校
                ellDeriveDepartList.get(i).setSchoolName(departName.get(departName.get(ellDeriveDepartList.get(i).getParentId()).getParentId()).getDeptName());
            } catch (Exception e) {
                ellDeriveDepartList.get(i).setSchoolName("这条为第一层级部门，不是班级");
                ellDeriveDepartList.get(i).setGrade("这条为第一层级部门，不是班级");
                ellDeriveDepartList.get(i).setId(00000000000000000);
                ellDeriveDepartList.get(i).setDeptName("这条为第一层级部门，不是班级");
            }

        }
        String fileName = UUID.randomUUID() + ".xlsx";
        String path = "";
        String url = "";
        //判断是否为windows
        if (FileUploadUtil.isLinux() == false) {
            path = WindowsSite.fileDepartPath;
            url = WindowsSite.DEPART_NGINX_PATH_Excel + fileName;
        } else {
            path = LinuxSite.fileDepartPath;
            url = LinuxSite.DEPART_NGINX_PATH_Excel + fileName;
        }
        //判断是否有文件路径
        PathGeneration.createPath(path + fileName);
        EasyExcel.write(path + fileName, EllDeriveDepart.class).sheet("模板").doWrite(ellDeriveDepartList);
        File file = new File(path + fileName);
        return new Result(HttpStatus.SUCCESS, "添加成功！", url);
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
        String fileName = "ImportDepartTemplate.xlsx";
        File file = new File(template + fileName);
        String path = template + fileName;
        //判断是否有文件路径
        PathGeneration.createPath(path);
        //判断文件是否存在
        if (!file.exists()) {
            List<EllImportDepartVo> ellUserList = new ArrayList<>();
            EasyExcel easyExcel = new EasyExcel();
            easyExcel.write(path, EllImportDepartVo.class).registerWriteHandler(new EasyExcelColumnTypeUtil()).sheet("模板").doWrite(ellUserList);
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

    /**
     * 添加组织机构
     *
     * @param departDto
     * @return
     */
    @ApiOperation(value = "添加组织机构")
    @PostMapping(value = "/regDepart")
    public Result regDepart(@RequestBody DepartDto departDto) throws Exception {
        return regDept(departDto);
    }

    /**
     * 添加根节点组织机构
     *
     * @param departDto
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "添加根节点组织机构")
    @PostMapping(value = "/regRootDepart")
    public Result regRootDepart(@RequestBody DepartDto departDto) throws Exception {
        // 加入组织机构时，path为该用户的路径
        // 加入parentId：通过parentPath查询出组织机构id
        String parentPath = getParentPath(departDto.getPath());
        String id = "0";
        // 如果不为根节点
        if (!"/".equals(parentPath)) {
            List<DepartVo> res = departService.getDepartByPath(parentPath);
            id = res.get(0).getId() + "";
        }
        departDto.setParentId(id);
        return regDepart(departDto);
    }

    private Result regDept(DepartDto departDto) {
        String nowDate = DateUtils.nowDate();
        departDto.setCreatedTime(nowDate);
//    	departDto.setCreatedBy("xxx");
        departDto.setUpdatedTime(nowDate);
        departDto.setUpdatedBy(departDto.getCreatedBy());

        // parentId为数字
        if (isNumeric(departDto.getParentId())) {

            int res = departService.regDepart(departDto);
            PfOperatorVo pfOperatorVo = sysUserService.getPersonalInfo(Integer.valueOf(departDto.getOptId()));
            String jie = pfOperatorVo.getPath().substring(1, pfOperatorVo.getPath().length() - 1);
            String[] item = jie.split("/");
            for (int i = 1; i < item.length; i++) {
                String uuid = IdUtils.fastSimpleUUID();
                // 注册之后，维护操作关系表
                operatorRatService.regOperatorRat(uuid, item[i], MODULE, departDto.getId());
            }


            if (res > 0) {
                int updateDepartPathById = departService.updateDepartPathById(departDto.getId());
                if (updateDepartPathById > 0) {
                    return new Result(HttpStatus.SUCCESS, "添加组织机构成功", departDto);
                }
            }

        } else {
            String tid = departDto.getId();
            // 保存数据到数据库记录下tid和tpid
            int res = departService.regDepartByTree(departDto);
            PfOperatorVo pfOperatorVo = sysUserService.getPersonalInfo(Integer.valueOf(departDto.getOptId()));
            String jie = pfOperatorVo.getPath().substring(3, pfOperatorVo.getPath().length() - 1);
            String[] item = jie.split("/");
            for (int i = 0; i < item.length; i++) {
                String uuid = IdUtils.fastSimpleUUID();
                // 注册之后，维护操作关系表
                operatorRatService.regOperatorRat(uuid, item[i], MODULE, departDto.getId());
            }

            if (res > 0) {
                // 更新本节点的parentId和path
                int updateDepartPathAndPidByTid = departService.updateDepartPathAndPidByTid(tid);
                if (updateDepartPathAndPidByTid > 0) {
                    return new Result(HttpStatus.SUCCESS, "添加组织机构成功", departDto);
                }
            }

        }


        return new Result(HttpStatus.ERROR, "添加组织机构失败");
    }

    /**
     * 更新组织机构
     *
     * @param departDto
     * @return
     */
    @ApiOperation(value = "更新组织机构")
    @PostMapping(value = "/updateDepart")
    public Result updateDepart(@RequestBody DepartDto departDto) throws Exception {
        return updateDept(departDto);
    }

    private Result updateDept(DepartDto departDto) {
        departDto.setUpdatedTime(DateUtils.nowDate());
//    	departDto.setUpdatedBy("xxx");

        int res = 0;
        // id为数字
        if (isNumeric(departDto.getId())) {
            res = departService.updateDepart(departDto);

        } else {
            res = departService.updateDepartByTid(departDto);
        }
        if (res > 0) {
            return new Result(HttpStatus.SUCCESS, "更新组织机构成功");
        } else {
            return new Result(HttpStatus.ERROR, "更新组织机构失败");
        }
    }

    /**
     * 删除组织机构
     *
     * @param departDto
     * @return
     */
    @ApiOperation(value = "删除组织机构")
    @PostMapping(value = "/delDepartById")
    public Result delDepartById(@RequestBody DepartDto departDto) throws Exception {
        // 先查询出是否有子元素
        // 通过id查询出path
        // 通过path查询数量
        String id = departDto.getId();
        int count = 0;
        if (isNumeric(id)) {
            count = departService.queryChildCountById(id);

        } else {
            count = departService.queryChildCountByTid(id);
        }

        if (count > 1) {
            return new Result(HttpStatus.ERROR, "删除失败：不能有后代节点");
        } else {

            int res = 0;
            // 不是数字，需要查询出id
            if (!isNumeric(id)) {
                List<DepartVo> departByTreeId = departService.getDepartByTreeId(id);
                DepartVo departVo = departByTreeId.get(0);
                id = departVo.getId() + "";
            }

            int deptUseCountByIdFromEmployee = departService.queryUserByDeptId(id);
            if (deptUseCountByIdFromEmployee > 0) {
                return new Result(HttpStatus.ERROR, "删除组织机构失败:有人员正在使用该部门");
            }

            // 通过id删除组织机构
            res = departService.delDepartById(id);

            // 删除权限关系表
            operatorRatService.delOperatorRat(MODULE, id);

            if (res > 0) {
                return new Result(HttpStatus.SUCCESS, "删除组织机构成功");
            } else {
                return new Result(HttpStatus.ERROR, "删除组织机构失败");
            }
        }
    }

    /**
     * 通过操作员path获取组织机构信息
     *
     * @param departDto
     * @return
     */
    @ApiOperation(value = "通过操作员path获取组织机构信息")
    @PostMapping(value = "/getDeptByOperId")
    public Result getDepartByOperId(@RequestBody DepartDto departDto) throws Exception {
        String optId = departDto.getOptId();
        // 通过操作员id查询出授权的组织机构
        List<DepartVo> dest = departService.getDepartByOperId(optId);
        if (dest.isEmpty() || dest.size() == 1) {
            return new Result(HttpStatus.SUCCESS, "获取组织机构信息成功", dest);
        }

//		List<DepartVo> res = departService.getDepartByOperId(optId);
//
//		List<DepartVo> rootNodeList = new ArrayList<DepartVo>();
//		// 取到顶级节点
//		DepartVo rootDepartVo = dest.get(0);
//		rootNodeList.add(rootDepartVo);
//
//		List<DepartVo> resRootNodeList = new ArrayList<DepartVo>();
//		DepartVo rootDepartVo2 = res.get(0);
//		// 设置只读
//		rootDepartVo2.setReadOnly(true);
//		resRootNodeList.add(rootDepartVo2);
//
//		// 取到根节点下我添加的所有节点
//		List<DepartVo> departByMeAdd = departService.getDepartByOptIdAndPath(optId, rootDepartVo.getPath());
//		// 将查询出的所有节点与之前的节点取差集 -- 保留只读节点
////    	List<DepartVo> onlyReadNodeList = removeAll(dest, departByMeAdd);
//		for (DepartVo dvm : departByMeAdd) {
//			for (int i = 0; i < dest.size(); i++) {
//				DepartVo dv = dest.get(i);
//				// id相等则移出
//				if(dv.getId() == dvm.getId()){
//					dest.remove(i);
//				}
//			}
//		}
//		List<DepartVo> onlyReadNodeList = dest;
//
////    	// 将只读节点设置为只读
////    	for (DepartVo departVo : onlyReadNodeList) {
////    		departVo.setReadOnly(true);
////		}
//
//		Map<Integer, DepartVo> leafMap = new HashMap<Integer, DepartVo>();
//		// 对只读节点进行组装树，获取到叶子节点
//		genTreeByDg(onlyReadNodeList, rootNodeList,leafMap);
//		for (DepartVo departVo : onlyReadNodeList) {
//			if(leafMap.containsKey(departVo.getId())){
//				departVo.setLeaf(true);
//			}
//		}
//
//		// 循环赋值
//		for (DepartVo departVo : res) {
//			for(DepartVo rn : onlyReadNodeList){
//				if(rn.getId() == departVo.getId()){
//					departVo.setReadOnly(true);
//					if(rn.isLeaf()){
//						departVo.setLeaf(true);
//					}
//				}
//			}
//		}
//
//		// 组装树形结构到根节点
//		genTreeByDg(res, resRootNodeList);

        return new Result(HttpStatus.SUCCESS, "获取组织机构信息成功", dest);
    }

    public void installSigFmt(Map<String, String> map, List<DepartVo> dest) {
        for (DepartVo dv : dest) {
            dv.setSigFmt(map.get(dv.getSig()));
        }
    }
//
//    /**
//     * 通过操作员path获取组织机构信息
//     * @param optId
//     * @return
//     */
//    @ApiOperation(value = "通过操作员path获取组织机构信息")
//    @GetMapping(value = "/getDepartByOperId")
//    public Result getDepartByOperId(String optId) throws Exception {
//    	// 获取场景
//		List<SysDataDictionary> sysDataDictionaryList = sysUserService.selSysDataDictionaryByType(6);
//		HashMap<String,String> sddMap = new HashMap<String,String>();
//		String sig = "";
//		for (SysDataDictionary sdd:sysDataDictionaryList) {
//			sddMap.put(sdd.getId()+"",sdd.getName());
//		}
//		// 通过操作员id查询出授权的组织机构
//    	List<DepartVo> dest = departService.getDepartByOperId(optId);
//    	if(dest.isEmpty() || dest.size() == 1){
//    		return new Result(HttpStatus.SUCCESS, "获取组织机构信息成功",dest);
//    	}
//
//    	List<DepartVo> res = departService.getDepartByOperId(optId);
//
//    	// 设置sigFmt到集合中
//		installSigFmt(sddMap,dest);
//		installSigFmt(sddMap,res);
//
//    	List<DepartVo> rootNodeList = new ArrayList<DepartVo>();
//    	// 取到顶级节点
//    	DepartVo rootDepartVo = dest.get(0);
//    	rootNodeList.add(rootDepartVo);
//
//    	List<DepartVo> resRootNodeList = new ArrayList<DepartVo>();
//    	DepartVo rootDepartVo2 = res.get(0);
//    	// 设置只读
//    	rootDepartVo2.setReadOnly(true);
//    	resRootNodeList.add(rootDepartVo2);
//
//    	// 取到根节点下我添加的所有节点
//    	List<DepartVo> departByMeAdd = departService.getDepartByOptIdAndPath(optId, rootDepartVo.getPath());
//
//		installSigFmt(sddMap,departByMeAdd);
//    	// 将查询出的所有节点与之前的节点取差集 -- 保留只读节点
////    	List<DepartVo> onlyReadNodeList = removeAll(dest, departByMeAdd);
//    	for (DepartVo dvm : departByMeAdd) {
//    		for (int i = 0; i < dest.size(); i++) {
//    			DepartVo dv = dest.get(i);
//    			// id相等则移出
//				if(dv.getId() == dvm.getId()){
//					dest.remove(i);
//				}
//			}
//		}
//    	List<DepartVo> onlyReadNodeList = dest;
//
////    	// 将只读节点设置为只读
////    	for (DepartVo departVo : onlyReadNodeList) {
////    		departVo.setReadOnly(true);
////		}
//
//    	Map<Integer, DepartVo> leafMap = new HashMap<Integer, DepartVo>();
//    	// 对只读节点进行组装树，获取到叶子节点
//    	genTreeByDg(onlyReadNodeList, rootNodeList,leafMap);
//    	for (DepartVo departVo : onlyReadNodeList) {
//			if(leafMap.containsKey(departVo.getId())){
//				departVo.setLeaf(true);
//			}
//		}
//
//    	// 循环赋值
//    	for (DepartVo departVo : res) {
//			for(DepartVo rn : onlyReadNodeList){
//				if(rn.getId() == departVo.getId()){
//					departVo.setReadOnly(true);
//					if(rn.isLeaf()){
//						departVo.setLeaf(true);
//						sig = departVo.getSig();
//					}
//				}
//			}
//		}
//
//		// 组装树形结构到根节点
//    	genTreeByDg(res, resRootNodeList);
//
//		Map<String,Object> rst = new HashMap<String,Object>();
//		rst.put("deptDataArr",resRootNodeList);
//		rst.put("sigDataArr",sysDataDictionaryList);
//		rst.put("leafSig",sig);
//
//    	return new Result(HttpStatus.SUCCESS, "获取组织机构信息成功",rst);
//    }

    /**
     * 通过操作员path获取组织机构信息
     *
     * @param departDto
     * @return
     */
    @ApiOperation(value = "通过操作员path获取组织机构信息")
    @PostMapping(value = "/getDepartByOperPath")
    public Result getDepartByOperPath(@RequestBody DepartDto departDto) throws Exception {
        List<DepartVo> res = departService.getDepartByOperPath(departDto.getPath());
        List<DepartVo> dest = departService.getDepartByPath(departDto.getPath());
        genTreeByDg(res, dest);
        return new Result(HttpStatus.SUCCESS, "获取组织机构信息成功", dest);
    }

    /**
     * 根据部门id查询学校信息
     *
     * @param deptId
     * @return
     */
    @ApiOperation(value = "根据部门id查询学校信息")
    @PostMapping(value = "/selSchoolByDeptId")
    public Result selSchoolByDeptId(Integer deptId) {
        PfDepart pfDepart = departService.selSchoolByDeptId(deptId);
        if (pfDepart != null) {
            return new Result(HttpStatus.SUCCESS, "查询成功", pfDepart);
        }
        return new Result(HttpStatus.ERROR, "未查询到学校", null);
    }

    /**
     * 获取所有组织机构信息
     *
     * @param
     * @return
     */
    @ApiOperation(value = "获取所有组织机构信息")
    @GetMapping(value = "/getAllDepart")
    public Result getAllDepart() throws Exception {
        List<DepartVo> res = departService.getAllDepart();
        List<DepartVo> dest = new ArrayList<DepartVo>();
        genTreeByDg(res, dest);
        return new Result(HttpStatus.SUCCESS, "获取所有组织机构信息成功", dest);
    }

    private void genTreeByDg(List<DepartVo> res, List<DepartVo> dest) {

        if (dest.isEmpty()) {
            return;
        }

        renderLastLevel(res);
        renderLastLevel(dest);
        DepartVo.genTreeByDg(res, dest);
    }

    private void genTreeByDg(List<DepartVo> res, List<DepartVo> dest, Map<Integer, DepartVo> leafMap) {

        if (dest.isEmpty()) {
            return;
        }

        renderLastLevel(res);
        renderLastLevel(dest);
        DepartVo.genTreeByDg(res, dest, leafMap);
    }

    private void renderLastLevel(List<DepartVo> dest) {
        for (DepartVo dv : dest) {
            if ("1".equals(dv.getLastLevel())) {
                dv.setLastLevelFmt("是");
            } else {
                dv.setLastLevelFmt("否");
            }
        }
    }

    /**
     * 差集：删除左边集合中在右边集合存在的元素并返回
     *
     * @param left
     * @param right
     * @return
     */
    private static List<DepartVo> removeAll(List<DepartVo> left, List<DepartVo> right) {
        if (left == null) {
            return null;
        }
        if (right == null) {
            return left;
        }
        //使用LinkedList方便插入和删除
        List<DepartVo> res = new LinkedList<>(left);
        Set<Integer> set = new HashSet<>();
        //将DepartVo id存放到set
        for (DepartVo item : right) {
            set.add(item.getId());
        }
        //迭代器遍历listA
        Iterator<DepartVo> iter = res.iterator();
        while (iter.hasNext()) {
            DepartVo item = iter.next();
            //如果set中包含id则remove
            if (set.contains(item.getId())) {
                iter.remove();
            }
        }
        return res;
    }

}