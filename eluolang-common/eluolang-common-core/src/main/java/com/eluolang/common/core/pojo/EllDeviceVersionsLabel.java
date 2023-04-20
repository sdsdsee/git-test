package com.eluolang.common.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllDeviceVersionsLabel {
    private int id;
    private String title;
    private int parentId;
    private String label;
}
