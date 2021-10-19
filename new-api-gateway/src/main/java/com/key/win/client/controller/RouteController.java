package com.key.win.client.controller;

import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.client.dto.GatewayRouteDefinition;
import com.key.win.client.service.DynamicRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/route")
@SuppressWarnings("all")
public class RouteController {

    @Autowired
    private DynamicRouteService dynamicRouteService;

    //增加路由
    @PostMapping("/add")
    public Mono<Result> add(@RequestBody GatewayRouteDefinition gatewayRouteDefinition) {
        return Mono.just(Result.succeed(dynamicRouteService.add(gatewayRouteDefinition)));
    }

    //更新路由
    @PostMapping("/update")
    public Mono<Result> update(@RequestBody GatewayRouteDefinition gatewayRouteDefinition) {
        return Mono.just(Result.succeed(dynamicRouteService.update(gatewayRouteDefinition)));
    }

    //删除路由
    @DeleteMapping("/{id}")
    public Mono<Result> delete(@PathVariable String id) {
        return Mono.just(Result.succeed(dynamicRouteService.delete(id)));
    }

    //获取全部数据
    @GetMapping("/findAll")
    public Mono<PageResult> findAll(@RequestParam Map<String, Object> params) {
        return Mono.just(dynamicRouteService.findAll(params));
    }

    //同步redis数据 从mysql中同步过去
    @GetMapping("/synchronization")
    public Mono<Result> synchronization() {
        return Mono.just(Result.succeed(dynamicRouteService.synchronization()));
    }


    //修改路由状态
    @GetMapping("/updateFlag")
    public Mono<Result> updateFlag(@RequestParam Map<String, Object> params) {
        return Mono.just(Result.succeed(dynamicRouteService.updateFlag(params)));
    }


}
