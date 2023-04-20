package com.eluolang.physical.feign.FeignFallBack;

import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.web.Result;
import com.eluolang.physical.feign.PlayGroundServiceFeign;
import com.eluolang.physical.model.EllClassHistoryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class PlayGroundServiceFeignFallBack implements PlayGroundServiceFeign {

    @Override
    public Result addHistory(List<EllClassHistoryVo> historys) {
        return new Result(HttpStatus.ERROR);
    }
}
