package com.eluolang.app.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EllTestAppointmentVo implements Serializable, Cloneable {

    /**
     * id
     */
    private Integer id;
    /**
     * 计划id
     */
    private String planId;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 开始时间几点
     */
    private String beginTime;
    /**
     * 结束时间几点
     */
    private String endTime;
    /**
     * 总人数
     */
    private Integer num;
    /**
     * 计划开始时间
     */
    private String dateBegin;
    /**
     * 计划结束时间
     */
    private String dateEnd;
    /**
     * 已预约人数
     */
    private int reservationNum;
    /**
     * 日期
     */
    private String dayTime;

    /**
     * 克隆 实现Cloneable接口
     * @return
     */
    @Override
    public Object clone() {
        EllTestAppointmentVo ellTestAppointmentVo = null;
        try{
            ellTestAppointmentVo = (EllTestAppointmentVo)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return ellTestAppointmentVo;
    }
}