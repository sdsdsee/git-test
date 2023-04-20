package com.eluolang.common.core.pojo;

import java.io.Serializable;

/**
 * 文件管理表
 *
 * @author suziwei
 * @date 2020/8/20
 */

public class FileMgr implements Serializable,Cloneable
{
    /** id */
    private String id ;
    /** 上传时间 */
    private String uploadTime ;
    /** 文件类型 */
    private Integer type ;
    /** 文件地址 */
    private String fileUrl ;
    /** 文件压缩地址 */
    private String thumbnailUrl ;
    /** 文件大小 */
    private long size ;
    /** 文件名称 */
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString()
    {
        return "FileMgr{" +
                "id=" + id +
                ", uploadTime=" + uploadTime +
                ", type=" + type +
                ", fileUrl='" + fileUrl + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", size='" + size + '\'' +
                '}';
    }

    /** id */
    public String getId(){
        return this.id;
    }
    /** id */
    public void setId(String id){
        this.id = id;
    }
    /** 上传时间 */
    public String getUploadTime(){
        return this.uploadTime;
    }
    /** 上传时间 */
    public void setUploadTime(String uploadTime){
        this.uploadTime = uploadTime;
    }
    /** 文件类型 */
    public Integer getType(){
        return this.type;
    }
    /** 文件类型 */
    public void setType(Integer type){
        this.type = type;
    }
    /** 文件地址 */
    public String getFileUrl(){
        return this.fileUrl;
    }
    /** 文件地址 */
    public void setFileUrl(String fileUrl){
        this.fileUrl = fileUrl;
    }
    /** 文件压缩地址 */
    public String getThumbnailUrl(){
        return this.thumbnailUrl;
    }
    /** 文件压缩地址 */
    public void setThumbnailUrl(String thumbnailUrl){
        this.thumbnailUrl = thumbnailUrl;
    }
    /** 文件大小 */
    public long getSize(){
        return this.size;
    }
    /** 文件大小 */
    public void setSize(long size){
        this.size = size;
    }
}