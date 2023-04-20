package com.eluolang.physical.Listener;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.physical.model.*;
import com.eluolang.physical.service.EllUserService;
import com.eluolang.physical.service.PlanService;
import com.eluolang.physical.util.IdCardUtil;
import com.eluolang.physical.util.IsIdCardUtil;
import com.eluolang.physical.util.PathGeneration;
import com.eluolang.physical.util.RSAUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

//取参数和构造
@Data
@Transactional
@Slf4j
public class BatchOptionalListener extends AnalysisEventListener<BatchOptionalVo> {
    private PlanService planService;
    private String planId;
    private EllUserService ellUserService;
    //公钥
    private String RsaPublic;
    //私钥
    private String RsaPrivate;

    //错误信息表
    private List<BatchOptionalVo> listMistake = new ArrayList<>();
    //正确的参数进行传输
    private List<BatchOptionalVo> addList = new ArrayList<>();
    private String fileUri = null;
    //学籍号
    private Map<String, String> idCardSame = new HashMap<>();

    public BatchOptionalListener(PlanService planService, String planId, EllUserService ellUserService, String RsaPrivate) {

        //这里是demo，所以随便new一个。实际使用如果到了spring请使用下面的有参构z造函数
        this.planService = planService;
        this.planId = planId;
        this.ellUserService = ellUserService;
        this.RsaPrivate = RsaPrivate;
    }

    @Override
    public void invoke(BatchOptionalVo batchOptionalVo, AnalysisContext analysisContext) {
        //判断是否为身份证
//        if (IsIdCardUtil.isIDNumber(batchOptionalVo.getIdCard())) {
        //判断学籍号是否在表格中有重复
        //学籍号
        if (idCardSame.get(batchOptionalVo.getIdCard()) == null) {
            //添加到map中判断以后是否重复
            idCardSame.put(batchOptionalVo.getIdCard(), batchOptionalVo.getIdCard());
            if (batchOptionalVo.getProId() != null && batchOptionalVo.getProId() != "") {
                //生成身份证标识
//                    String identificationString = IdCardUtil.identificationString(batchOptionalVo.getIdCard());
                //查询出相同标识的人
//                    List<String> userIds = ellUserService.findIdentification(identificationString);
                //通过身份证查询学生信息
                List<EllReturnScoreVo> ellReturnScoreVos = ellUserService.findUserBy(batchOptionalVo.getIdCard(), 0);
                if (ellReturnScoreVos.size() == 1) {
                    for (int i = 0; i < ellReturnScoreVos.size(); i++) {
                        //判断身份证解密是否相同
//                        String idCardMys = ellUserService.findIdCardById(ellReturnScoreVos.get(i).);
//                        //对比数据库的身份证号和表格中的身份证是否相同
//                        if (idCardMys != null && idCardMys != "" && RSAUtils.decode(idCardMys, RsaPrivate).equals(batchOptionalVo.getIdCard())) {
//                            isAddList(userIds, i, batchOptionalVo);
//                            break;
//                        }
                        isAddList(ellReturnScoreVos, i, batchOptionalVo);
                    }
                }
                //标识没有这个学籍号
                else if (ellReturnScoreVos.size() == 0) {
                    batchOptionalVo.setIdCard(batchOptionalVo.getIdCard() + "--没有这个用户请检查学籍号");
                    listMistake.add(batchOptionalVo);
                }
                //代表标识没有相同的人
                else if (ellReturnScoreVos.size() == 1) {
                    isAddList(ellReturnScoreVos, 0, batchOptionalVo);
                }
            } else {
                batchOptionalVo.setProId(batchOptionalVo.getProId() + "---没有输入项目标识！");
                listMistake.add(batchOptionalVo);
            }
        } else {
            batchOptionalVo.setProId(batchOptionalVo.getProId() + "--学籍号在表格中重复,在表格中已存在");
            listMistake.add(batchOptionalVo);
        }
    }
//        else {
//            batchOptionalVo.setIdCard(batchOptionalVo.getIdCard() + "--学籍号错误");
//            listMistake.add(batchOptionalVo);
//        }

//}

    //判断是否进行添加
    public void isAddList(List<EllReturnScoreVo> userIds, int i, BatchOptionalVo batchOptionalVo) {
        List<EllReturnScoreVo> ellReturnScoreVo = ellUserService.findUserById(userIds.get(i).getId());
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
            List<FindOptionalProjectVo> optionalProjectVoList = planService.findOptionalProject(planId, String.valueOf(ellReturnScoreVo.get(0).getUserSex()), Integer.parseInt(porId[j]));
            //代表可选项中有这个项目
            if (optionalProjectVoList.size() > 0) {
                //判断自选项已经足够
                int selectedNum = planService.findSelectedNum(planId, userIds.get(i).getId());
                //判断已选项目选择项目数是否小于等于可选择数量
                if ((selectedNum + porId.length) <= optionalProjectVoList.get(0).getCustomizeTheNumber()) {
                    //判断是否已经选择改项目
                    List<EllFindSelectedPro> ellFindSelectedPros = planService.findProjectOptional(planId, userIds.get(i).getId(), Integer.parseInt(porId[j]));
                    if (ellFindSelectedPros.size() > 0) {
                        batchOptionalVo.setProId(porId[j] + "---项目已经选择请更换项目！");
                        listMistake.add(batchOptionalVo);
                        continue;
                    } else {
                        BatchOptionalVo batchOptional = new BatchOptionalVo();
                        batchOptional.setUserId(userIds.get(i).getId());
                        batchOptional.setProId(porId[j]);
                        addList.add(batchOptional);
                        continue;
                    }
                } else {
                    batchOptionalVo.setProId(porId[j] + "---选择项目已经多于自选数量请不要多选！");
                    listMistake.add(batchOptionalVo);
                    continue;
                }
            } else {
                batchOptionalVo.setProId(porId[j] + "---请输入正确的项目标识或者此用户不能选择该项目");
                listMistake.add(batchOptionalVo);
                continue;
            }
        }
    }

    @Transactional
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (addList.size() > 0) {
            planService.addOptionalProject(addList, planId);
        }
        if (listMistake.size() > 0) {
            String fileName = IdUtils.randomUUID() + ".xlsx";
            String BATCH_OPTIONAL_PROJECT_PATH_ERROR = "";
            if (FileUploadUtil.isLinux() == false) {
                fileUri = WindowsSite.IMG_NGINX_PATH + fileName;
                BATCH_OPTIONAL_PROJECT_PATH_ERROR = WindowsSite.BATCH_OPTIONAL_PROJECT_PATH_ERROR;
            } else {
                fileUri = LinuxSite.IMG_NGINX_PATH + fileName;
                BATCH_OPTIONAL_PROJECT_PATH_ERROR = LinuxSite.BATCH_OPTIONAL_PROJECT_PATH_ERROR;
            }
            //判断是否有文件路径
            PathGeneration.createPath(BATCH_OPTIONAL_PROJECT_PATH_ERROR + fileName);
            //PathGeneration.createPath(fileUri);
            EasyExcel.write(BATCH_OPTIONAL_PROJECT_PATH_ERROR + fileName, BatchOptionalVo.class).sheet("模板").doWrite(listMistake);
        }
    }
}
