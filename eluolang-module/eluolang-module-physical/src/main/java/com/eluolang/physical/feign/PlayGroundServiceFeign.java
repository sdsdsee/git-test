package com.eluolang.physical.feign;

import com.eluolang.common.core.web.Result;
import com.eluolang.physical.feign.FeignFallBack.ELLSocketServerFeignFallBack;
import com.eluolang.physical.feign.FeignFallBack.PlayGroundServiceFeignFallBack;
import com.eluolang.physical.model.EllClassHistoryVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "ELL-smart-playground", fallback = PlayGroundServiceFeignFallBack.class)
public interface PlayGroundServiceFeign {
    @PostMapping("/classroom/submitScores")
    public Result addHistory(@RequestBody List<EllClassHistoryVo> historys);
}
