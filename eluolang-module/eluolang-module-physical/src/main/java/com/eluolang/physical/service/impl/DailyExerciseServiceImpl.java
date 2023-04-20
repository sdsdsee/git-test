package com.eluolang.physical.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.arcsoft.face.*;
import com.arcsoft.face.enums.ImageFormat;
import com.arcsoft.face.toolkit.ImageInfo;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.physical.dto.DailyPlanUseDepartDto;
import com.eluolang.physical.dto.EllDailyRunRankDto;
import com.eluolang.physical.dto.EllDailyTimeNumDto;
import com.eluolang.physical.dto.UserMonthDailyDto;
import com.eluolang.physical.mapper.DailyExerciseMapper;
import com.eluolang.physical.model.EllDailyExercisePlanVo;
import com.eluolang.physical.model.EllDailyUserVo;
import com.eluolang.physical.model.EllSelDailyDetailsVo;
import com.eluolang.physical.model.UpdateUserVo;
import com.eluolang.physical.redis.AvoidRedis;
import com.eluolang.physical.service.DailyExerciseService;
import com.eluolang.physical.util.CreateTime;
import com.eluolang.physical.util.PathGeneration;
import com.eluolang.physical.util.PicUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

@Service("dailyExerciseService")
public class DailyExerciseServiceImpl implements DailyExerciseService {
    @Autowired
    private DailyExerciseMapper dailyExerciseMapper;
    @Autowired
    private AvoidRedis avoidRedis;

    @Override
    public int addDailyPlan(EllDailyExercisePlanVo ellDailyExercisePlan) {
        return dailyExerciseMapper.addDailyPlan(ellDailyExercisePlan);
    }

    @Override
    public int addDailyTime(List<EllDailyTimeQuantum> ellDailyTimeQuantum, String dailyId) {
        return dailyExerciseMapper.addDailyTime(ellDailyTimeQuantum, dailyId);
    }

    @Override
    public List<EllDailyExercisePlan> selDailPlan(EllDailyExercisePlanVo ellDailyExercisePlan) {
        return dailyExerciseMapper.selDailPlan(ellDailyExercisePlan);
    }

    @Override
    public List<EllDailyExercisePlanVo> selDailyExercisePlanByName(String orgId, String account) {
        return dailyExerciseMapper.selDailyExercisePlanByName(orgId, account);
    }

    @Override
    public String selDailyTextContent(String dailyId) {
        return dailyExerciseMapper.selDailyTextContent(dailyId);
    }

    @Override
    public String selDailyTextContentDeviceId(String deviceId) {
        return dailyExerciseMapper.selDailyTextContentDeviceId(deviceId);
    }

    @Override
    public List<EllDailyExercisePlanVo> selDailyExercisePlan(String orgId, String account) {
        return dailyExerciseMapper.selDailyExercisePlanByName(orgId, account);
    }

    @Override
    public List<DailyPlanUseDepartDto> selPlanAllDepart(String orgId, String account) {
        return dailyExerciseMapper.selPlanAllDepart(orgId, account);
    }

    @Override
    public List<EllDailyPlanUseDepart> selPlanUseDp(String dailyId, String orgId) {
        return dailyExerciseMapper.selPlanUseDp(dailyId, orgId);
    }

    @Override
    public int addPlanUseDp(List<DailyPlanUseDepartDto> pfDepartList, String dailyId) {
        return dailyExerciseMapper.addPlanUseDp(pfDepartList, dailyId);
    }

    @Override
    public int delPlanTime(String dailyId) {
        return dailyExerciseMapper.delPlanTime(dailyId);
    }

    @Override
    public int delPlanUseDp(String dailyId) {
        return dailyExerciseMapper.delPlanUseDp(dailyId);
    }

    @Override
    public int updataDailyPlan(EllDailyExercisePlanVo ellDailyExercisePlan) {

        return dailyExerciseMapper.updataDailyPlan(ellDailyExercisePlan);
    }

