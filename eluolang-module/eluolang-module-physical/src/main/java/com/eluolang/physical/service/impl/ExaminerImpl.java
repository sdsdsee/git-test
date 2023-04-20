package com.eluolang.physical.service.impl;

import cn.hutool.core.codec.Base64;
import com.eluolang.common.core.constant.Constants;
import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.util.GroupingUtil;
import com.eluolang.common.core.util.IdUtils;
import com.eluolang.physical.dto.EllExaminerDto;
import com.eluolang.physical.dto.GroupingDto;
import com.eluolang.physical.mapper.EllUserMapper;
import com.eluolang.physical.mapper.ExaminerMapper;
import com.eluolang.physical.model.*;
import com.eluolang.physical.service.EllUserService;
import com.eluolang.physical.service.ExaminerService;
import com.eluolang.physical.util.CreateTime;
import com.eluolang.physical.util.FaceUtil;
import com.eluolang.physical.util.IdCardUtil;
import com.eluolang.physical.util.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

@Service
@Transactional
public class ExaminerImpl implements ExaminerService {
    @Autowired
    private ExaminerMapper examinerMapper;

    @Override
    public EllExaminer selExaminerByLoginCode(String code) {
        return examinerMapper.selExaminerByLoginCode(code);
    }

    @Override
    public EllDevice examinerLoginDevice(String deviceId,Integer deptId) {
        return examinerMapper.examinerLoginDevice(deviceId, deptId);
    }

    @Override
    public int insertExaminer(EllExaminer ellExaminer) {
        return examinerMapper.insertExaminer(ellExaminer);
    }

    @Override
    public int deleteExaminer(String id) {
        return examinerMapper.deleteExaminer(id);
    }

    @Override
    public int updateExaminer(EllExaminer ellExaminer) {
        return examinerMapper.updateExaminer(ellExaminer);
    }

    @Override
    public List<EllExaminer> selectExaminer(EllExaminerDto ellExaminerDto) {
        return examinerMapper.selectExaminer(ellExaminerDto);
    }
}
