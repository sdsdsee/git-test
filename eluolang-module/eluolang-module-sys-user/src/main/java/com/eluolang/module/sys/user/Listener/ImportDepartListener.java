package com.eluolang.module.sys.user.Listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.eluolang.module.sys.user.dto.DepartDto;
import com.eluolang.module.sys.user.service.DepartService;
import com.eluolang.module.sys.user.util.CreateTime;
import com.eluolang.module.sys.user.vo.EllImportDepartVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ImportDepartListener extends AnalysisEventListener<EllImportDepartVo> {
    private DepartService departService;
    private String accountId;

    public ImportDepartListener(DepartService departService, String accountId) {
        this.departService = departService;
        this.accountId = accountId;
    }

    /**
     * 部门
     */
    private Map<String, List<DepartDto>> depart = new HashMap<>();

    /**
     * 年级
     */
    private Map<String, Map<String, DepartDto>> gradeDepart = new HashMap<>();
    /*    *//**
     * 计数
     *//*
    private int count = 0;*/
    /**
     * 班级
     */
    private List<DepartDto> list = new ArrayList<>();

    @Override
    public void invoke(EllImportDepartVo ellImportDepartVo, AnalysisContext analysisContext) {
        //更改年级编码
//        ellImportDepartVo.setGradeName(grade(ellImportDepartVo.getGradeName()));
        int i = 0;
        //判断是否有这个上级
        DepartDto departDto = departService.selDepartName(null, null, ellImportDepartVo.getCity());
        if (departDto != null) {
            while (i == 0) {
                //判断区有没有
                if (depart.containsKey(ellImportDepartVo.getDistrict())) {
                    //判断学校有没有
                    if (depart.containsKey(ellImportDepartVo.getSchoolName())) {
                        //判断该学习有没有该年级
                        if (gradeDepart.containsKey(ellImportDepartVo.getSchoolName()) && gradeDepart.get(ellImportDepartVo.getSchoolName()).containsKey(ellImportDepartVo.getGradeName())) {
                            //有就添加班级
                            DepartDto addDepartDto = new DepartDto();
                         /*   if (count > 0) {
                                addDepartDto.setId(String.valueOf(Integer.parseInt(list.get(0).getId()) + list.size() + 1));
                            } else {
                                addDepartDto.setId(String.valueOf(departService.selMaxId() + 1));
                            }*/
                            addDepartDto.setDeptName(ellImportDepartVo.getClassName());
                            addDepartDto.setLastLevel("1");
                            addDepartDto.setCreatedTime(CreateTime.getTime());
                            //上一级的path
                            addDepartDto.setPath(gradeDepart.get(ellImportDepartVo.getSchoolName()).get(ellImportDepartVo.getGradeName()).getPath());
                            addDepartDto.setUpdatedBy(CreateTime.getTime());
                            addDepartDto.setCreatedBy(CreateTime.getTime());
                            addDepartDto.setCreatedBy(CreateTime.getTime());
                            addDepartDto.setIsSchool(0);
                            addDepartDto.setLastLevel("1");
                            addDepartDto.setParentId(gradeDepart.get(ellImportDepartVo.getSchoolName()).get(ellImportDepartVo.getGradeName()).getId());
                            addDepartDto.setSig(ellImportDepartVo.getSign());
//                            System.out.println(addDepartDto.getId());
                            list.add(addDepartDto);
                            break;
                        } else {
                            uploudMap(ellImportDepartVo, ellImportDepartVo.getGradeName(), depart.get(ellImportDepartVo.getSchoolName()).get(0).getPath(), depart.get(ellImportDepartVo.getSchoolName()).get(0).getId(), 0, "0");
                            //存储年级
                            Map<String, DepartDto> departDtoMap = new HashMap<>();
                            departDtoMap.put(ellImportDepartVo.getGradeName(), depart.get(ellImportDepartVo.getGradeName()).get(0));
                            gradeDepart.put(ellImportDepartVo.getSchoolName(), departDtoMap);
                        }
                    } else {
                        uploudMap(ellImportDepartVo, ellImportDepartVo.getSchoolName(), depart.get(ellImportDepartVo.getDistrict()).get(0).getPath(), depart.get(ellImportDepartVo.getDistrict()).get(0).getId(), 1, "0");
                    }
                } else {
                    uploudMap(ellImportDepartVo, ellImportDepartVo.getDistrict(), departDto.getPath(), departDto.getId(), 0, "0");
                }
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (list != null && list.size() > 0) {
            //设置id和path
            AtomicInteger index = new AtomicInteger(1);
            list.stream().forEach(l -> {
                l.setId(String.valueOf(departService.selMaxId() + index.get()));
                l.setPath(l.getPath() + l.getId() + "/");
//                System.out.println(l.getId());
                index.incrementAndGet();  //先执行增加然后 再返回
            });
            departService.addDepart(list);
        }
    }

    public void uploudMap(EllImportDepartVo ellImportDepartVo, String departName, String path, String parentId, int isSchool, String last) {
        //没有就去查找
        DepartDto departDto = departService.selDepartName(departName, parentId, null);
        //没有查找到就添加存入map
        DepartDto addDepartDto = new DepartDto();
        if (departDto == null) {
            addDepartDto.setId(String.valueOf(departService.selMaxId() + 1));
            addDepartDto.setDeptName(departName);
            addDepartDto.setLastLevel(last);
//            addDepartDto.setPath("/1/100/" + path);
            addDepartDto.setCreatedTime(CreateTime.getTime());
            addDepartDto.setUpdatedBy(CreateTime.getTime());
            addDepartDto.setCreatedBy("system");
            addDepartDto.setCreatedBy("system");
            addDepartDto.setIsSchool(isSchool);
            addDepartDto.setPath(path + addDepartDto.getId() + "/");
            addDepartDto.setParentId(parentId);
            List<DepartDto> departDtos = new ArrayList<>();
//            addDepartDto = departService.addDepart(departDtos);
            departDtos.add(addDepartDto);
            departService.addDepart(departDtos);
            List<DepartDto> departMapList = new ArrayList<>();
            departMapList.add(departService.selDepartName(departName, parentId, null));
            depart.put(departName, departMapList);
        } else {
            List<DepartDto> integerList = new ArrayList<>();
            integerList.add(departDto);
            depart.put(departName, integerList);
        }
    }

    private String grade(String Grade) {
        switch (Integer.parseInt(Grade)) {
            case -1:
                Grade = "其他";
                break;
            case 11:
                Grade = "一年级";
                break;
            case 12:
                Grade = "二年级";
                break;
            case 13:
                Grade = "三年级";
                break;
            case 14:
                Grade = "四年级";
                break;
            case 15:
                Grade = "五年级";
                break;
            case 16:
                Grade = "六年级";
                break;
            case 21:
                Grade = "初一";
                break;
            case 22:
                Grade = "初二";
                break;
            case 23:
                Grade = "初三";
                break;
            case 31:
                Grade = "高一";
                break;
            case 32:
                Grade = "高二";
                break;
            case 33:
                Grade = "高三";
                break;
            case 41:
                Grade = "大一";
                break;
            case 42:
                Grade = "大二";
                break;
            case 43:
                Grade = "大三";
                break;
            case 44:
                Grade = "大四";
                break;
        }
        return Grade;
    }
}