    @Override
    public List<EllDailyTimeQuantum> selUseTime(String dailyId, int timeType) {
        return dailyExerciseMapper.selUseTime(dailyId, timeType);
    }

    @Override
    public int selDailyNum(EllSelDailyDetailsVo ellSelDailyDetailsVo, String sex) {
        return dailyExerciseMapper.selDailyNum(ellSelDailyDetailsVo, sex);
    }

    @Override
    public List<EllDailyRunRankDto> selRankHistory(EllSelDailyDetailsVo ellSelDailyDetailsVo) {
        return dailyExerciseMapper.selRankHistory(ellSelDailyDetailsVo);
    }

    @Override
    public List<EllDailyRunRankDto> selNotQualifiedHistoryRank(EllSelDailyDetailsVo ellSelDailyDetailsVo, int type, long startTime, long endTime) {
        return dailyExerciseMapper.selNotQualifiedHistoryRank(ellSelDailyDetailsVo, type, startTime, endTime);
    }

    @Override
    public int selTimeNum(String startTime, String endTime, String dailyId, String account, String orgId) {
        return dailyExerciseMapper.selTimeNum(startTime, endTime, dailyId, account, orgId);
    }

    @Override
    public int selMonthNum(String account, String orgId, String dailyId, Long monthStart, Long monthEnd, int type, String sex) {
        return dailyExerciseMapper.selMonthNum(account, orgId, dailyId, monthStart, monthEnd, type, sex);
    }

    @Override
    public int selUserTime(String userId, String dailyId, Long timeStart, Long timeEnd, int type) {
        return dailyExerciseMapper.selUserTime(userId, dailyId, timeStart, timeEnd, type);
    }

    @Override
    public List<EllDailyExercisePlanVo> selDailyById(String dailyId) {
        return dailyExerciseMapper.selDailyById(dailyId);
    }

    @Override
    public List<UserMonthDailyDto> getUserMonthDaily(EllDailyUserVo ellDailyUserVo) {
        List<UserMonthDailyDto> userMonthDailyDtoList = new ArrayList<>();
        //��ѯ�ճ�������
        List<EllDailyExercisePlanVo> ellDailyExercisePlanList = selDailyById(ellDailyUserVo.getDailyId());      //���°����ճ���ʱ���
        String[] firstMoths = ellDailyExercisePlanList.get(0).getFirstHalfYear().split("-");
        String[] twoMoths = ellDailyExercisePlanList.get(0).getSecondHalfYear().split("-");
        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        String year = CreateTime.getYear();
        //��ȡ�·ݵĴ������
        for (int i = 0; i < months.length; i++) {
            int Type = 0;
            //����������
            Double monthTask = 1.0;
            if (Integer.parseInt(months[i]) >= Integer.parseInt(firstMoths[0]) && Integer.parseInt(months[i]) <= Integer.parseInt(firstMoths[1])) {
                monthTask = ellDailyExercisePlanList.get(0).getFirstMonth();
                Type = 1;
            } else if (Integer.parseInt(months[i]) >= Integer.parseInt(twoMoths[0]) && Integer.parseInt(months[i]) <= Integer.parseInt(twoMoths[1])) {
                Type = 2;
                monthTask = ellDailyExercisePlanList.get(0).getSecondMonth();
            }
            Long monthStart = CreateTime.monthTransitionTimestamp(year + "-" + months[i]);
            Long monthEnd = null;
            if (i == months.length - 1) {
                //���һ���¾ͼ�һ��
                monthEnd = CreateTime.monthTransitionTimestamp((Integer.parseInt(year) + 1) + "-" + months[0]);
            } else {
                monthEnd = CreateTime.monthTransitionTimestamp(year + "-" + months[i + 1]);
            }

            UserMonthDailyDto userMonthDailyDto = new UserMonthDailyDto();
            userMonthDailyDto.setMileage(selUserTime(ellDailyUserVo.getUserId(), ellDailyUserVo.getDailyId(), monthStart, monthEnd, Type));
            userMonthDailyDto.setMileage(userMonthDailyDto.getMileage() >= monthTask ? 1 : 2);
            userMonthDailyDtoList.add(userMonthDailyDto);
        }
        return userMonthDailyDtoList;
    }

