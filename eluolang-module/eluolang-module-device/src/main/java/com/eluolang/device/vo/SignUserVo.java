package com.eluolang.device.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年07月28日 17:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUserVo {
    /** id */
    private String id;
    /** 学生姓名 */
    private String username;
    /** 入学时间 */
    private String enTime;
    /** 入学年级 */
    private Integer enGrade;
    /** 年级 */
    private Integer grade;
    /** 班级id */
    private Integer classId;
    /** 班级名称 */
    private String className;
    /** 学生id */
    private String studentId;
    /** 学校id */
    private Integer schoolId;
    /** 学校名称 */
    private String schoolName;
    /** 文件地址 */
    private String fileUrl;
}
