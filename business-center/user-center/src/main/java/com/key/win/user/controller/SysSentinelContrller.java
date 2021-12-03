package com.key.win.user.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphO;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.key.win.common.web.Result;
import com.key.win.sentinel.flow.FlowHelper;
import com.key.win.sentinel.flow.FlowType;
import com.key.win.sentinel.flow.Flower;
import com.key.win.sentinel.flow.common.TimeUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@Api(tags = "Sentinel测试")
public class SysSentinelContrller {

	private FlowHelper flowHelper = new FlowHelper(FlowType.Minute);

    @GetMapping("/test")
    public Result<Flower> testApi() {
        try{
            long startTime = TimeUtil.currentTimeMillis();
            // 业务逻辑
            Thread.sleep(1000);
            // 计算耗时
            long rt = TimeUtil.currentTimeMillis() - startTime;
            flowHelper.incrSuccess(rt);
            Flower flower = flowHelper.getFlow(FlowType.Minute);
            System.out.println("总请求数:"+flower.total());
            System.out.println("成功请求数:"+flower.totalSuccess());
            System.out.println("异常请求数:"+flower.totalException());
            System.out.println("平均请求耗时:"+flower.avgRt());
            System.out.println("最大请求耗时:"+flower.maxRt());
            System.out.println("最小请求耗时:"+flower.minRt());
            System.out.println("平均请求成功数(每毫秒):"+flower.successAvg());
            System.out.println("平均请求异常数(每毫秒):"+flower.exceptionAvg());


            return Result.succeed("ok");
        }catch (Exception e){
            flowHelper.incrException();
            return Result.failed("ko");
        }
    }
	/**
	 * 流控规则
	 * @return
	 */
	@GetMapping("/test/sentinelTest")
	public String test(){
		try(Entry entry = SphU.entry("user-center")){

			log.info("ok");
		} catch (BlockException e) {
			return "ko" ;
		}

		return "ok";

	}
	/**
	 * 降级规则
	 * @return
	 */
	@GetMapping("/test/sentinelResource")
	@SentinelResource(value ="user-center" ,blockHandler="doErrorTest1")
	public String test1(){

		try {
			TimeUnit.MILLISECONDS.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("ok");

		return "ok";

	}
	public String doErrorTest1(BlockException e){

		return "ko";

	}


	/**
	 * 热点参数限流
	 */
	@GetMapping("/test/hotParamFlow")
	@ResponseBody
	public 	String hotParamFlow(@RequestParam("prodId") Long prodId,@RequestParam("ip") String ip) {
	    Entry entry = null;
	    String retVal;
	    try{
	        // 只对参数prodId进行限流，参数ip不进行限制

	    	if(1L ==prodId){
	    		entry = SphU.entry("hotParam", EntryType.IN,1,prodId);
	    	}else{
	    		// 不传入任何参数
	    		entry = SphU.entry("hotParam", EntryType.IN,1);
	    	}

	        retVal = "passed";
	    }catch(BlockException e){
	        retVal = "blocked";
	    }finally {
	        if(entry!=null){
	            entry.exit();
	        }
	    }
	    return retVal;
	}

    @RequestMapping("helloWorld11")
    public String helloWorld(){
        try(Entry entry=SphU.entry("HelloWorld")){ // 使用限流规则 HelloWorld
            return "Sentinel 大爷你好！"+System.currentTimeMillis();
        }catch (Exception e){
            e.printStackTrace();
            return "系统繁忙，请稍后！";  // 降级处理
        }
    }

    /**
     * 返回布尔值方式定义资源
     * @return
     */
    @RequestMapping("helloWorld21")
    public String helloWorld2(){
        // 资源名可使用任意有业务语义的字符串
        if (SphO.entry("HelloWorld2")) {
            // 务必保证finally会被执行
            try {
                /**
                 * 被保护的业务逻辑
                 */
                return "Sentinel 大爷你好！boolean"+System.currentTimeMillis();
            } finally {
                SphO.exit();
            }
        } else {
            // 资源访问阻止，被限流或被降级
            // 进行相应的处理操作
            return "系统繁忙，请稍后！";  // 降级处理
        }
    }
}
