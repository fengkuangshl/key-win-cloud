package com.key.win.rpc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.key.win.common.util.BeanUtils;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.mybatis.page.MybatisPageServiceTemplate;
import com.key.win.rpc.dao.MybatiesRpcTemplateDao;
import com.key.win.rpc.model.MybatiesRpcTemplate;
import com.key.win.rpc.mybaties.service.MybatiesRpcTemplateService;
import com.key.win.rpc.mybaties.vo.MybatiesRpcTemplateVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DubboService
public class MybatiesRpcTemplateServiceImpl extends ServiceImpl<MybatiesRpcTemplateDao, MybatiesRpcTemplate> implements MybatiesRpcTemplateService, IService<MybatiesRpcTemplate> {

    private static final Logger log = LoggerFactory.getLogger(MybatiesRpcTemplateServiceImpl.class);


    @Override
    public Result delete(String id) {
        boolean b = this.removeById(id);
        return b ? Result.succeed("操作成功") : Result.succeed("操作失败");
    }

    public Result saveOrUpdateMybatiesRpcTemplate(MybatiesRpcTemplateVo mybatiesTemplate){
        MybatiesRpcTemplate po = null;
        if (StringUtils.isNotBlank(mybatiesTemplate.getId())) {
            po = this.getById(mybatiesTemplate.getId());
            //BeanUtils.copyProperties(jpaTemplate,po);
        } else {
            //BeanUtils.copyProperties(jpaTemplate,po);
            po = new MybatiesRpcTemplate();
            //po = jpaTemplate;
        }
        BeanUtils.copyPropertiesToPartField(mybatiesTemplate, po);
        boolean b = this.saveOrUpdate(po);
        return  b ? Result.succeed("操作成功"):Result.succeed("操作失败");
    }

    public MybatiesRpcTemplateVo get( String id) {

        MybatiesRpcTemplate byId = this.getById(id);
        MybatiesRpcTemplateVo mybatiesRpcTemplateVo = new MybatiesRpcTemplateVo();
        BeanUtils.copyProperties(byId,mybatiesRpcTemplateVo);
        return mybatiesRpcTemplateVo;
    }

    @Override
    public List<MybatiesRpcTemplateVo> getMybatiesRpcTemplateByCondition(String name, String code) {
        Map<String,Object> map = new HashMap<String,Object>();
        if(StringUtils.isNotBlank(name)){
            map.put("name",name);
        }
        if(StringUtils.isNotBlank(code)){
            map.put("code",code);
        }
        List<MybatiesRpcTemplate> mybatiesRpcTemplates = this.baseMapper.selectByMap(map);
        return  this.mybatiesRpcTemplateToVos(mybatiesRpcTemplates);
    }

    private List<MybatiesRpcTemplateVo> mybatiesRpcTemplateToVos(List<MybatiesRpcTemplate> mybatiesRpcTemplates){
        List<MybatiesRpcTemplateVo> mybatiesRpcTemplateVos = new ArrayList<>();
        for (MybatiesRpcTemplate mft: mybatiesRpcTemplates ) {
            MybatiesRpcTemplateVo vo = new MybatiesRpcTemplateVo();
            BeanUtils.copyProperties(mft,vo);
            mybatiesRpcTemplateVos.add(vo);
        }
        return mybatiesRpcTemplateVos;
    }

    @Override
    public List<MybatiesRpcTemplateVo> getMybatiesRpcTemplateByLike(String name) {
        List<MybatiesRpcTemplate> mybatiesRpcTemplates = this.baseMapper.selectList(new QueryWrapper<MybatiesRpcTemplate>().like("name", name));
        return  this.mybatiesRpcTemplateToVos(mybatiesRpcTemplates);
    }

    public PageResult<MybatiesRpcTemplateVo> findMybatiesRpcTemplateByPaged(PageRequest<MybatiesRpcTemplateVo> pageRequest) {
        MybatisPageServiceTemplate<MybatiesRpcTemplateVo, MybatiesRpcTemplate> page = new MybatisPageServiceTemplate<MybatiesRpcTemplateVo, MybatiesRpcTemplate>(this.baseMapper) {
            @Override
            protected LambdaQueryWrapper<MybatiesRpcTemplate> constructWrapper(MybatiesRpcTemplateVo mybatiesTemplate) {
                LambdaQueryWrapper<MybatiesRpcTemplate> lqw = new LambdaQueryWrapper<MybatiesRpcTemplate>();
                if(mybatiesTemplate != null && StringUtils.isNotBlank(mybatiesTemplate.getName())){
                    lqw.like(MybatiesRpcTemplate::getName, mybatiesTemplate.getName() == null? "":mybatiesTemplate.getName());
                }
                if(mybatiesTemplate != null && StringUtils.isNotBlank(mybatiesTemplate.getCode())){
                    lqw.like(MybatiesRpcTemplate::getCode, mybatiesTemplate.getCode() == null? "":mybatiesTemplate.getCode().toUpperCase());
                }

                lqw.orderByDesc(MybatiesRpcTemplate::getCreateDate);
                return lqw;
            }
        };
        PageResult<MybatiesRpcTemplate> result = page.doPagingQuery(pageRequest);

        PageResult<MybatiesRpcTemplateVo> mybatiesPageResultVo = new PageResult<>();
        mybatiesPageResultVo.setCode(result.getCode());
        mybatiesPageResultVo.setPageNo(result.getPageNo());
        mybatiesPageResultVo.setPageSize(result.getPageSize());
        mybatiesPageResultVo.setCount(result.getCount());
        mybatiesPageResultVo.setData(this.mybatiesRpcTemplateToVos(result.getData()));
        return mybatiesPageResultVo;
    }
}
