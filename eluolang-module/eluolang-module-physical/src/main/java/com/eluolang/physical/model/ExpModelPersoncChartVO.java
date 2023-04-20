package com.eluolang.physical.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExpModelPersoncChartVO {
	private List<String> chartTitle;
	private List<Double> chartScore;
}
