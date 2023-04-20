package com.eluolang.app.manage.service.impl;

import com.eluolang.app.manage.dto.EllUserVo;
import com.eluolang.app.manage.mapper.SkipAppMapper;
import com.eluolang.app.manage.service.SkipAppService;
import com.eluolang.app.manage.util.CreateTime;
import com.eluolang.app.manage.util.IdCardUtil;
import com.eluolang.app.manage.util.PathGenerationUtil;
import com.eluolang.app.manage.util.RSAUtils;
import com.eluolang.app.manage.vo.*;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.pojo.EllSkipDevice;
import com.eluolang.common.core.pojo.EllSkipJoinContest;
import com.eluolang.common.core.pojo.EllUser;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;


@Service
@Transactional
public class SkipAppServiceImpl implements SkipAppService {
    @Autowired
    private SkipAppMapper skipAppMapper;
    @Value("${RSA.public}")//公钥
    private String RsaPublic;
    @Value("${RSA.private}")//私钥
    private String RsaPrivate;

    @Override
    public EllUserVo findUser(String wxOpenId) {
        return skipAppMapper.findUser(wxOpenId);
    }

    @Override
    public List<String> findUserIdByIdentification(String identification) {
        return skipAppMapper.findUserIdByIdentification(identification);
    }

    @Override
    public EllUser findUserById(String userId) {
        return skipAppMapper.findUserById(userId);
    }

    @Override
    public EllUserVo findTourist(String skipUserId, int role) {
        return skipAppMapper.findTourist(skipUserId, role);
    }

    @Override
    public int addSkipUserWx(EllSkipUserVo ellSkipUserVo) {
        //查询微信是否绑定其他或者此角色已经被其他微信绑定
        int i = findUserRoleIsHas(ellSkipUserVo);
        if (i > 0) {
            return 0;
        }
        ellSkipUserVo.setCreateTime(CreateTime.getTime());
        //生成标识
        skipAppMapper.addIdentification(ellSkipUserVo.getId(), IdCardUtil.identificationString(ellSkipUserVo.getIdCard()));
        //加密保存
        ellSkipUserVo.setIdCard(RSAUtils.encode(ellSkipUserVo.getIdCard(), RsaPublic));
        return skipAppMapper.addSkipUserWx(ellSkipUserVo);
    }


    @Override
    public String findUserSchoolName(String orgId) {
        return skipAppMapper.findUserSchoolName(orgId);
    }

    @Override
    public int findUserRoleIsHas(EllSkipUserVo ellSkipUserVo) {
        return skipAppMapper.findUserRoleIsHas(ellSkipUserVo);
    }

    @Override
    public int addSkipNum(EllSkipNumberVo ellSkipNumberVo) {
        ellSkipNumberVo.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
        ellSkipNumberVo.setCreateTime(CreateTime.getTime());
        //计算平均数好计算排名
        ellSkipNumberVo.setSkipAvg((double) ellSkipNumberVo.getSkipNum() / ellSkipNumberVo.getSkipTime());
        return skipAppMapper.addSkipNum(ellSkipNumberVo);
    }

    @Override
    public int addSkipContestNum(EllSkipNumberVo ellSkipNumberVo, MultipartFile multipartFile) throws IOException {
        String videoId = new Date().getTime() + IdUtils.fastSimpleUUID();
        if (!multipartFile.isEmpty()) {
            String[] type = multipartFile.getContentType().toString().split("/");
            String fileName = ellSkipNumberVo.getSkipUserId() + "_" + ellSkipNumberVo.getContestId();
            String path = "";
            if (FileUploadUtil.isLinux() == false) {
                path = WindowsSite.video + fileName + "." + type[1];
            } else {
                path = LinuxSite.video + fileName + "." + type[1];
            }
            //判断是否有文件路径并生成路径
            PathGenerationUtil.createPath(path);
            File file = new File(path);
            //删除原文件
            file.delete();
            multipartFile.transferTo(file);//把文件写入目标文件地址
            String url = "/" + fileName + "." + type[1];
            skipAppMapper.uploadImage(fileName + "." + type[1], String.valueOf(multipartFile.getSize()), "2", url, CreateTime.getTime(), videoId);
        }
        ellSkipNumberVo.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
        ellSkipNumberVo.setCreateTime(CreateTime.getTime());
        //计算平均数好计算排名
        ellSkipNumberVo.setSkipAvg(((double) ellSkipNumberVo.getSkipNum() / (double) ellSkipNumberVo.getSkipTime()));
        if (ellSkipNumberVo.getSkipAvg() == null) {
            ellSkipNumberVo.setSkipAvg(0.0);
        }
        //int ranking = skipAppMapper.findRanking(ellSkipNumberVo.getContestId(), ellSkipNumberVo.getSkipAvg());
        //存放成绩
        int i = skipAppMapper.addSkipNum(ellSkipNumberVo);
        int conContent = -1;
        //判断模式
        switch (ellSkipNumberVo.getSkipPattern()) {
            case 2:
                conContent = skipAppMapper.findConContent(ellSkipNumberVo.getContestId(), ellSkipNumberVo.getSkipTime());
                break;
            case 3:
                conContent = skipAppMapper.findConContent(ellSkipNumberVo.getContestId(), ellSkipNumberVo.getSkipNum());
                break;
        }
        //判断是否满足成绩排名计算

        //修改对应成绩以及视频地址
        int j = skipAppMapper.updateGrade(ellSkipNumberVo.getSkipAvg(), CreateTime.getTime(), ellSkipNumberVo.getContestId(), ellSkipNumberVo.getId(), ellSkipNumberVo.getSkipUserId(), videoId);
        if (conContent == -1) {
            return -1;
        }
        if (i > 0 && j > 0) {
            //修改排名
            skipAppMapper.updateRanking(ellSkipNumberVo.getContestId(), ellSkipNumberVo.getSkipAvg(), ellSkipNumberVo.getSkipUserId());
            return i;
        }
        return 1;
    }

