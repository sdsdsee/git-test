package com.eluolang.physical.Listener;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.pojo.ELLSymmetry;
import com.eluolang.common.core.pojo.EllUser;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.physical.model.EllIdentification;
import com.eluolang.physical.model.EllUserVo;
import com.eluolang.physical.service.EllUserService;
import com.eluolang.physical.util.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.ArrayUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

@Data
@Slf4j
@Transactional
public class ImportUserListener extends AnalysisEventListener<EllUserVo> {
    private EllUserService ellUserService;
    private List<EllUser> list = new ArrayList<>();
    private String[] orgIds;
    private String orgId;
    //非对称的加密的标识创建
    List<EllIdentification> identificationList = new ArrayList<>();
    //对称加密秘钥
    private static String DesKey = "3de902b37c15e6d3";
    //对称加密
    private List<ELLSymmetry> ellSymmetryList = new ArrayList<>();
    //学籍号
    private Map<String, String> studentCodeMap = new HashMap<>();
    //加密公钥
    private static String RsaPublic = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCHdsWMm1lNNtIJeulKRd5yHiAHz0hWczSNwaT2BJQRxY892JwT7w9IShg1TsNDr0B7jotSItTk4+x9afP0Qi1M6loywYmWINw458M2vEAnoOr/bAd1EE74GXiA0i+68h/JIyrKaDXMT9yEDzibvdjpifPcR2tlo0Gh6u1lSbAcyQIDAQAB";
    //解密私钥
    private static String RsaPrivate = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAId2xYybWU020gl66UpF3nIeIAfPSFZzNI3BpPYElBHFjz3YnBPvD0hKGDVOw0OvQHuOi1Ii1OTj7H1p8/RCLUzqWjLBiZYg3Djnwza8QCeg6v9sB3UQTvgZeIDSL7ryH8kjKspoNcxP3IQPOJu92OmJ89xHa2WjQaHq7WVJsBzJAgMBAAECgYAOMJnW9brwBKsRxSdE2XbIu3EdgFAShjTeGIfAIZClH+22S8dlqygNqX1JsDtyDpyRlMfxTabBZ0KxjHS6hGgcPHg3dsmSl2rMTCZ+g3x+g1CTTEFvU2poghJgvi3MGXMsD/4OqAhctkems6Py4lLopLh4LIruWxjVQT916HGNEQJBALvoOVxDpeaqBfX+rmcId751CCV7lGOM6CQ3RCcKKOVJQrg2vdWaPQQhMtxOdHuxYFEmlCs7bkMwb5gZAoKkUu0CQQC4jX9Eyq3GnjQHGw8tKsjW0qXDva0bhWSzoKupctOF8p+CGzej1dEzoOD7B3G3s7/6cqHUsoZFlXQGzgDbIOnNAkAXxN5Mn3oC3Yr3fQnOmrGa8+7d3u38UbyjqCIE1jIqHsRDzRjiRipltVVbrMjrRJqsaTbg6Rmfgib7lF5O7D5ZAkBto3IWVipCsr3J8uNjO+Ww6decn9R1YxveMveIZTUQgIEOdEjrxhzQhSTjPRrrvZexm+RUTfLC5/TUDhIoHDeJAkAum6U4iZ1Odsad3wvndI+D3pYou3LEteKzgsOdisqjFC5kwr9x3u/Hrj2NunBmF6rlcZ+WNwNoBnKJzSHQB4kb";
    //导入信息不对的人员
    private List<EllUserVo> listMistake = new ArrayList<>();
    //文件名称
    private String fileName = "";
    //文件地址
    private String fileUri = "";
    private static String ID_CARD_ERROR = "(身份证错误!)";
    private static String ID_CARD_SAME = "(该身份证用户已存在!)";
    private static String IDENTIFICATION_SAME = "(学号或编号重复)";
    private static String ORG_ID_SAME = "(部门ID不存在或无权限!)";
    private static String Birth = "birth";
    private static String Sex = "sex";

    public ImportUserListener(EllUserService ellUserService, String orgId) {
        //这里是demo，所以随便new一个。实际使用如果到了spring请使用下面的有参构z造函数
        this.ellUserService = ellUserService;
        this.orgId = orgId;
    }

