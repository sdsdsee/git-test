package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllVideoVo {
    /**
     * id
     */
    private String id;
    /**
     * 上传时间
     */
    private String uploadTime;
    /**
     * 文件类型
     */
    private Integer type;
    /**
     * 文件地址
     */
    private String fileUrl;
    /**
     * 文件压缩地址
     */
    private String thumbnailUrl;
    /**
     * 文件大小
     */
    private long size;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 视频项目名称
     */
    private String proName;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 学籍号
     */
    private String studentCode;
}