    @Override
    public List<EllSkipDayNum> findSkipDayNum(String skipUserId) {
        List<String> dayList = CreateTime.dateWeek(CreateTime.getTime());
        LinkedHashMap<String, Integer> dayHashMap = new LinkedHashMap<>();
        //查询每天的跳绳总数
        List<EllSkipDayNum> ellSkipDayNumList = skipAppMapper.findSkipDayNum(skipUserId, dayList);
        //这一周的每天的总数
        List<EllSkipDayNum> skipDayNumList = new ArrayList<>();
        //生成没有数据的日期信息
        for (int i = 0; i < dayList.size(); i++) {
            EllSkipDayNum ellSkipDayNum = new EllSkipDayNum();
            ellSkipDayNum.setCreateTime(dayList.get(i));
            ellSkipDayNum.setSumSkip(0);
            skipDayNumList.add(i, ellSkipDayNum);
            //保存下标
            dayHashMap.put(dayList.get(i), i);
        }
        //并修改查询到某天的总数
        if (ellSkipDayNumList.size() > 0) {
            ellSkipDayNumList.stream().forEach(ellSkipDayNum -> {
                        skipDayNumList.set(dayHashMap.get(ellSkipDayNum.getCreateTime()), ellSkipDayNum);
                    }
            );
        }
        return skipDayNumList;
    }

    @Override
    public List<EllSkipNumberVo> findSkipTimeQuantum(String skipUserId, int dayNum, int isWork) {
        return skipAppMapper.findSkipTimeQuantum(skipUserId, CreateTime.dateCalendar(dayNum), isWork);
    }

    @Override
    public List<EllSkipDevice> findDeviceCoding(String userId, String deviceCoding) {
        return skipAppMapper.findDeviceCoding(userId, deviceCoding);
    }

    @Override
    public int addUserSkipDevice(String id, String userId, String deviceCoding) {
        return skipAppMapper.addUserSkipDevice(id, userId, deviceCoding);
    }

    @Override
    public List<EllSkipContestVo> findEllSkipContest(EllSkipContestVo ellSkipContestVo) {
        List<EllSkipContestVo> ellSkipContestVoList = skipAppMapper.findEllSkipContest(ellSkipContestVo);
        ellSkipContestVoList.stream().forEach(skip -> {
            if (skip.getConState() == 3) {
                skip.setState(3);
            }
        });
        return ellSkipContestVoList;
    }

    @Override
    public int addContest(EllSkipJoinContest ellSkipJoinContest) throws Exception {
        //角色写死
        EllUserVo ellUserVo = findTourist(ellSkipJoinContest.getSkipUserId(), 1);
        if (ellUserVo != null) {
            String idCard = RSAUtils.decode(ellUserVo.getIdCard(), RsaPrivate);
            String birthDay = IdCardUtil.getBirth(idCard);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date da = sdf.parse(birthDay);
            ellSkipJoinContest.setAge(CreateTime.getAge(da));
            ellSkipJoinContest.setUserName(ellUserVo.getUserName());
            String conNumber = new Date().getTime() + (int) Math.random() + 9 + "";
            ellSkipJoinContest.setConNumber(conNumber);
            ellSkipJoinContest.setId(IdUtils.fastSimpleUUID() + new Date().getTime());
            ellSkipJoinContest.setCreateTime(CreateTime.getTime());
            ellSkipJoinContest.setState(1);
            return skipAppMapper.addContest(ellSkipJoinContest);
        }
        return 0;
    }


    @Override
    public EllSkipAllNumAndTime findSkipAllNumAndTime(String skipUserId) {
        EllSkipAllNumAndTime ellSkipAllNumAndTime = skipAppMapper.findSkipAllNumAndTime(skipUserId);
        if (ellSkipAllNumAndTime != null && !ellSkipAllNumAndTime.getSkipAllTime().equals("") && ellSkipAllNumAndTime.getSkipAllTime() != null) {
            //把秒换为分钟
            int minute = Integer.parseInt(ellSkipAllNumAndTime.getSkipAllTime()) / 60;
            int second = Integer.parseInt(ellSkipAllNumAndTime.getSkipAllTime()) % 60;
            String time = minute + "'" + second + "\"";
            ellSkipAllNumAndTime.setSkipAllTime(time);
        } else {
            if (ellSkipAllNumAndTime == null) {
                ellSkipAllNumAndTime = new EllSkipAllNumAndTime();
            }
            ellSkipAllNumAndTime.setSkipAllTime("0");
        }
        return ellSkipAllNumAndTime;
    }

    @Override
    public String findUserImgId(String userId) {
        return skipAppMapper.findUserImgId(userId);
    }

    @Override
    public List<EllSkipRankingVo> findSkipRanking(String conTestId, String userId) {
        return skipAppMapper.findSkipRanking(conTestId, userId);
    }

    @Override
    public List<Integer> findNum() {
        return skipAppMapper.findNum();
    }

    @Override
    public List<EllSkipHomeworkVo> findHomework(EllSkipHomeworkVo ellSkipHomeworkVo) {
        return skipAppMapper.findHomework(ellSkipHomeworkVo);
    }

}
