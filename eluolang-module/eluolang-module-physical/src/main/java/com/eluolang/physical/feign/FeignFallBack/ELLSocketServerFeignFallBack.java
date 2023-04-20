package com.eluolang.physical.feign.FeignFallBack;

import com.eluolang.common.core.constant.HttpStatus;
import com.eluolang.common.core.web.Result;
import com.eluolang.physical.feign.ELLSocketServerFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
@Slf4j
public class ELLSocketServerFeignFallBack implements ELLSocketServerFeign {
    @Override
    public Result sendToAll(String message) throws IOException {
        return new Result(HttpStatus.ERROR);
    }

}
