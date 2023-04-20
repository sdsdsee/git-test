package com.eluolang.common.core.pojo;

/**
 * 分组学生关联表
 * @author dengrunsen
 * @date 2022/1/11
 */
public class EllExamineeUser {
    /** id */
    private String id;
    /** 考生分组id */
    private String groupId;
    /** 学生id */
    private String stuId;
    /** 考号 */
    private String testNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(String testNumber) {
        this.testNumber = testNumber;
    }
}
