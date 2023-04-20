package com.eluolang.module.socket.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dengrunsen
 * @date 2022年11月25日 17:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaceDataVo {
    private String imageId;
    private String description;
    private Object rect;
}
