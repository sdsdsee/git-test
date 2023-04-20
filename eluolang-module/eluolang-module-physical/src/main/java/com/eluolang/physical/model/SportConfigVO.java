package com.eluolang.physical.model;

import com.eluolang.physical.pojo.EllSportConfig;
import com.eluolang.physical.pojo.EllSportConfigRule;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class SportConfigVO extends EllSportConfig {
	
	private static final long serialVersionUID = 1L;
	
	private List<RuleVO> ruleList;
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public static class RuleVO extends EllSportConfigRule {
		
		private static final long serialVersionUID = 1L;
		
		private String proKey;
		
		
	}
	
	
	
	

}
