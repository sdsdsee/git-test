package com.eluolang.module.report.servier.impl;

import com.eluolang.common.core.pojo.EllUser;
import com.eluolang.module.report.mapper.ArchivesMapper;
import com.eluolang.module.report.servier.ArchivesService;
import com.eluolang.module.report.util.CreateTime;
import com.eluolang.module.report.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ArchivesServiceImpl implements ArchivesService {
    @Autowired
    private ArchivesMapper archivesMapper;

    @Override
    public List<EllSelYearPersonNumVo> selYearTestPersonNum(int orgId, int accountId, int yearNum, int isExam) {
        List<EllSelYearPersonNumVo> ellSelYearPersonNumVoList = new ArrayList<>();
        //不获取本年
        for (int i = yearNum; i > 0; i--) {
            ellSelYearPersonNumVoList.add(archivesMapper.selYearTestPersonNum(orgId, accountId, i, isExam));
        }
        return ellSelYearPersonNumVoList;
    }

    @Override
    public EllYearScoreGradeVo selYearNum(int orgId, int accountId, String year, int isExam) {
        EllYearScoreGradeVo scoreGradeVo = new EllYearScoreGradeVo();
        scoreGradeVo.setExcellent(archivesMapper.selYearNum(orgId, accountId, year, 90, 100, isExam));
        scoreGradeVo.setGood(archivesMapper.selYearNum(orgId, accountId, year, 80, 90, isExam));
        scoreGradeVo.setPass(archivesMapper.selYearNum(orgId, accountId, year, 60, 80, isExam));
        scoreGradeVo.setNoPass(archivesMapper.selYearNum(orgId, accountId, year, 0, 60, isExam));
        return scoreGradeVo;
    }

    @Override
    public List<EllAFewYearGradeNumVo> selAFewYearNum(int orgId, int accountId, int isExam) {
        List<String> yearStr = CreateTime.yearCalendar(8);
        List<EllAFewYearGradeNumVo> ellAFewYearGradeNumVos = new ArrayList<>();
        //不查看本年
        for (int i = 1; i < yearStr.size(); i++) {
            EllAFewYearGradeNumVo ellAFewYearGradeNumVo = new EllAFewYearGradeNumVo();
            ellAFewYearGradeNumVo.setYear(yearStr.get(i));
            ellAFewYearGradeNumVo.setExcellent(archivesMapper.selYearNum(orgId, accountId, yearStr.get(i), 90, 100, isExam));
            ellAFewYearGradeNumVo.setGood(archivesMapper.selYearNum(orgId, accountId, yearStr.get(i), 80, 90, isExam));
            ellAFewYearGradeNumVo.setPass(archivesMapper.selYearNum(orgId, accountId, yearStr.get(i), 60, 80, isExam));
            ellAFewYearGradeNumVo.setNoPass(archivesMapper.selYearNum(orgId, accountId, yearStr.get(i), 0, 60, isExam));
            ellAFewYearGradeNumVos.add(ellAFewYearGradeNumVo);
        }
        return ellAFewYearGradeNumVos;
    }

    @Override
    public List<EllUser> findUser(int orgId) {
        return archivesMapper.findUser(orgId);
    }

    @Override
    public List<EllYearAvgScoreVo> selYearAvgScore(int orgId, int accountId, int dayNum, int isExam) {
        List<EllYearAvgScoreVo> ellYearAvgScoreVoList = new ArrayList<>();
        for (int i = dayNum; i > 0; i--) {
            ellYearAvgScoreVoList.add(archivesMapper.selYearAvgScore(orgId, accountId, i, isExam));
        }
        return ellYearAvgScoreVoList;
    }

    @Override
    public List<EllUserYearProAvgVo> selUserYearData(String userId, int isExam) {
        List<EllUserYearProAvgVo> ellUserYearProAvgVoList = new ArrayList<>();
        String[][] proNameAndId = {{"身高体重", "35"}, {"肺活量", "36"}, {"50米跑", "56"}, {"体前屈", "38"}, {"跳绳", "44"}, {"立定跳远", "37"}};
        for (int i = 0; i < proNameAndId.length; i++) {
            EllUserYearProAvgVo ellUserYearProAvgVo = new EllUserYearProAvgVo();
            ellUserYearProAvgVo.setName(proNameAndId[i][0]);
            List<EllUserYearProDateVo> ellUserYearProDateVoList = new ArrayList<>();
            for (int j = 5; j > 0; j--) {
                EllUserYearProDateVo ellUserYearProDateVo = archivesMapper.selUserYearData(userId, Integer.parseInt(proNameAndId[i][1]), isExam, j);
                ellUserYearProDateVoList.add(ellUserYearProDateVo);
            }
            ellUserYearProAvgVo.setDateList(ellUserYearProDateVoList);
            ellUserYearProAvgVoList.add(ellUserYearProAvgVo);
        }
        return ellUserYearProAvgVoList;
    }

    @Override
    public List<EllYearAvgScoreVo> selUserYearAvg(String userId) {
        List<EllYearAvgScoreVo> ellYearAvgScoreVoList = new ArrayList<>();
        for (int i = 5; i > 0; i--) {
            ellYearAvgScoreVoList.add(archivesMapper.selUserYearAvg(userId, i));
        }

        return ellYearAvgScoreVoList;
    }

    @Override
    public List<EllHeightAndWeightVo> selUserHeightAndWeight(String userId) {
        List<EllHeightAndWeightVo> ellHeightAndWeightVoList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for (int i = 5; i > 0; i--) {
            EllHeightAndWeightVo ellHeightAndWeightVo = archivesMapper.selUserHeightAndWeight(userId, i);
            if (ellHeightAndWeightVo == null) {
                cal.setTime(new Date());
                ellHeightAndWeightVo = new EllHeightAndWeightVo();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
                cal.add(Calendar.YEAR, -i);
                ellHeightAndWeightVo.setYear(simpleDateFormat.format(cal.getTime()));
                ellHeightAndWeightVo.setHeight(0.0);
                ellHeightAndWeightVo.setWeight(0.0);
            }
            ellHeightAndWeightVoList.add(ellHeightAndWeightVo);
        }
        return ellHeightAndWeightVoList;
    }
}
