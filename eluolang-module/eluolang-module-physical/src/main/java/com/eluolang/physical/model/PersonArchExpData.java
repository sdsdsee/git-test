package com.eluolang.physical.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.deepoove.poi.data.TableRenderData;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 个人导出档案
 * @author: zerolifes
 * @date:   2021-11-02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonArchExpData implements Serializable{
	
	
	
	/**   
	 * @Fields serialVersionUID : TODO(这个变量表示什么)   
	 */  
	private static final long serialVersionUID = 1L;
	/**
	 * 自定义表格
	 */
	private TableRenderData order;
	private String year;
	private User u;
	private Res r;
	private Object rardar;//图形数据
	private List<SportTxt> sections;
	private Map<String,PhyData> objMap;
	
	@Data
	@Builder
	public static class User{
		private String name;//姓名
		private String gcName;//年级班级名称
		private String stuNum;//学号
	}
	
	@Data
	@Builder
	public static class Res{
		private String score;//总分
		private String remark;//总评
	}
	
	@Data
	@Builder
	public static class SportTxt{
		private Object title;//运动指南
		private Object txt;//建议
	}
	

}
