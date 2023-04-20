package com.eluolang.physical.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arcsoft.face.*;
import com.arcsoft.face.enums.ImageFormat;
import com.arcsoft.face.toolkit.ImageInfo;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.ChartMultiSeriesRenderData;
import com.deepoove.poi.data.Charts;
import com.deepoove.poi.data.Texts;
import com.deepoove.poi.util.PoitlIOUtils;
import com.eluolang.common.core.constant.Constants;
import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.pojo.*;
import com.eluolang.common.core.util.*;
import com.eluolang.common.core.web.Result;
import com.eluolang.physical.dto.EllExamineeDto;
import com.eluolang.physical.dto.EllSexNumDto;
import com.eluolang.physical.dto.GroupingDto;
import com.eluolang.physical.feign.MegviiServerFeign;
import com.eluolang.physical.mapper.EllUserMapper;
import com.eluolang.physical.mapper.PlanMapper;
import com.eluolang.physical.mapper.ScoreMapper;
import com.eluolang.physical.model.*;
import com.eluolang.physical.model.PersonArchExpData.Res;
import com.eluolang.physical.model.PersonArchExpData.SportTxt;
import com.eluolang.physical.model.SportConfigVO.RuleVO;
import com.eluolang.physical.policy.DetailTablePolicy;
import com.eluolang.physical.service.EllUserService;
import com.eluolang.physical.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

@Slf4j
@Service
@Transactional
public class EllUserImpl implements EllUserService {
    @Autowired
    private EllUserMapper ellUserMapper;
    @Autowired
    private EllUserService ellUserService;
    @Autowired
    private ScoreMapper scoreMapper;
    @Value("${RSA.public}")//公钥
    private String RsaPublic;
    @Value("${RSA.private}")//私钥
    private String RsaPrivate;
    @Value("${kuangShiUrl}")
    private String kuangShiUrl;
    //对称加密秘钥
    private static String DesKey = "3de902b37c15e6d3";
    private static String Birth = "birth";
    private static String Sex = "sex";
    @Autowired
    private PlanMapper planMapper;
    //ell人脸生产包地址windows
//    static String haarPath = "D:\\体测项目\\体测新架构\\eluolang_back_server\\eluolang-module\\eluolang-module-physical\\src\\main\\java\\com\\eluolang\\physical\\files\\haarcascade_frontalface_alt2.xml";
//    static String modelPath = "D:\\体测项目\\体测新架构\\eluolang_back_server\\eluolang-module\\eluolang-module-physical\\src\\main\\java\\com\\eluolang\\physical\\files\\w600k_r50_new.onnx";

