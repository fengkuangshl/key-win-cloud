package com.key.win.activiti.feign;

import com.key.win.activiti.feign.fallback.UserFeignClientFallbackFactory;
import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.feign.FeignExceptionConfig;
import com.key.win.common.model.system.SysUser;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@FeignClient(value = "user-center", configuration = FeignExceptionConfig.class, fallbackFactory = UserFeignClientFallbackFactory.class, decode404 = true)
public interface UserFeignClient {

    /**
     * feign rpc访问远程/getUserByGroupId/{groupId}
     *
     * @param groupId
     * @return
     */
    @GetMapping(value = "/user/getUserByGroupId/{groupId}")
    Result<List<SysUser>> getUserByGroupId(@PathVariable String groupId);


    @GetMapping(value = "/user/getUserByGroupCode/{groupCode}")
    Result<List<SysUser>> getUserByGroupCode(@PathVariable String groupCode);
}
