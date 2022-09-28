package com.key.win.mybaties.feign.fallback;

import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.mybaties.feign.MybatiesFeignClient;
import com.key.win.mybaties.vo.MybatiesFeignTemplateVo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MybatiesFeignClientFallbackFactory implements FallbackFactory<MybatiesFeignClient> {
    @Override
    public MybatiesFeignClient create(Throwable throwable) {
        return new MybatiesFeignClient() {

            public PageResult<MybatiesFeignTemplateVo> getMybatiesFeignTemplateByPaged(PageRequest<MybatiesFeignTemplateVo> t) {
                log.error("查询列表异常:", throwable);
                return new PageResult<MybatiesFeignTemplateVo>();
            }


            public Result saveOrUpdateMybatiesFeignTemplate(MybatiesFeignTemplateVo mybatiesTemplate) {
                log.error("保存或更新异常:", throwable);
                return Result.failed("操作失败！");
            }

            public MybatiesFeignTemplateVo get(String id) {
                log.error("根据id查询异常:", throwable);
                return new MybatiesFeignTemplateVo();
            }


            public Result delete(String id) {
                log.error("根据id删除记录失败:", throwable);
                return Result.failed("操作失败！！");
            }

            @Override
            public List<MybatiesFeignTemplateVo> getMybatiesFeignTemplateByCondition(MybatiesFeignTemplateVo mybatiesTemplate) {
                log.error("查询异常：", throwable);
                return new ArrayList<>();
            }

            @Override
            public List<MybatiesFeignTemplateVo> getMybatiesFeignTemplateByLike(MybatiesFeignTemplateVo mybatiesTemplate) {
                log.error("查询异常！！");
                return new ArrayList<>();
            }
        };
    }
}
