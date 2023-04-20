package com.eluolang.playground.util;

/**
 * @author dengrunsen
 * @date 2022年10月24日 17:31
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 工具类，用来加载配置参数
 */
@Component(value = "runningConfig")
public class RunningConfig {
    @Value("${run.cp800.location}")
    private String cp800Location;
    @Value("${run.cp800.num}")
    private Integer cp800Num;

    @Value("${run.cp1000.start.location}")
    private String cp1000StartLocation;
    @Value("${run.cp1000.start.num}")
    private Integer cp1000StartNum;
    @Value("${run.cp1000.end.location}")
    private String cp1000EndLocation;
    @Value("${run.cp1000.end.num}")
    private Integer cp1000EndNum;

    @Value("${run.cp2000.location}")
    private String cp2000Location;
    @Value("${run.cp2000.num}")
    private Integer cp2000Num;

    @Value("${run.cp3000.start.location}")
    private String cp3000StartLocation;
    @Value("${run.cp3000.start.num}")
    private Integer cp3000StartNum;
    @Value("${run.cp3000.end.location}")
    private String cp3000EndLocation;
    @Value("${run.cp3000.end.num}")
    private Integer cp3000EndNum;

    @Value("${run.cp5000.start.location}")
    private String cp5000StartLocation;
    @Value("${run.cp5000.start.num}")
    private Integer cp5000StartNum;
    @Value("${run.cp5000.end.location}")
    private String cp5000EndLocation;
    @Value("${run.cp5000.end.num}")
    private Integer cp5000EndNum;

    public String getCp800Location() {
        return cp800Location;
    }

    public void setCp800Location(String cp800Location) {
        this.cp800Location = cp800Location;
    }

    public Integer getCp800Num() {
        return cp800Num;
    }

    public void setCp800Num(Integer cp800Num) {
        this.cp800Num = cp800Num;
    }

    public String getCp1000StartLocation() {
        return cp1000StartLocation;
    }

    public void setCp1000StartLocation(String cp1000StartLocation) {
        this.cp1000StartLocation = cp1000StartLocation;
    }

    public Integer getCp1000StartNum() {
        return cp1000StartNum;
    }

    public void setCp1000StartNum(Integer cp1000StartNum) {
        this.cp1000StartNum = cp1000StartNum;
    }

    public String getCp1000EndLocation() {
        return cp1000EndLocation;
    }

    public void setCp1000EndLocation(String cp1000EndLocation) {
        this.cp1000EndLocation = cp1000EndLocation;
    }

    public Integer getCp1000EndNum() {
        return cp1000EndNum;
    }

    public void setCp1000EndNum(Integer cp1000EndNum) {
        this.cp1000EndNum = cp1000EndNum;
    }

    public String getCp2000Location() {
        return cp2000Location;
    }

    public void setCp2000Location(String cp2000Location) {
        this.cp2000Location = cp2000Location;
    }

    public Integer getCp2000Num() {
        return cp2000Num;
    }

    public void setCp2000Num(Integer cp2000Num) {
        this.cp2000Num = cp2000Num;
    }

    public String getCp3000StartLocation() {
        return cp3000StartLocation;
    }

    public void setCp3000StartLocation(String cp3000StartLocation) {
        this.cp3000StartLocation = cp3000StartLocation;
    }

    public Integer getCp3000StartNum() {
        return cp3000StartNum;
    }

    public void setCp3000StartNum(Integer cp3000StartNum) {
        this.cp3000StartNum = cp3000StartNum;
    }

    public String getCp3000EndLocation() {
        return cp3000EndLocation;
    }

    public void setCp3000EndLocation(String cp3000EndLocation) {
        this.cp3000EndLocation = cp3000EndLocation;
    }

    public Integer getCp3000EndNum() {
        return cp3000EndNum;
    }

    public void setCp3000EndNum(Integer cp3000EndNum) {
        this.cp3000EndNum = cp3000EndNum;
    }

    public String getCp5000StartLocation() {
        return cp5000StartLocation;
    }

    public void setCp5000StartLocation(String cp5000StartLocation) {
        this.cp5000StartLocation = cp5000StartLocation;
    }

    public Integer getCp5000StartNum() {
        return cp5000StartNum;
    }

    public void setCp5000StartNum(Integer cp5000StartNum) {
        this.cp5000StartNum = cp5000StartNum;
    }

    public String getCp5000EndLocation() {
        return cp5000EndLocation;
    }

    public void setCp5000EndLocation(String cp5000EndLocation) {
        this.cp5000EndLocation = cp5000EndLocation;
    }

    public Integer getCp5000EndNum() {
        return cp5000EndNum;
    }

    public void setCp5000EndNum(Integer cp5000EndNum) {
        this.cp5000EndNum = cp5000EndNum;
    }
}