    //ell人脸生产包地址linux
//    static String haarPath = "/data/server/ellFace/files/haarcascade_frontalface_alt2.xml";
//    static String modelPath = "/data/server/ellFace/files/w600k_r50_new.onnx";
    @Transactional
    @Override
    public int createUser(EllUserVo ellUserVo) throws ParseException {
        if (!IsIdCardUtil.isIDNumber(ellUserVo.getIdCard())) {
            return HttpStatus.PARAMETER_ERROR;
        }
        //生成身份标识
        String identification = findString(ellUserVo.getIdCard());
        //获取是否有相同的身份标识,判断身份证重复
        List<String> stringList = ellUserMapper.findIdentification(findString(ellUserVo.getIdCard()));
        for (int i = 0; i < stringList.size(); i++) {
            //数据库的加密身份证
            String idCardMys = ellUserMapper.findIdCardById(stringList.get(i));
            String idCard = "";
            if (idCardMys != null && idCardMys != "") {
                idCard = RSAUtils.decode(idCardMys, RsaPrivate);
            }
            if (idCardMys != null && idCardMys != "" && idCard.equals(ellUserVo.getIdCard())) {
                return HttpStatus.ID_CARD_EXIST;
            } else {
                //判断同一部门重复的编号/学号
                List<EllUser> ellUsers = ellUserMapper.findUsers(null, null, ellUserVo.getStudentId(), ellUserVo.getOrgId(), null, null);
                if (ellUsers.size() > 0) {
                    return HttpStatus.STUDY_NUMBER_EXIST;
                } else {
                    //判断学籍号或者军籍号唯一
                    int studentCodeCount = ellUserMapper.findStudentCode(ellUserVo.getStudentCode());
                    if (studentCodeCount > 0) {
                        return HttpStatus.STUDY_NUMBER_EXIST;
                    }
                }
            }
        }
        Map<String, String> idCardInfo = IdCardUtil.getIdCardInfo(ellUserVo.getIdCard());
        EllUser ellUser = new EllUser();
        ellUser.setSourceCode(ellUserVo.getSourceCode());
        ellUser.setId(new Date().getTime() + IdUtils.fastSimpleUUID());
        ellUser.setUserName(ellUserVo.getUserName());
        ellUser.setCreateTime(CreateTime.getTime());
        ellUser.setUserBirth(idCardInfo.get(Birth).toString());
        ellUser.setOrgId(ellUserVo.getOrgId());
        ellUser.setEnTime(ellUserVo.getEnTime());
        ellUser.setPhone(ellUserVo.getPhone());
        ellUser.setStudentId(ellUserVo.getStudentId());
        ellUser.setIdCard(RSAUtils.encode(ellUserVo.getIdCard(), RsaPublic));
        ellUser.setUserSex(Integer.parseInt(idCardInfo.get(Sex)));
        ellUser.setHomeAddress(ellUserVo.getHomeAddress());
        ellUser.setCreateBy(ellUserVo.getCreateBy());
        ellUser.setEthnic(ellUserVo.getEthnic());
        ellUser.setEnGrade(ellUserVo.getEnGrade());
        ellUser.setStudentCode(ellUserVo.getStudentCode());
        //数据添加身份证唯一标识
        ellUserMapper.addIdentification(ellUser.getId(), identification, ellUserVo.getOrgId());
        //创建对称加密身份证
        List<ELLSymmetry> list = new ArrayList<>();
        ELLSymmetry ellSymmetry = new ELLSymmetry();
        try {
            ellSymmetry.setIdCardSymmetry(DESUtil.encrypt(ellUserVo.getIdCard(), DesKey.getBytes()));
            ellSymmetry.setUserId(ellUser.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        list.add(ellSymmetry);
        ellUserMapper.createSymmetry(list);
        return ellUserMapper.createUser(ellUser);
    }

    //截取前3位和后4位
    public String findString(String idCard) {
        String front = idCard.substring(0, 6);//前
        String queen = idCard.substring(14, 18);//后
        return front + queen;
    }

    @Override
    public List<EllUser> findUsers(FindEllUserVo findEllUserVo) {
        List<EllUser> ellUsers = ellUserMapper.findUsers(findEllUserVo.getName(), findEllUserVo.getPhone(), findEllUserVo.getStudentId(), findEllUserVo.getId(), findEllUserVo.getOrgId(), findEllUserVo.getEthnic());
        if (ellUsers.size() > 0 && findEllUserVo.getIdCard() != null) {
            List<EllUser> ellUserList = new ArrayList<>();
            for (int i = 0; i < ellUsers.size(); i++) {
                if (findEllUserVo.getIdCard().equals(RSAUtils.decode(ellUsers.get(i).getIdCard(), RsaPrivate))) {
                    ellUserList.add(ellUserList.get(i));
                    return ellUserList;
                }
            }
        }
        return ellUsers;
    }

    @Transactional
    @Override
    public int updateUser(UpdateUserVo updateUserVo) {
        return ellUserMapper.updateUser(updateUserVo);
    }

    @Override
    public List<UserInfoVo> findUser(FindEllUserVo findEllUserVo) {
        List<UserInfoVo> ellUsers = ellUserMapper.findUser(findEllUserVo.getName(), findEllUserVo.getPhone(), findEllUserVo.getStudentId(), findEllUserVo.getId(), findEllUserVo.getOrgId(), findEllUserVo.getEthnic(), findEllUserVo.getIsRegister(), findEllUserVo.getAccountOrgId());
        return ellUsers;
    }

    @Override
    public List<UserInfoVo> findOrgUsers(FindEllUserVo findEllUserVo) {
        if (findEllUserVo.getPage() > 0) {
            findEllUserVo.setPage((findEllUserVo.getPage() - 1) * 15);
        } else {
            findEllUserVo.setPage(0);
        }
        return ellUserMapper.findOrgUsers(findEllUserVo);
    }

    @Override
    public int findOrgUsersNum(FindEllUserVo findEllUserVo) {
        return ellUserMapper.findOrgUsersNum(findEllUserVo);
    }

    @Override
    public int findNoRegisterNum(String orgId) {
        return ellUserMapper.findNoRegisterNum(orgId);
    }

    @Transactional
    @Override
    public int deleteUser(String id) {
        //删除标识以及对称加密
        ellUserMapper.deleteIdentification(id);
        ellUserMapper.deleteSymetrical(id);
        return ellUserMapper.deleteUser(id);
    }

    @Override
    public List<DepartVo> findDepart(String orgId) {
        return ellUserMapper.findDepart(orgId);
    }

    @Override
    public List<DepartVo> findDeparts(String orgId) {
        List<DepartVo> departVoList = ellUserMapper.findDepart(orgId);
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < departVoList.size(); i++) {
            for (int j = 0; j < departVoList.size(); j++) {
                if (departVoList.get(i).getId().equals(departVoList.get(j).getParentId())) {
                    if (map.containsKey(departVoList.get(i).getId())) {
                        map.put(departVoList.get(i).getId(), map.get(departVoList.get(j).getParentId()) + "/" + map.get(departVoList.get(i).getParentId()));
                    } else {
                        map.put(departVoList.get(i).getId(), departVoList.get(i).getDeptName() + "/" + departVoList.get(j).getDeptName());
                    }
                }
            }
        }
        return departVoList;
    }

    @Override
    public List<Ethnic> findEthnic() {
        return ellUserMapper.findEthnic();
    }

    @Override
    public Ethnic findByNameEthnic(String name) {
        return ellUserMapper.findByNameEthnic(name);
    }

    @Transactional
    @Override
    public int importUser(List<EllUser> ellUserList, List<ELLSymmetry> ellSymmetryList, List<EllIdentification> identificationList) throws ParseException {
        if (ellSymmetryList.size() > 0) {
            ellUserMapper.createSymmetry(ellSymmetryList);
        }
        if (identificationList.size() > 0) {
            //数据添加身份证唯一标识
            ellUserMapper.addBatchIdentification(identificationList);
        }
        return ellUserMapper.importUser(ellUserList);
    }

    @Autowired
    private MegviiServerFeign megviiServerFeign;

    @Transactional
    @Override
    public int createImageFace(MultipartFile multipartFile, String userId) {
        //查询该用户是否拥有人脸
        int hasUserFace = ellUserMapper.findUserFaceInfo(userId);
        String imgId = IdUtils.fastSimpleUUID();
        if (!multipartFile.isEmpty()) {
            try {
                String[] type = multipartFile.getContentType().toString().split("/");
                //把文件写入到目标位置
                String fileName = UUID.randomUUID().toString();
                String path = "";
                if (FileUploadUtil.isLinux() == false) {
                    path = WindowsSite.IMG_PATH + fileName + "." + type[1];
                } else {
                    path = LinuxSite.IMG_PATH + fileName + "." + type[1];
                }
                //判断是否有文件路径并生成路径
                PathGeneration.createPath(path);
                File file = new File(path);
                multipartFile.transferTo(file);//把文件写入目标文件地址
                //文件进行压缩
                try {
                    PicUtils.compressPicForScale(file, 200, file.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //把文件信息存入数据库
                //把nginx访问图片地址存入数据库
                String url = "";
                url = "/" + fileName + "." + type[1];
                int i = ellUserMapper.uploadImage(fileName, String.valueOf(multipartFile.getSize()), "1", url, CreateTime.getTime(), imgId);
                if (i > 0) {
                    //  Integer.parseInt("a");
                    //获取照片
                    InputStream in = new FileInputStream(file);
                    //通过虹软把获取人脸信息
                    String faceStr = "";
                    byte[] faceByte = FaceUtil.resolveFaceCode(in);
                    faceStr = Base64.encode(faceByte);
                    String ellFace = null;
               /*     try {
//通过ell人脸包生产人脸信息
                        *//*
                     * 1、取得FaceHelper
                     *//*
                        FaceHelper faceHelper = FaceHelper.getInstance(haarPath, modelPath);

                        *//*
                     * 2、提取头像，并得到特征码
                     *//*
                        EllFaceFeature faceFeature = faceHelper.detectFaceAndGetFeature(path);
                        ellFace = faceFeature.base64();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(e);
                    }*/
                    //把照片上传到旷视
                    File images = file;
                    MultipartFile image = FileToMultipartFile.getMultipartFile(images);
//                    image.transferTo(new File("F:\\ss.jpeg"));
                    JSONObject jsonObject = new JSONObject();
                    KsImageVo ksImageVo = new KsImageVo();
                    //旷世人脸线上的注释掉只能本地部署并且有人脸识别盒子时候使用
      /*              try {
                        jsonObject = megviiServerFeign.ksImage(image);
                        if (!jsonObject.get("data").equals(HttpStatus.CODE_SUCCESS_KS)) {
                            return 0;
                        }
                        ksImageVo = JSON.parseObject(JSON.toJSONString(jsonObject.get("data")), KsImageVo.class);
                        if (ksImageVo == null) {
                            ksImageVo = new KsImageVo();
                            ksImageVo.setImageId(null);
                        } else {
                            Map<String, String[]> faceGroupList = new HashMap<>();
                            String[] aa = {"test"};
                            faceGroupList.put("faceGroupList", aa);
                            String code = FeignUtil.sendPost(kuangShiUrl + ksImageVo.getFaceToken(), faceGroupList);
                            if (!code.equals(HttpStatus.SUCCESS)) {
                                return 0;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("旷视人脸生成");
                    }*/
                    if (hasUserFace > 0) {
                        return ellUserMapper.updateImageFace(imgId, faceStr, userId, ellFace, ksImageVo.getImageId());
                    } else {
                        UpdateUserVo updateUserVo = new UpdateUserVo();
                        updateUserVo.setIsAuthentication("1");
                        updateUserVo.setId(userId);
                        ellUserMapper.updateUser(updateUserVo);
                        return ellUserMapper.createImageFace(imgId, faceStr, userId, ellFace, ksImageVo.getImageId());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
                return 0;

            }
        }
        return 0;
    }

    @Transactional
    @Override
    public String batchFaceAuthentication(MultipartFile multipartFile) {
        //查询该用户是否拥有人脸
        /*     int hasUserFace = ellUserMapper.findUserFaceInfo(userId);*/
        //errorExcelUri错误照片信息excelUri
        String errorExcelUri = null;
        //获取文件名，文件名为身份证号
        String idCard = null;
        if (!multipartFile.isEmpty()) {
            String[] type = multipartFile.getContentType().toString().split("/");
            //把文件写入到目标位置
            String fileName = UUID.randomUUID().toString();
            String path = "";
            String BATCH_IMG_PATH = "";
            if (FileUploadUtil.isLinux() == false) {
                path = WindowsSite.BATCH_IMG_PATH + fileName + ".zip";
                BATCH_IMG_PATH = WindowsSite.BATCH_IMG_PATH;
            } else {
                path = LinuxSite.BATCH_IMG_PATH + fileName + ".zip";
                BATCH_IMG_PATH = LinuxSite.BATCH_IMG_PATH;
            }
            //判断是否有路径并生成
            PathGeneration.createPath(path);
            PathGeneration.createPath(BATCH_IMG_PATH);
            //压缩包地址
            File fileZip = new File(path);
            //上传失败的用户的excel
            String fileNameExcel = UUID.randomUUID() + ".xlsx";
            List<FaceErrorVO> faceErrorVOS = new ArrayList<>();
            FaceErrorVO faceError = new FaceErrorVO();
            //文件保存
            List<FileMgr> fileMgrs = new ArrayList<>();
            //创建人脸信息
            List<EllFaceVo> ellFaceVoList = new ArrayList<>();
            //修改人脸信息
            List<EllFaceVo> ellFaceUpList = new ArrayList<>();
            try {
                multipartFile.transferTo(fileZip);//把文件写入目标文件地址
                PackageDecompression packageDecompression = new PackageDecompression();
                //解压zip文件并获取解压后的文件地址
                List<File> files = packageDecompression.decompression(path, BATCH_IMG_PATH + fileName);
                //删除压缩包
                fileZip.delete();
                for (int i = 0; i < files.size(); i++) {
                    //把文件信息存入数据库
                    //把nginx访问图片地址存入数据库
                    String imgId = IdUtils.fastSimpleUUID();
                    String url = "";
                    if (FileUploadUtil.isLinux() == false) {
                        url = "/" + files.get(i).getPath().substring(files.get(i).getPath().indexOf("EllTestImg\\") + 10);
                        url = url.replace("\\", "/");
                    } else {
                        url = "/" + files.get(i).getPath().substring(files.get(i).getPath().indexOf("EllTestImg/") + (11));
                    }
                    //获取文件名，文件名为身份证号
                    idCard = files.get(i).getName().substring(0, files.get(i).getName().lastIndexOf("."));
                    if (IsIdCardUtil.isIDNumber(idCard)) {
                        //文件进行压缩
                        try {
                            PicUtils.compressPicForScale(files.get(i), 200, files.get(i).getName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //通过身份证生成标识
                        String identification = findString(idCard);
                        //获取到用户id
                        List<String> list = ellUserMapper.findIdentification(identification);
                        //判断是否有这个用户
                        if (list.size() < 1) {
                            faceError.setIdCard(idCard);
                            faceErrorVOS.add(faceError);
                            break;
                        }
                        //获取照片
                        InputStream in = new FileInputStream(files.get(i).getPath());
                        //通过虹软把获取人脸信息
                        byte[] faceByte = FaceUtil.resolveFaceCode(in);
                        String faceStr = Base64.encode(faceByte);
                        String aiFace = null;

                   /*     try {
//通过ell人脸包生产人脸信息
                            *//*
                         * 1、取得FaceHelper
                         *//*
                            FaceHelper faceHelper = FaceHelper.getInstance(haarPath, modelPath);

                            *//*
                         * 2、提取头像，并得到特征码
                         *//*
                            EllFaceFeature faceFeature = faceHelper.detectFaceAndGetFeature(files.get(i).getPath());
                            aiFace = faceFeature.base64();
                        } catch (Exception e) {
                            faceError.setIdCard(idCard + "ELL生成错误");
                            faceErrorVOS.add(faceError);
                            e.printStackTrace();
                            System.out.println(e);
                        }*/
                        for (int j = 0; j < list.size(); j++) {
                            //判断是否拥有这个学生
                            String idCardMy = ellUserMapper.findIdCardById(list.get(j));
                            if (idCardMy != null && RSAUtils.decode(idCardMy, RsaPrivate).equals(idCard)) {
                                FileMgr fileMgr = new FileMgr();
                                fileMgr.setFileName(files.get(i).getName());
                                fileMgr.setSize(files.get(i).length());
                                fileMgr.setId(imgId);
                                fileMgr.setType(1);
                                fileMgr.setUploadTime(CreateTime.getTime());
                                fileMgr.setFileUrl(url);
                                fileMgrs.add(fileMgr);
                                //查询该用户是否拥有人脸
                                int hasUserFace = ellUserMapper.findUserFaceInfo(list.get(j));
                                //人脸信息
                                EllFaceVo ellFaceVo = new EllFaceVo();
                                ellFaceVo.setFaceInfo(faceStr);
                                ellFaceVo.setUserId(list.get(j));
                                ellFaceVo.setImageId(imgId);
                                ellFaceVo.setEllFace(aiFace);
                                //把照片放入旷视
                                MultipartFile image = FileToMultipartFile.getMultipartFile(files.get(i));
                                JSONObject jsonObject = new JSONObject();
                                KsImageVo ksImageVo = new KsImageVo();
                                //旷世人脸线上的注释掉只能本地部署并且有人脸识别盒子时候使用
                           /*     try {
                                    jsonObject = megviiServerFeign.ksImage(image);
                                    if (jsonObject.get("code").equals(HttpStatus.CODE_SUCCESS_KS)) {
                                        ksImageVo = JSON.parseObject(JSON.toJSONString(jsonObject.get("data")), KsImageVo.class);
                                        if (ksImageVo == null) {
                                            ksImageVo = new KsImageVo();
                                            ksImageVo.setImageId(null);
                                        } else {
                                            ellFaceVo.setMegviiImageId(ksImageVo.getImageId());
                                            Map<String, String[]> faceGroupList = new HashMap<>();
                                            String[] aa = {"test"};
                                            faceGroupList.put("faceGroupList", aa);
                                            String code = FeignUtil.sendPost("http://192.168.3.211/v1/MEGBOX/faceGroups/binding/" + ksImageVo.getFaceToken(), faceGroupList);
                                            if (!code.equals(HttpStatus.SUCCESS)) {
                                                faceError.setIdCard(idCard + "--人脸绑定分组失败");
                                                faceErrorVOS.add(faceError);
                                            }
                                        }
                                    } else {
                                        faceError.setIdCard(idCard + "--" + jsonObject.get("message"));
                                        faceErrorVOS.add(faceError);
                                    }
                                } catch (Exception e) {
                                    System.out.println(e);
                                }*/
                                ellFaceVoList.add(ellFaceVo);
                                if (fileMgrs.size() >= 100 && ellFaceUpList.size() >= 100) {
                                    //照片保存
                                    if (fileMgrs != null && fileMgrs.size() > 0) {
                                        ellUserMapper.uploadImageMany(fileMgrs);
                                    }
                                    if (ellFaceUpList != null && ellFaceUpList.size() > 0) {
                                        //需要修改人脸信息的用户先进行删除再添加
                                        ellUserMapper.delImageFaceMany(ellFaceUpList);
                                    }
                                    if (ellFaceVoList != null && ellFaceVoList.size() > 0) {
                                        //人脸信息保存
                                        ellUserMapper.createImageFaceMany(ellFaceVoList);
                                    }
                                    fileMgrs = new ArrayList<>();
                                    ellFaceUpList = new ArrayList<>();
                                    ellFaceVoList = new ArrayList<>();
                                }
                                //存在就修改
                                if (hasUserFace > 0) {
                                    //ellUserMapper.updateImageFace(imgId, faceStr, list.get(j));
                                    //修改要进行删除的
                                    ellFaceUpList.add(ellFaceVo);
                                    continue;
                                } else {
                                    UpdateUserVo updateUserVo = new UpdateUserVo();
                                    updateUserVo.setIsAuthentication("1");
                                    updateUserVo.setId(list.get(j));
                                    ellUserMapper.updateUser(updateUserVo);
                                    ellFaceUpList.add(ellFaceVo);
                                    continue;
                                }

                            } else {
                                faceError.setIdCard(idCard);
                                faceErrorVOS.add(faceError);
                            }
                        }
                    } else {
                        faceError.setIdCard(idCard);
                        faceErrorVOS.add(faceError);
                    }

                }
                //照片保存
                if (fileMgrs != null && fileMgrs.size() > 0) {
                    ellUserMapper.uploadImageMany(fileMgrs);
                }
                if (ellFaceUpList != null && ellFaceUpList.size() > 0) {
                    //需要修改人脸信息的用户先进行删除再添加
                    ellUserMapper.delImageFaceMany(ellFaceUpList);
                }
                if (ellFaceVoList != null && ellFaceVoList.size() > 0) {
                    //人脸信息保存
                    ellUserMapper.createImageFaceMany(ellFaceVoList);
                }
                if (faceErrorVOS.size() > 0) {
                    String errorExcelPath = "";
                    String FACE_INFO_MISRAKE_URI = "";
                    if (FileUploadUtil.isLinux() == false) {
                        errorExcelPath = WindowsSite.FACE_INFO_MISRAKE + fileNameExcel;
                        FACE_INFO_MISRAKE_URI = WindowsSite.FACE_INFO_MISRAKE_URI;
                    } else {
                        errorExcelPath = LinuxSite.FACE_INFO_MISRAKE + fileNameExcel;
                        FACE_INFO_MISRAKE_URI = LinuxSite.FACE_INFO_MISRAKE_URI;
                    }
                    //生成路径
                    PathGeneration.createPath(errorExcelPath);
                    PathGeneration.createPath(FACE_INFO_MISRAKE_URI);
                    File fileErrorExel = new File(errorExcelPath);
                    errorExcelUri = "/" + FACE_INFO_MISRAKE_URI + fileNameExcel;
                    //错误照片信息保存
                    ellUserMapper.uploadImage(fileNameExcel, String.valueOf(fileErrorExel.length()), "2", errorExcelUri, CreateTime.getTime(), IdUtils.fastSimpleUUID());
                    EasyExcel.write(errorExcelPath, FaceErrorVO.class).sheet("模板").doWrite(faceErrorVOS);
                    return errorExcelUri;
                }
            } catch (Exception e) {
                faceError.setIdCard(idCard);
                faceErrorVOS.add(faceError);
                e.printStackTrace();
                System.out.println(e);
            }
        }
        return errorExcelUri;
    }


    @Override
    public int findNotAuthenticated(String orgId) {
        return ellUserMapper.findNotAuthenticated(orgId);
    }

    @Override
    public int insertExamineeGroup(EllExamineeGroup ellExamineeGroup) {
        return ellUserMapper.insertExamineeGroup(ellExamineeGroup);
    }

    @Override
    public int insertExamineeUser(List<EllExamineeUser> ellExamineeUserList) {
        return ellUserMapper.insertExamineeUser(ellExamineeUserList);
    }

    @Override
    public boolean ExamineeGroupBySex(List<List<EllUser>> ellUsers, GroupingDto groupingDto, Integer sex) {
        if (ellUsers.size() != 0) {
            for (int i = 0; i < ellUsers.size(); i++) {
                String uuid = IdUtils.fastSimpleUUID();
                EllExamineeGroup ellExamineeGroup = new EllExamineeGroup();
                ellExamineeGroup.setId(uuid);
                if (sex == Constants.MALE) {
                    ellExamineeGroup.setGroupName(groupingDto.getPlanName() + "男" + (i + 1) + "组");
                } else {
                    ellExamineeGroup.setGroupName(groupingDto.getPlanName() + "女" + (i + 1) + "组");
                }
                ellExamineeGroup.setPlanId(groupingDto.getPlanId());
                ellExamineeGroup.setOptId(groupingDto.getOptId());
                int count = ellUserService.insertExamineeGroup(ellExamineeGroup);
                if (count > 0) {
                    List<EllExamineeUser> ellExamineeUsers = new ArrayList<>();
                    for (int j = 0; j < ellUsers.get(i).size(); j++) {
                        EllExamineeUser ellExamineeUser = new EllExamineeUser();
                        ellExamineeUser.setId(IdUtils.fastSimpleUUID());
                        ellExamineeUser.setGroupId(uuid);
                        ellExamineeUser.setStuId(ellUsers.get(i).get(j).getId());
                        ellExamineeUser.setTestNumber(GroupingUtil.randomTestNumber(sex));
                        ellExamineeUsers.add(ellExamineeUser);
                    }
                    int count1 = ellUserService.insertExamineeUser(ellExamineeUsers);
                    if (count1 < 1) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean upsetGroup(List<List<EllUser>> ellUsers, GroupingDto groupingDto) {
        if (ellUsers.size() != 0) {
            for (int i = 0; i < ellUsers.size(); i++) {
                String uuid = IdUtils.fastSimpleUUID();
                EllExamineeGroup ellExamineeGroup = new EllExamineeGroup();
                ellExamineeGroup.setId(uuid);
                ellExamineeGroup.setGroupName(groupingDto.getPlanName() + (i + 1) + "组");
                ellExamineeGroup.setPlanId(groupingDto.getPlanId());
                ellExamineeGroup.setOptId(groupingDto.getOptId());
                int count = ellUserService.insertExamineeGroup(ellExamineeGroup);
                if (count > 0) {
                    List<EllExamineeUser> ellExamineeUsers = new ArrayList<>();
                    for (int j = 0; j < ellUsers.get(i).size(); j++) {
                        EllExamineeUser ellExamineeUser = new EllExamineeUser();
                        ellExamineeUser.setId(IdUtils.fastSimpleUUID());
                        ellExamineeUser.setGroupId(uuid);
                        ellExamineeUser.setStuId(ellUsers.get(i).get(j).getId());
                        ellExamineeUser.setTestNumber(GroupingUtil.randomTestNumber(ellUsers.get(i).get(j).getUserSex()));
                        ellExamineeUsers.add(ellExamineeUser);
                    }
                    int count1 = ellUserService.insertExamineeUser(ellExamineeUsers);
                    if (count1 < 1) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<EllUser> selEllUserByDeptId(List<PfDepart> pfDepartList) {
        return ellUserMapper.selEllUserByDeptId(pfDepartList);
    }

    @Override
    public List<EllReturnScoreVo> findUserById(String id) {
        return ellUserMapper.findUserById(id);
    }

    @Override
    public List<EllReturnScoreVo> findUserByIdCard(String idCard) {
        return ellUserMapper.findUserByIdCard(idCard);
    }

    @Override
    public String findDeptNameById(int orgId) {
        return ellUserMapper.findDeptNameById(orgId);
    }

    @Override
    public List<EllReturnScoreVo> findUserBy(String identification, int orgId) {
        return ellUserMapper.findUserBy(identification, orgId);
    }

    @Override
    public List<EllExamineeGroup> selectExamineeGroupAll(String planId) {
        return ellUserMapper.selectExamineeGroupAll(planId);
    }

    @Override
    public int selExamineeGroupByPlanId(String planId) {
        return ellUserMapper.selExamineeGroupByPlanId(planId);
    }

    @Override
    public List<EllExamineeVo> selectExaminee(EllExamineeDto ellExamineeDto) {
        return ellUserMapper.selectExaminee(ellExamineeDto);
    }

    @Override
    public int deleteExamineeByPlanId(String planId) {
        return ellUserMapper.deleteExamineeByPlanId(planId);
    }

    @Override
    public Result expPersonHis(String planId, String userId, HttpServletRequest request, HttpServletResponse response) {
        // 生成数据
        PersonArchExpData data = new PersonArchExpData();
        List<EllReturnScoreVo> user = ellUserMapper.findUserById(userId);
        String score = scoreMapper.selUserAvg(userId, planId);
        Integer avgScore = Double.valueOf(score).intValue();
        List<TestHistoryModel> his = scoreMapper.selUserTestScore(userId, planId);
        String comment = "";
        if (avgScore >= 60 && avgScore < 70) {
            comment = "及格";
        } else if (avgScore >= 70 && avgScore < 90) {
            comment = "良";
        } else if (avgScore >= 90) {
            comment = "优秀";
        } else if (avgScore < 60) {
            comment = "不及格";
        }
        data.setU(PersonArchExpData.User.builder().name(user.get(0).getUserName()).gcName(user.get(0).getDeptName())
                .stuNum(user.get(0).getStudentId()).build());
        data.setR(Res.builder().score(String.valueOf(avgScore)).remark(comment).build());
        data.setYear(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy")));
        Map<String, PhyData> obj = new HashMap<>();
        for (int i = 0; i < his.size(); i++) {
            PhyData phyData = new PhyData();
            phyData.setName(his.get(i).getProjectName());
            phyData.setScore(his.get(i).getScore());
            phyData.setComment(his.get(i).getComment());
            List<PhyData.RowData> rowDataList = new ArrayList<>();
            Map<String, Object> testDataMaps = JSONObject.parseObject(his.get(i).getTesData());
            Map<String, List<TestData>> testDataMap = new HashMap<>();
            for (String key : testDataMaps.keySet()) {
                testDataMap.put(key, JSONArray.parseArray(testDataMaps.get(String.valueOf(key)).toString(), TestData.class));
            }
            for (String key : testDataMap.keySet()) {
                List<TestData> testData = testDataMap.get(key);
                for (int j = 0; j < testData.size(); j++) {
                    PhyData.RowData rowData = new PhyData.RowData();
                    rowData.setVal(testData.get(j).getData());
                    rowData.setName(testData.get(j).getRemark());
                    rowData.setIsmax(true);
                    if (testData.get(j).getUnit() == null) {
                        rowData.setUnit("");
                    } else {
                        rowData.setUnit(testData.get(j).getUnit());
                    }
                    rowDataList.add(rowData);
                }
            }
            phyData.setData(rowDataList);
            obj.put(his.get(i).getProNameAbbreviation(), phyData);
        }
        List<String> config = Arrays.asList("身体形态", "心肺功能", "柔韧度", "上下肢力量", "耐力");//图表项目
//		log.info(JSON.toJSONString(obj,SerializerFeature.WriteMapNullValue));
        this.configRule(config, user.get(0).getUserSex(), obj, data);
        // 导出动态表格
        Configure configTable = Configure.builder().bind("detail_table", new DetailTablePolicy(obj)).build();
        try {
            String modePath = "";
            if (FileUploadUtil.isLinux() == false) {
                modePath = WindowsSite.modePath;
            } else {
                modePath = LinuxSite.modePath;
            }
            //生成路径
            PathGeneration.createPath(modePath + "personArchModel.docx");
            XWPFTemplate template = XWPFTemplate.compile(new File(modePath + "personArchModel.docx"), configTable).render(data);
            response.setHeader("Content-disposition", "attachment;filename=\"(" + user.get(0).getUserName() + ").docx" + "\"");
            OutputStream out = response.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(out);
            template.write(bos);
            bos.flush();
            out.flush();
            PoitlIOUtils.closeQuietlyMulti(template, bos, out);
        } catch (IOException e) {
            return new Result(HttpStatus.ERROR, "导出失败", e);
        }
        return new Result(HttpStatus.SUCCESS, "导出成功", null);
    }

    @Override
    public Result expPersonHisZip(String planId, List<String> userIdList, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String modePath = null;
        String zipPath = "";
        if (FileUploadUtil.isLinux() == false) {
            zipPath = WindowsSite.ZIP_PATH;
        } else {
            zipPath = LinuxSite.ZIP_PATH;
        }
        List<String> filesPathArray = new ArrayList<>();
        for (int k = 0; k < userIdList.size(); k++) {
            // 生成数据
            PersonArchExpData data = new PersonArchExpData();
            List<EllReturnScoreVo> user = ellUserMapper.findUserById(userIdList.get(k));
            if (user.size() == 0) {
                return new Result(HttpStatus.ERROR, "未查询到该数据", null);
            }
            String score = scoreMapper.selUserAvg(userIdList.get(k), planId);
            Integer avgScore = Double.valueOf(score).intValue();
            List<TestHistoryModel> his = scoreMapper.selUserTestScore(userIdList.get(k), planId);
            String comment = "";
            if (avgScore >= 60 && avgScore < 70) {
                comment = "及格";
            } else if (avgScore >= 70 && avgScore < 90) {
                comment = "良";
            } else if (avgScore >= 90) {
                comment = "优秀";
            } else if (avgScore < 60) {
                comment = "不及格";
            }
            data.setU(PersonArchExpData.User.builder().name(user.get(0).getUserName()).gcName(user.get(0).getDeptName())
                    .stuNum(user.get(0).getStudentId()).build());
            data.setR(Res.builder().score(String.valueOf(avgScore)).remark(comment).build());
            data.setYear(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy")));
            Map<String, PhyData> obj = new HashMap<>();
            for (int i = 0; i < his.size(); i++) {
                PhyData phyData = new PhyData();
                phyData.setName(his.get(i).getProjectName());
                phyData.setScore(his.get(i).getScore());
                phyData.setComment(his.get(i).getComment());
                List<PhyData.RowData> rowDataList = new ArrayList<>();
                Map<String, Object> testDataMaps = JSONObject.parseObject(his.get(i).getTesData());
                Map<String, List<TestData>> testDataMap = new HashMap<>();
                for (String key : testDataMaps.keySet()) {
                    testDataMap.put(key, JSONArray.parseArray(testDataMaps.get(String.valueOf(key)).toString(), TestData.class));
                }
                for (String key : testDataMap.keySet()) {
                    List<TestData> testData = testDataMap.get(key);
                    for (int j = 0; j < testData.size(); j++) {
                        PhyData.RowData rowData = new PhyData.RowData();
                        rowData.setVal(testData.get(j).getData());
                        rowData.setName(testData.get(j).getRemark());
                        rowData.setIsmax(true);
                        if (testData.get(j).getUnit() == null) {
                            rowData.setUnit("");
                        } else {
                            rowData.setUnit(testData.get(j).getUnit());
                        }
                        rowDataList.add(rowData);
                    }
                }
                phyData.setData(rowDataList);
                obj.put(his.get(i).getProNameAbbreviation(), phyData);
            }
            List<String> config = Arrays.asList("身体形态", "心肺功能", "柔韧度", "上下肢力量", "耐力");//图表项目
//		log.info(JSON.toJSONString(obj,SerializerFeature.WriteMapNullValue));
            this.configRule(config, user.get(0).getUserSex(), obj, data);
            // 导出动态表格
            Configure configTable = Configure.builder().bind("detail_table", new DetailTablePolicy(obj)).build();
            try {
                if (FileUploadUtil.isLinux() == false) {
                    modePath = WindowsSite.modePath;
                } else {
                    modePath = LinuxSite.modePath;
                }
                //生成路径
                PathGeneration.createPath(modePath + "personArchModel.docx");
                XWPFTemplate template = XWPFTemplate.compile(new File(modePath + "personArchModel.docx"), configTable).render(data);
                //response.setHeader("Content-disposition", "attachment;filename=\"(" + user.get(0).getUserName() + ").docx" + "\"");
//            OutputStream out = response.getOutputStream();
//            BufferedOutputStream bos = new BufferedOutputStream(out);
                String filePath = zipPath + user.get(0).getUserName() + ".docx";
                File dest = new File(filePath);
                //检测上传的文件目录是否存在
                if (!dest.getParentFile().exists()) {
                    //不存在则新建文件夹
                    dest.getParentFile().mkdirs();
                }
                template.writeToFile(filePath);
                filesPathArray.add(filePath);
//            bos.flush();
//            out.flush();
                PoitlIOUtils.closeQuietlyMulti(template);
            } catch (IOException e) {
                return new Result(HttpStatus.ERROR, "导出失败", e);
            }
        }

        String fileName = DateUtils.getCurrentTime() + "学生健康档案.zip";
        CompressionAndDecompressionUtils.zipCompression(filesPathArray.toArray(new String[filesPathArray.size()]), zipPath + fileName);
        return new Result(HttpStatus.SUCCESS, "导出成功", fileName);
    }

    public static final String color = "FFFFFF";

    /**
     * 查询运动指南规则
     * <p>Title: </p>
     *
     * @param config
     * @param sex
     * @param objData
     */
    public void configRule(List<String> config, int sex, Map<String, PhyData> objData, PersonArchExpData data) {
        List<SportConfigVO> configList = this.ellUserMapper.querySportConfig();
        Map<String, SportConfigVO> configMap = configList.stream()
                .collect(Collectors.toMap(SportConfigVO::getName, Function.identity()));
        List<String> chartTitle = new ArrayList<String>();
        List<Double> chartScore = new ArrayList<Double>();
        List<SportTxt> txtList = new ArrayList<SportTxt>();
        configMap.forEach((k, v) -> {
            Map<String, List<RuleVO>> ruleMap = v.getRuleList().stream().filter(r -> r.getSex() == sex)
                    .collect(Collectors.groupingBy(RuleVO::getProKey));
            Object[] obj = this.queryHisRes(k, ruleMap, objData);
            chartTitle.add(k);
            chartScore.add(((Double) obj[0]) + 50D);
            txtList.add(SportTxt.builder()
                    .title(Texts.of(k).bold().color(color).create())
                    .txt(Texts.of(obj[1].toString()).color(color).create()).build());
        });
        ChartMultiSeriesRenderData rardar = Charts
                .ofMultiSeries("趋势分布", chartTitle.toArray(new String[]{}))
                .addSeries("分数", chartScore.toArray(new Double[]{}))
                .create();
        ExpModelPersoncChartVO.builder().chartScore(chartScore).chartTitle(chartTitle).build();
        data.setSections(txtList);
        data.setRardar(rardar);
    }

    /**
     * 拼接评语和计算平均分
     * <p>Title: </p>
     *
     * @param name    运动服指南名称
     * @param ruleMap 运动指南规则评语
     * @param obj     体质测试成绩
     */
    public Object[] queryHisRes(String name, Map<String, List<RuleVO>> ruleMap, Map<String, PhyData> obj) {
        StringBuffer txt = new StringBuffer();
        List<Double> scoreList = new ArrayList<Double>();
        ruleMap.forEach(
                (k, v) -> {
                    PhyData data = obj.get(k);
                    if (null != data) {
                        v.stream()
                                .filter(r -> r.getComment().equals(data.getComment()))
                                .findFirst()
                                .map(
                                        r -> {
                                            scoreList.add(Double.parseDouble(data.getScore()));
                                            return txt.append(r.getGuide());
                                        });
                    }
                });
        Double sumSocre = scoreList.stream().collect(Collectors.summarizingDouble(v -> v)).getSum();
        Object[] retArr = new Object[2];//评分、评语
        retArr[0] = (sumSocre.isNaN() || sumSocre.isInfinite() || sumSocre.intValue() == 0) ? new Double("0") : new Double(String.format("%.2f", (sumSocre.doubleValue() / ruleMap.size())));
        retArr[1] = StringUtils.isBlank(txt) ? "暂无！" : txt.toString();
//		log.info("项目："+name+"=====分数："+sumSocre.intValue()+"===平均分："+JSON.toJSONString(retArr));
        return retArr;
    }

    @Override
    public int updatePfDepartStatus(List<Integer> deptIdList) {
        return ellUserMapper.updatePfDepartStatus(deptIdList);
    }

    @Override
    public int updateUserStatusList(List<EllUser> userIdList) {
        return ellUserMapper.updateUserStatusList(userIdList);
    }

    @Override
    public List<EllUser> selUserByDeptIdList(List<Integer> deptIdList) {
        return ellUserMapper.selUserByDeptIdList(deptIdList);
    }

    @Override
    public int insertRetestGroup(List<EllRetestGroup> ellRetestGroupList) {
        return ellUserMapper.insertRetestGroup(ellRetestGroupList);
    }

    @Override
    public int findIsRetestGroupUser(String planId, String userId) {
        return ellUserMapper.findIsRetestGroupUser(planId, userId);
    }

    @Override
    public List<String> findIdentification(String identification) {
        return ellUserMapper.findIdentification(identification);
    }

    @Override
    public String findIdCardById(String id) {
        return ellUserMapper.findIdCardById(id);
    }

    @Override
    public int findOrgUserSize(int accountOrgId, String orgId) {
        return ellUserMapper.findOrgUserSize(accountOrgId, orgId);
    }

    @Override
    public String findFeature(String planId, String image) {
        //存储最大特征值
        double maxFeature = 0;
        StringBuffer stringUserId = new StringBuffer();
        try {
            //判断图片的活体
            FaceEngine faceEngine = SpringUtil.getBean(FaceEngine.class);
            //base64转为byte[]
            BASE64Decoder d = new BASE64Decoder();
            byte[] imageData = d.decodeBuffer(image);
            ImageInfo imageInfo = getRGBData(imageData);
            //获取人脸信息
            List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
            int detectCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
            //特征提取
            FaceFeature faceFeature = new FaceFeature();
            int extractCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);
            //通过虹软把获取人脸信息特征
            byte[] faceByte = faceFeature.getFeatureData();
            //开启人脸检测的功能
            FunctionConfiguration configuration = new FunctionConfiguration();
            configuration.setSupportLiveness(true);
            //将开启人脸检测的功能参数放入检测
            int processCode = faceEngine.process(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList, configuration);
            //活体检测
            List<LivenessInfo> livenessInfoList = new ArrayList<LivenessInfo>();
            int livenessCode = faceEngine.getLiveness(livenessInfoList);
            System.out.println("活体：" + livenessInfoList.get(0).getLiveness());
            //非活体就返回
           /* if (livenessInfoList.get(0).getLiveness() != 1) {
                return HttpStatus.NON_LIVING_BODY + "";
            }*/
            //获取数据库的相关特征值
            List<EllFeature> featureList = ellUserMapper.findFeature(planId);
            for (int i = 0; i < featureList.size(); i++) {
                //数据库人脸特征值
                byte[] faceByteMy = d.decodeBuffer(featureList.get(i).getFaceInfo());
                //特征比对
                FaceFeature targetFaceFeature = new FaceFeature();
                //作比较的特征值
                targetFaceFeature.setFeatureData(faceByte);
                FaceFeature sourceFaceFeature = new FaceFeature();
                //数据库存储的特征值
                sourceFaceFeature.setFeatureData(faceByteMy);
                FaceSimilar faceSimilar = new FaceSimilar();
                //做比较
                int compareCode = faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
                //faceSimilar.getScore()相识度大于0.7的进行存储
                if (faceSimilar.getScore() > 0.5 && maxFeature < faceSimilar.getScore()) {
                    stringUserId.setLength(0);
                    maxFeature = faceSimilar.getScore();
                    stringUserId.append(featureList.get(i).getUserId());
                }
                System.out.println("相似度：" + faceSimilar.getScore());
            }
            if (maxFeature < 0.7) {
                return HttpStatus.MATCHING_FAILURE + "";
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

        return stringUserId.toString();
    }

    @Override
    public EllAdmissionTicketPrintingVo AdmissionTicketPrinting(String planId, String userId) {
        //项目名称
        List<String> proName = new ArrayList<>();
        //查询计划
        List<EllPlan> ellPlans = planMapper.findPlan(planId, null, null, null);
        //查询自选项目
        List<EllFindSelectedPro> ellFindSelectedPros = planMapper.findProjectOptional(planId, userId, 0);
        EllAdmissionTicketPrintingVo admissionTicketPrinting = new EllAdmissionTicketPrintingVo();
        if (ellPlans.get(0).getCustomizeTheNumber() == ellFindSelectedPros.size()) {
            //查询学生信息
            List<EllUser> users = ellUserMapper.findUsers(null, null, null, userId, null, null);
            //查询计划必选项目
            List<EllPlanProjectChance> ellPlanProjectList = planMapper.findMustProject(planId, String.valueOf(users.get(0).getUserSex()));
            //查询学校名称
            String schoolName = ellUserMapper.findOrgName(users.get(0).getOrgId(), 1, userId);
            //查询班级名称
            String className = ellUserMapper.findOrgName(users.get(0).getOrgId(), 0, userId);
            //查询照片
            String userImage = ellUserMapper.findUserImage(userId);
            //封装自选项目名称
            for (int i = 0; i < ellFindSelectedPros.size(); i++) {
                proName.add(ellFindSelectedPros.get(i).getProName());
            }
            //封装必选项目名称
            for (int i = 0; i < ellPlanProjectList.size(); i++) {
                proName.add(ellPlanProjectList.get(i).getProName());
            }
            admissionTicketPrinting.setExamNumber(ellUserMapper.selTestNumber(userId, planId));
            admissionTicketPrinting.setClassName(className);
            admissionTicketPrinting.setSex(users.get(0).getUserSex());
            admissionTicketPrinting.setPlanTitle(ellPlans.get(0).getPlanTitle());
            admissionTicketPrinting.setDateBegin(ellPlans.get(0).getDateBegin());
            admissionTicketPrinting.setDateEnd(ellPlans.get(0).getDateEnd());
            admissionTicketPrinting.setUserName(users.get(0).getUserName());
            admissionTicketPrinting.setImgUrl(userImage);
            admissionTicketPrinting.setSchoolName(schoolName);
            admissionTicketPrinting.setProName(proName);
            admissionTicketPrinting.setId(users.get(0).getId());
            admissionTicketPrinting.setStudentCode(users.get(0).getStudentCode());
        } else {
            return null;
        }
        return admissionTicketPrinting;
    }

    @Override
    public String findUserImage(String userId) {
        return ellUserMapper.findUserImage(userId);
    }

    @Override
    public String findOrgName(String orgId, int orgType, String userId) {
        return ellUserMapper.findOrgName(orgId, orgType, userId);
    }

    @Override
    public List<EllUser> findUsers(String name, String phone, String studentId, String id, String orgId, String ethnic) {
        return ellUserMapper.findUsers(name, phone, studentId, id, orgId, ethnic);
    }

    @Override
    public String batchAdmissionTicketPrinting(String planId, String orgId) {
        List<EllPlan> ellPlanList = planMapper.findPlan(planId, null, null, null);
        return null;
    }

    @Override
    public String findImageUrl(String userId) {
        return ellUserMapper.findUserImage(userId);
    }

    @Override
    public int hasTestProject(FindAvoidVo findAvoidVo) {
        return ellUserMapper.hasTestProject(findAvoidVo);
    }

    @Override
    public String selSchoolName(int orgId) {
        return ellUserMapper.selSchoolName(orgId);
    }

    @Override
    public int findStudentCode(String studentCode) {
        return ellUserMapper.findStudentCode(studentCode);
    }

    @Override
    public List<EllUser> selPlanUsers(String planId) {
        return ellUserMapper.selPlanUsers(planId);
    }

    @Override
    public List<PfDepart> selPlanSchool(String planId, int type, String classOrgId) {
        return ellUserMapper.selPlanSchool(planId, type, classOrgId);
    }

    @Override
    public List<EllUserPlanDepartment> selUsePlanDep(String planId) {
        return ellUserMapper.selUsePlanDep(planId);
    }

    @Override
    public List<EllUser> selPlanSchoolUser(String planId, String orgId) {
        return ellUserMapper.selPlanSchoolUser(planId, orgId);
    }

    @Override
    public List<EllOptionalUserProVo> selOptionalUserPro(String planId, String orgId) {
        return ellUserMapper.selOptionalUserPro(planId, orgId);
    }

    @Override
    public int insertBacthExamineeGroup(List<EllExamineeGroup> ellExamineeGroup) {
        return ellUserMapper.insertBacthExamineeGroup(ellExamineeGroup);
    }

    @Override
    public List<EllExamineeUser> selGroupUser(String groupId) {
        return ellUserMapper.selGroupUser(groupId);
    }

    @Override
    public EllUser selUserById(String userId) {
        return ellUserMapper.selUserById(userId);
    }

    @Override
    public List<EllSexNumDto> selSexUser(FindEllUserVo findEllUserVo) {
        List<EllSexNumDto> list = ellUserMapper.selSexUser(findEllUserVo);
        //男女都没有的情况
        if (list == null || list.size() <= 0) {
            list.add(new EllSexNumDto(0, 1));
            list.add(new EllSexNumDto(0, 2));
        } else if (list.size() == 1) {
            //只有男生的情况
            if (list.get(0).getUserSex() == 1) {
                list.add(new EllSexNumDto(0, 2));
            } else {
                //只有女生的情况
                list.add(new EllSexNumDto(0, 1));
            }
        }
        return list;
    }

    @Override
    public List<EllUser> findExaminee(String planId) {
        List<EllPlan> list = planMapper.findPlan(planId, null, null, null);
        /*if (list.get(0).getIsSubscribe()==1) {

        }*/
        return null;
    }
}
