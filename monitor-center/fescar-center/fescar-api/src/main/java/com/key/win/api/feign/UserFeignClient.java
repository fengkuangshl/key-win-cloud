package com.key.win.api.feign;

import com.key.win.api.config.NotBreakerConfiguration;
import com.key.win.common.exception.service.ServiceException;
import com.key.win.common.web.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "fescar-user",configuration = NotBreakerConfiguration.class)
public interface UserFeignClient {


    /**
     * 扣款接口 Feign 方式调用
     * @param userId
     * @return
     */
    @RequestMapping(value = "/deductionAmount", method = RequestMethod.GET)
    Result deductionAmount(@RequestParam("userId") String userId) throws ServiceException;


}
