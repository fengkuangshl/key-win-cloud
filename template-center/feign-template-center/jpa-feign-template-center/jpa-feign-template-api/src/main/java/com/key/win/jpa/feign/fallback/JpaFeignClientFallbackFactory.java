package com.key.win.jpa.feign.fallback;

import com.key.win.common.web.Result;
import com.key.win.jpa.feign.JpaFeignClient;
import com.key.win.jpa.vo.JpaFeignTemplateVo;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Component
public class JpaFeignClientFallbackFactory implements FallbackFactory<JpaFeignClient> {
    @Override
    public JpaFeignClient create(Throwable throwable) {
        return new JpaFeignClient() {


            public PageResult<JpaFeignTemplateVo> getJpaFeignTemplateByPaged(@RequestBody PageRequest<JpaFeignTemplateVo> t) {
                log.error("查询列表异常:{}");
                return new PageResult<JpaFeignTemplateVo>();
            }


            public Result saveOrUpdateJpaFeignTemplate(@RequestBody JpaFeignTemplateVo jpaTemplate) {
                log.error("保存或更新异常:{}");
                return Result.failed("操作失败！");
            }

            public JpaFeignTemplateVo get(@PathVariable String id){
                log.error("根据id查询异常:{}");
                return new JpaFeignTemplateVo();
            }



            public Result delete(@PathVariable String id){
                log.error("根据id删除记录失败！！");
                return Result.failed("操作失败！！");
            }
        };
    }
}
