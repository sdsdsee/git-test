package com.eluolang.device.mapper;

import com.eluolang.common.core.pojo.ToiletEvaluate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 定义数据访问层设备接口方法
 *
 * @author renzhixing
 */
@Mapper
public interface LargeScreenMapper {
    /**
     * 根据设备id查询有权限的管理员信息
     * @param deviceId
     * @return
     */
    List<Integer> selOperIdByDeviceId(String deviceId);

    /**
     * 添加厕所评价记录
     * @param toiletEvaluate
     * @return
     */
    int addToiletEvaluate(ToiletEvaluate toiletEvaluate);

    /**
     * 根据设备id查询最新的20条评价
     * @param deviceId
     * @return
     */
    List<ToiletEvaluate> selToiletEvaluate(String deviceId);

    /**
     * 查询设备各星级评价总数
     * @param deviceId
     * @param level
     * @return
     */
    int selToiletEvaluateByLevel(@Param("deviceId") String deviceId, @Param("level") Integer level);
}
