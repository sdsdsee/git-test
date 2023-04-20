package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllEyesightParticulars implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 计划id
     */
    private String planId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 视力情况
     */
    private String visionComment;
    /**
     * 左眼裸眼视力
     */
    private String leftVision;
    /**
     * 右眼裸眼视力
     */
    private String rightVision;
    /**
     * 左眼屈光度(球镜度)
     */
    private String leftDiopterSph;
    /**
     * 右眼屈光度(球镜度)
     */
    private String rightDiopterSph;
    /**
     * 左眼屈光度(柱镜度)
     */
    private String leftDiopterCyl;
    /**
     * 右眼屈光度(柱镜度)
     */
    private String rightDiopterCyl;
    /**
     * 左眼屈光度(轴位)
     */
    private String leftDiopterAx;
    /**
     * 右眼屈光度(轴位)
     */
    private String rightDiopterAx;
    /**
     * 左眼屈光矫正
     */
    private String leftDiopterCorrect;
    /**
     * 右眼屈光矫正
     */
    private String rightDiopterCorrect;
    /**
     * 左眼角膜曲率班级(水平方向)
     */
    private String leftCornealCurvatureLevel;
    /**
     * 左眼角膜曲率班级(垂直方向)
     */
    private String leftCornealCurvatureVertical;
    /**
     * 左眼角膜曲率班级(水平方向-轴位)
     */
    private String leftCornealCurvatureLevelAx;
    /**
     * 左眼角膜曲率班级(垂直方向-轴位)
     */
    private String leftCornealCurvatureVerticalAx;
    /**
     * 右眼角膜曲率班级(水平方向)
     */
    private String rightCornealCurvatureLevel;
    /**
     * 右眼角膜曲率班级(垂直方向)
     */
    private String rightCornealCurvatureVertical;
    /**
     * 右眼角膜曲率班级(水平方向-轴位)
     */
    private String rightCornealCurvatureLevelAx;
    /**
     * 右眼角膜曲率班级(水平方向-轴位)
     */
    private String rightCornealCurvatureVerticalAx;
    /**
     * 左眼角膜屈光度(水平方向)
     */
    private String leftCornealDiopterLevel;
    /**
     * 左眼角膜屈光度(垂直方向)
     */
    private String leftCornealDiopterVertical;
    /**
     * 左眼角膜屈光度(平均值)
     */
    private String leftCornealDiopterAug;
    /**
     * 右眼角膜屈光度(水平方向
     */
    private String rightCornealDiopterLevel;
    /**
     * 右眼角膜屈光度(垂直方向)
     */
    private String rightCornealDiopterVertical;
    /**
     * 右眼角膜屈光度(平均值)
     */
    private String rightCornealDiopterAug;
    /**
     * 左眼角膜散光度
     */
    private String leftCornealAstigmatism;
    /**
     * 右眼角膜散光度
     */
    private String rightCornealAstigmatism;
    /**
     * 左眼瞳孔直径
     */
    private String leftPupil;
    /**
     * 右眼瞳孔直径
     */
    private String rightPupil;
    /**
     * 瞳距
     */
    private String pupilDistance;
    /**
     * 左眼屈光正不正(0正常1近视2远视)
     */
    private Integer leftDioptricNormal;
    /**
     * 右眼屈光正不正(0正常1近视2远视)
     */
    private Integer rightDioptricNormal;
    /**
     * 是否删除
     */
    private Integer isDelete;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 次数
     */
    private Integer countNum;
    /**
     * 是否为最后一次0否1是
     */
    private Integer isLast;
    /**
     * 左眼串镜
     */
    private String leftTandemMirror;
    /**
     * 右眼串镜
     */
    private String rightTandemMirror;

}