    @Override
    public String finUserId(String image, String deviceId) {
        //��ѯ�豸�󶨵Ĳ���
        String orgId = dailyExerciseMapper.selOrgIdByDeviceId(deviceId);
        //��ѯ������Ϣ
        List<ImgUserRelevance> imgUserRelevances = avoidRedis.getFaceInfo(orgId);
        if (imgUserRelevances == null || imgUserRelevances.size() <= 0) {
            imgUserRelevances = dailyExerciseMapper.selUserRelevance(deviceId);
            avoidRedis.saveFaceInfo(orgId, imgUserRelevances);
        }
        if (image != null) {
            try {
                /* * 1��ȡ��FaceHelper
                FaceHelper faceHelper = FaceHelper.getInstance(haarPath, modelPath);
                 * 2����ȡͷ�񣬲��õ�������*/
                //�ж�ͼƬ�Ļ���
                FaceEngine faceEngine = SpringUtil.getBean(FaceEngine.class);
                //base64תΪbyte[]
                BASE64Decoder d = new BASE64Decoder();
                byte[] imageData = d.decodeBuffer(image);
                ImageInfo imageInfo = getRGBData(imageData);
                //��ȡ������Ϣ
                List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
                int detectCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
                //������ȡ
                FaceFeature faceFeature = new FaceFeature();
                int extractCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);
                //ͨ������ѻ�ȡ������Ϣ����
                byte[] faceByte = faceFeature.getFeatureData();
                //�����������Ĺ���
                FunctionConfiguration configuration = new FunctionConfiguration();
                configuration.setSupportLiveness(true);
                //�������������Ĺ��ܲ���������
                int processCode = faceEngine.process(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList, configuration);
                //������
                List<LivenessInfo> livenessInfoList = new ArrayList<LivenessInfo>();
                int livenessCode = faceEngine.getLiveness(livenessInfoList);
                //�洢�������ֵ
                double maxFeature = 0;
                FaceFeature sourceFaceFeature = new FaceFeature();
                //�����ȶ�
                FaceFeature targetFaceFeature = new FaceFeature();

                StringBuffer stringUserId = new StringBuffer();
                FaceSimilar faceSimilar = new FaceSimilar();
                for (int i = 0; i < imgUserRelevances.size(); i++) {
                    //��ѯ������Ϣ
//                    ImgUserRelevance imgUser=dailyExerciseMapper.selUserRelevanceById(imgUserRelevances.get(i).getUserId());
                    //���ݿ���������ֵ
                    byte[] faceByteMy = d.decodeBuffer(imgUserRelevances.get(i).getFaceInfo());
                    //���Ƚϵ�����ֵ
                    targetFaceFeature.setFeatureData(faceByte);
                    //���ݿ�洢������ֵ
                    sourceFaceFeature.setFeatureData(faceByteMy);
                    //���Ƚ�
                    int compareCode = faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
                    //faceSimilar.getScore()��ʶ�ȴ���0.7�Ľ��д洢
                    if (faceSimilar.getScore() > 0.5 && maxFeature < faceSimilar.getScore()) {
                        stringUserId.setLength(0);
                        maxFeature = faceSimilar.getScore();
                        stringUserId.append(imgUserRelevances.get(i).getUserId());
                    }
                }
                return stringUserId.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void delFaces() {
        //ɾ�����ڻ����е�������Ϣ
        List<String> keys = avoidRedis.getKeys("face:");
        avoidRedis.deleteKey(keys);
    }
}
