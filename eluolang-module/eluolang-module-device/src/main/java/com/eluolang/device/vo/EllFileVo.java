package com.eluolang.device.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllFileVo {
    private EllGradeVideoVo ellGradeVideo;
    private List<String> FilePath;
    private List<String> FIleName;
    private List<String> videoId;
    private List<EllGradeVideoVo> list;
}
