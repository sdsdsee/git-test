package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllFile {
    /**
     * id
     */
    private String id;
    /**
     *上传时间
     */
    private String uploadTime;
    /**
     *文件类型
     */
    private String type;
    /**
     *文件ID
     */
    private String file_id;
    /**
     *文件地址
     */
    private String fileUrl;
    /**
     * 文件压缩地址
     */
    private String thumbnailUrl;
    /**
     * 文件大小
     */
    private String size;
    /**
     * 文件名称
     */
    private String fileName;
}
