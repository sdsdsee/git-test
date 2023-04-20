package com.eluolang.module.sys.user.Listener;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.pojo.ELLSymmetry;
import com.eluolang.common.core.pojo.EllUser;
import com.eluolang.common.core.util.DateUtils;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.module.sys.user.dto.DepartDto;
import com.eluolang.module.sys.user.model.EllDepartVo;
import com.eluolang.module.sys.user.service.DepartService;
import com.eluolang.module.sys.user.service.SysUserService;

import com.eluolang.module.sys.user.vo.DepartVo;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.ArrayUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Slf4j
@Transactional
public class ImportUserListener extends AnalysisEventListener<EllDepartVo> {
    private SysUserService sysUserService;
    private DepartService departService;
    private List<EllDepartVo> list = new ArrayList<>();

    //导入信息不对的人员
    private List<EllDepartVo> listMistake = new ArrayList<>();

    public ImportUserListener(SysUserService sysUserService,DepartService departService) {
        //这里是demo，所以随便new一个。实际使用如果到了spring请使用下面的有参构z造函数
        this.sysUserService = sysUserService;
        this.departService = departService;
    }

    @Override
    public void invoke(EllDepartVo ellDepartVo, AnalysisContext analysisContext) {
        list.add(ellDepartVo);
    }

    @SneakyThrows
    @Transactional
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                List<EllDepartVo> ellDepartVoList = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(o -> o.getSchoolId() + ";" + o.getGradeName()))), ArrayList::new));
                for (EllDepartVo e:ellDepartVoList) {
                    String schoolId = e.getSchoolId();
                    String gadeName = e.getGradeName();
                    int isExist = departService.checkExistById(e.getSchoolId());
                    if (isExist == 0){
                        throw new Exception("学校ID编号错误，请仔细核对");
                    }
                    DepartDto departDto = new DepartDto();
                    departDto.setParentId(String.valueOf(e.getSchoolId()));
                    departDto.setDeptName(e.getGradeName());
                    departDto.setLastLevel("0");
                    departDto.setIsSchool(0);
                    String nowDate = DateUtils.nowDate();
                    departDto.setCreatedTime(nowDate);
                    departDto.setUpdatedTime(nowDate);
                    departService.regDepart(departDto);
                    departService.updateDepartPathById(departDto.getId());
                  for (EllDepartVo e1:list) {
                      String schoolId1 = e1.getSchoolId();
                      String gadeName1 = e1.getGradeName();
                      if (schoolId1.equals(schoolId) && gadeName1.equals(gadeName)){
                          DepartDto departDto1 = new DepartDto();
                          departDto1.setParentId(departDto.getId());
                          departDto1.setDeptName(e1.getClassName());
                          departDto1.setLastLevel("1");
                          departDto1.setIsSchool(0);
                          String nowDate1 = DateUtils.nowDate();
                          departDto1.setCreatedTime(nowDate1);
                          departDto1.setUpdatedTime(nowDate1);
                          departService.regDepart(departDto1);
                          departService.updateDepartPathById(departDto1.getId());
                      }
                   }
                }
           }

}