    @Override
    public void invoke(EllUserVo ellUserVo, AnalysisContext analysisContext) {
        //判断导入的参数是否合法
        if (isParameters(ellUserVo) && isSame(ellUserVo)) {
            //名族
            //中文情况下
            //ellUserVo.setEthnic(ellUserService.findByNameEthnic(ellUserVo.getEthnic()).getId());
            //标识
            //数据添加身份证唯一标识
            //    ellUserMapper.addIdentification(ellUser.getId(), identification, ellUserVo.getOrgId());
            //ellUserVo.setEthnic(ellUserVo.getEthnic());
            EllUser ellUser = new EllUser();
            ellUser.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
            ellUser.setUserName(ellUserVo.getUserName());
            ellUser.setCreateTime(CreateTime.getTime());
            ellUser.setOrgId(ellUserVo.getOrgId());
            ellUser.setEnTime(ellUserVo.getEnTime());
            ellUser.setPhone(ellUserVo.getPhone());
            ellUser.setStudentId(ellUserVo.getStudentId());
            ellUser.setHomeAddress(ellUserVo.getHomeAddress());
            ellUser.setCreateBy(ellUserVo.getCreateBy());
            ellUser.setEthnic(ellUserVo.getEthnic());
            ellUser.setEnGrade(ellUserVo.getEnGrade());
            ellUser.setStudentCode(ellUserVo.getStudentCode());
            ellUser.setUserSex(ellUserVo.getSex());
            //判断为空可去，去掉就必须要有
            if (ellUser.getIdCard() != null && !ellUser.getIdCard().equals(null)) {
                ellUser.setIdCard(RSAUtils.encode(ellUserVo.getIdCard(), RsaPublic));
                Map<String, String> idCardInfo = IdCardUtil.getIdCardInfo(ellUserVo.getIdCard());
                ellUser.setUserBirth(idCardInfo.get(Birth).toString());
                ELLSymmetry ellSymmetry = new ELLSymmetry();
                ellUser.setUserSex(Integer.parseInt(idCardInfo.get(Sex)));
                EllIdentification ellIdentification = new EllIdentification();
                try {
                    //身份证对称加密
                    ellSymmetry.setIdCardSymmetry(DESUtil.encrypt(ellUserVo.getIdCard(), DesKey.getBytes()));
                    ellSymmetry.setUserId(ellUser.getId());
                    //身份证非对称加密标识创建
                    ellIdentification.setUserId(ellUser.getId());
                    ellIdentification.setOrgId(ellUserVo.getOrgId());
                    ellIdentification.setIdentification(IdCardUtil.identificationString(ellUserVo.getIdCard()));
                    //进行对称加密封装
                    ellSymmetryList.add(ellSymmetry);
                    //非对称加密标识封装
                    identificationList.add(ellIdentification);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            list.add(ellUser);
            studentCodeMap.put(ellUser.getStudentCode(), "存在");
        }

    }

    @Transactional
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        try {
            if (list.size() > 0) {
                int code = ellUserService.importUser(list, ellSymmetryList, identificationList);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //生成错误信息用户的表格
        if (listMistake.size() > 0) {
            fileName = UUID.randomUUID() + ".xlsx";
            StringBuffer FILE_PATH = new StringBuffer();
            if (FileUploadUtil.isLinux() == false) {
                fileUri = WindowsSite.IMG_NGINX_PATH_Excel + fileName;
                FILE_PATH.append(WindowsSite.FILE_PATH);
            } else {
                fileUri = LinuxSite.IMG_NGINX_PATH_Excel + fileName;
                FILE_PATH.append(LinuxSite.filePath);
            }
            //判断是否有文件路径
            PathGeneration.createPath(FILE_PATH.toString() + fileName);
            PathGeneration.createPath(fileUri);
            EasyExcel.write(FILE_PATH.toString() + fileName, EllUserVo.class).sheet("模板").doWrite(listMistake);
            FILE_PATH.setLength(0);
        }
    }

    /**
     * 判断导入的参数是否合法
     *
     * @param ellUserVo
     * @return
     */
    public Boolean isParameters(EllUserVo ellUserVo) {
        //合法返回true
        if (ellUserVo != null) {
            if (ellUserVo.getUserName() != null && ellUserVo.getStudentId() != null && ellUserVo.getStudentCode() != null && ellUserVo.getHomeAddress() != null && ellUserVo.getEnTime() != null && ellUserVo.getEnGrade() != null && ellUserVo.getOrgId() != null) {
                return true;
            }
        }
        listMistake.add(ellUserVo);
        return false;
    }

    /**
     * 判断导入的身份证相同或者同部门有相同的编号或学号或是否有这个部门
     *
     * @param ellUserVo
     * @return
     */
    public boolean isSame(EllUserVo ellUserVo) {
        //判断为空可去，去掉就必须要有
        if (ellUserVo.getIdCard() != null && !ellUserVo.getIdCard().equals(null)) {
            if (!IsIdCardUtil.isIDNumber(ellUserVo.getIdCard())) {
                ellUserVo.setIdCard(ellUserVo.getIdCard() + (ID_CARD_ERROR));
                listMistake.add(ellUserVo);
                return false;
            }
            //获取是否有相同的身份标识,判断身份证重复
            List<String> stringList = ellUserService.findIdentification(IdCardUtil.identificationString(ellUserVo.getIdCard()));
            for (int i = 0; i < stringList.size(); i++) {
                //数据库的加密身份证
                String idCardMys = ellUserService.findIdCardById(stringList.get(i));
                //校验数据库数据完整性，避免出现直接删除数据库没删完
                if (idCardMys != null && idCardMys != "") {
                    String idCard = RSAUtils.decode(idCardMys, RsaPrivate);
                    if (idCard.equals(ellUserVo.getIdCard())) {
                        ellUserVo.setIdCard(ellUserVo.getIdCard() + ID_CARD_SAME);
                        listMistake.add(ellUserVo);
                        return false;
                    } /*else {
                    //判断同一部门重复的编号
                    List<EllUser> ellUsers = ellUserService.findUsers(null, null, ellUserVo.getStudentId(), ellUserVo.getOrgId(), null, null);
                    if (ellUsers.size() > 0) {
                        ellUserVo.setOrgId(ellUserVo.getOrgId() + IDENTIFICATION_SAME);
                        listMistake.add(ellUserVo);
                        return false;
                    }
                }*/
                }
            }
        }
        //判断是否有这个部门
        orgIds = orgId.split(",");
        Boolean a = ArrayUtils.contains(orgIds, ellUserVo.getOrgId());
        if (!a) {
            ellUserVo.setOrgId(ellUserVo.getOrgId() + ORG_ID_SAME);
            listMistake.add(ellUserVo);
            return false;
        }
        //判断学籍号或者军籍号唯一
        //本表格中
        if (studentCodeMap.containsKey(ellUserVo.getStudentCode())) {
            ellUserVo.setStudentCode(ellUserVo.getStudentCode() + IDENTIFICATION_SAME);
            listMistake.add(ellUserVo);
            return false;
        }
        //数据库中
        int studentCodeCount = ellUserService.findStudentCode(ellUserVo.getStudentCode());
        if (studentCodeCount > 0) {
            ellUserVo.setStudentCode(ellUserVo.getStudentCode() + IDENTIFICATION_SAME);
            listMistake.add(ellUserVo);
            return false;
        }
        //

        //判断同一部门重复的编号
        List<EllUser> ellUsers = ellUserService.findUsers(null, null, ellUserVo.getStudentId(), null, ellUserVo.getOrgId(), null);
        if (ellUsers.size() > 0) {
            ellUserVo.setOrgId(ellUserVo.getOrgId() + IDENTIFICATION_SAME);
            listMistake.add(ellUserVo);
            return false;
        }
        return true;
/*        switch (code) {
            case HttpStatus.ID_CARD_ERROR:
                ellUserVo.setIdCard(ellUserVo.getIdCard() + (ID_CARD_ERROR));
                listMistake.add(ellUserVo);
                break;
            case HttpStatus.ID_CARD_EXIST:
                ellUserVo.setIdCard(ellUserVo.getIdCard() + ID_CARD_SAME);
                listMistake.add(ellUserVo);
                break;
            case HttpStatus.STUDY_NUMBER_EXIST:
                ellUserVo.setStudentId(ellUserVo.getStudentId() + IDENTIFICATION_SAME);
                listMistake.add(ellUserVo);
                break;
            case HttpStatus.DEPT_ID_NOT_EXIST:
                ellUserVo.setOrgId(ellUserVo.getOrgId() + ORG_ID_SAME);
                listMistake.add(ellUserVo);
                break;
        }*/
    }
}
