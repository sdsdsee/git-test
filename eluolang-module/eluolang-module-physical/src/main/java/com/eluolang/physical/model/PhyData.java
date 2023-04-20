package com.eluolang.physical.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhyData {
	/**
	 * 项目名称
	 */
	private String name;
	/**
	 * 项目成绩
	 */
	private List<RowData> data;
	/**
	 * 评分
	 */
	private String score;
	/**
	 * 评语
	 */
	private String comment;
	
	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class  RowData{
		/**
		 * 成绩值
		 */
		private String val;
		/**
		 * 名称
		 */
		private String name;
		/**
		 * 是否是最佳项目
		 */
		private boolean ismax;
		/**
		 * 成绩值单位
		 */
		private String unit;
	}
}

