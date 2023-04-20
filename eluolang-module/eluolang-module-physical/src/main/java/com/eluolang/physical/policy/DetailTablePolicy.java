package com.eluolang.physical.policy;

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import com.eluolang.physical.model.PhyData;
import com.eluolang.physical.model.PhyData.RowData;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 个人档案导出：动态表格成绩数据
 * 
 * @author: zerolifes
 * @date: 2021-11-02
 */
@Slf4j
@NoArgsConstructor
public class DetailTablePolicy extends DynamicTableRenderPolicy {

	// 填充数据所在行数
	private static final int lastStartRow = 1;
	// 填充字体颜色
	private static final String textColor = "FFFFFF";
	// 填充无数据
	private static final String noDate = "-";

	private Map<String, PhyData> phyData;

	public DetailTablePolicy(Map<String, PhyData> phyData) {
		this.phyData = phyData;
	}

	@Override
	public void render(XWPFTable table, Object data) throws Exception {
		/* 计算总行数 */
		Long rowTotal = this.phyData.entrySet().stream()
				.collect(Collectors.summarizingInt(p -> p.getValue().getData().size())).getSum();
		/* 当前行号 */
		int rowIndex = 0;
		List<Map<String, int[]>> list = new ArrayList<Map<String, int[]>>();
		for (Map.Entry<String, PhyData> m : this.phyData.entrySet()) {
			PhyData v = m.getValue();
			for (int j = v.getData().size(), i = j; i > 0; i--) {
				RowData rd = v.getData().get(i - 1);
				XWPFTableRow insertNewTableRow = table.insertNewTableRow(lastStartRow);
				for (int a = 0; a < 5; a++)
					insertNewTableRow.createCell();
				// 合并单元格
				if (j > 1 && (i - 1) == 0) {
					// 计算正序 开始行和结束行 。注：插件添加的数据顺序 为 反序；
					int start = rowTotal.intValue() - rowIndex;
					int end = start - 1 + j;
					// 添加需要合并行和 名称
					Map<String, int[]> merge = new HashMap<String, int[]>();
					merge.put(v.getName(), new int[] { start, end });
					list.add(merge);
				}
				rowIndex++;
				String name = StringUtils.isBlank(rd.getName()) ? v.getName() : rd.getName();
				String comment = StringUtils.isBlank(v.getComment()) ? noDate : v.getComment();
				String score = null == v.getScore() ? noDate : String.valueOf(v.getScore());
				RowRenderData rowData = Rows.of(v.getName(), name, rd.getVal() + rd.getUnit(), comment, score)
						.textColor(textColor).center().create();
				try {
					TableRenderPolicy.Helper.renderRow(table.getRow(lastStartRow), rowData);
				} catch (Exception e) {
					log.error("个人测试数据渲染失败！", e);
				}
			}
		}
		// 合并数据行
		list.forEach(m -> {
			m.forEach((k, v) -> {
				TableTools.mergeCellsVertically(table, 4, v[0], v[1]);// 分数
				TableTools.mergeCellsVertically(table, 3, v[0], v[1]);// 评语
				TableTools.mergeCellsVertically(table, 0, v[0], v[1]);// 名称
			});
		});
	}
}
