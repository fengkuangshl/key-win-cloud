package com.key.win.api.feign;

import com.key.win.api.config.NotBreakerConfiguration;
import com.key.win.common.exception.service.ServiceException;
import com.key.win.common.web.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "fescar-item",configuration = NotBreakerConfiguration.class)
public interface ItemFeignClient {


    /**
     * 扣库存接口 Feign 方式调用
     * @param productId
     * @return
     */
    @RequestMapping(value = "/deductInventory", method = RequestMethod.GET)
    Result deductInventory(@RequestParam("productId") String productId) throws ServiceException;



}
