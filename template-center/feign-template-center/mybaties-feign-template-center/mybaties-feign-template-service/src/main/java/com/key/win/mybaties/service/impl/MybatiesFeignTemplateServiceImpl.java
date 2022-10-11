package com.key.win.mybaties.service.impl;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.key.win.common.util.BeanUtils;
import com.key.win.common.web.Result;
import com.key.win.mybaties.dao.MybatiesFeignTemplateDao;
import com.key.win.mybaties.model.MybatiesFeignTemplate;
import com.key.win.mybaties.service.MybatiesFeignTemplateService;
import com.key.win.mybaties.vo.MybatiesFeignTemplateVo;
import com.key.win.mybatis.page.MybatisPageServiceTemplate;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MybatiesFeignTemplateServiceImpl extends ServiceImpl<MybatiesFeignTemplateDao, MybatiesFeignTemplate> implements MybatiesFeignTemplateService {

    private static final Logger log = LoggerFactory.getLogger(MybatiesFeignTemplateServiceImpl.class);


    @Override
    public Result delete(String id) {
        boolean b = this.removeById(id);
        return b ? Result.succeed("操作成功") : Result.succeed("操作失败");
    }

    public Result saveOrUpdateMybatiesFeignTemplate(MybatiesFeignTemplateVo mybatiesTemplate){
        MybatiesFeignTemplate po = null;
        if (StringUtils.isNotBlank(mybatiesTemplate.getId())) {
            po = this.getById(mybatiesTemplate.getId());
            //BeanUtils.copyProperties(jpaTemplate,po);
        } else {
            //BeanUtils.copyProperties(jpaTemplate,po);
            po = new MybatiesFeignTemplate();
            //po = jpaTemplate;
        }
        BeanUtils.copyPropertiesToPartField(mybatiesTemplate, po);
        boolean b = this.saveOrUpdate(po);
        return  b ? Result.succeed("操作成功"):Result.succeed("操作失败");
    }

    public MybatiesFeignTemplateVo get( String id) {

        MybatiesFeignTemplate byId = this.getById(id);
        MybatiesFeignTemplateVo mybatiesFeignTemplateVo = new MybatiesFeignTemplateVo();
        BeanUtils.copyProperties(byId,mybatiesFeignTemplateVo);
        return mybatiesFeignTemplateVo;
    }

    @Override
    public List<MybatiesFeignTemplateVo> getMybatiesFeignTemplateByCondition(String name, String code) {
        Map<String,Object> map = new HashMap<String,Object>();
        if(StringUtils.isNotBlank(name)){
            map.put("name",name);
        }
        if(StringUtils.isNotBlank(code)){
            map.put("code",code);
        }
        List<MybatiesFeignTemplate> mybatiesFeignTemplates = this.baseMapper.selectByMap(map);
        return  this.mybatiesFeignTemplateToVos(mybatiesFeignTemplates);
    }

    private List<MybatiesFeignTemplateVo> mybatiesFeignTemplateToVos(List<MybatiesFeignTemplate> mybatiesFeignTemplates){
        List<MybatiesFeignTemplateVo> mybatiesFeignTemplateVos = new ArrayList<>();
        for (MybatiesFeignTemplate mft: mybatiesFeignTemplates ) {
            MybatiesFeignTemplateVo vo = new MybatiesFeignTemplateVo();
            BeanUtils.copyProperties(mft,vo);
            mybatiesFeignTemplateVos.add(vo);
        }
        return mybatiesFeignTemplateVos;
    }

    @Override
    public List<MybatiesFeignTemplateVo> getMybatiesFeignTemplateByLike(String name) {
        List<MybatiesFeignTemplate> mybatiesFeignTemplates = this.baseMapper.selectList(new QueryWrapper<MybatiesFeignTemplate>().like("name", name));
        return  this.mybatiesFeignTemplateToVos(mybatiesFeignTemplates);
    }

    public PageResult<MybatiesFeignTemplateVo> findMybatiesFeignTemplateByPaged(PageRequest<MybatiesFeignTemplateVo> pageRequest) {
        MybatisPageServiceTemplate<MybatiesFeignTemplateVo, MybatiesFeignTemplate> page = new MybatisPageServiceTemplate<MybatiesFeignTemplateVo, MybatiesFeignTemplate>(this.baseMapper) {
            @Override
            protected AbstractWrapper constructWrapper(MybatiesFeignTemplateVo mybatiesTemplate) {
                LambdaQueryWrapper<MybatiesFeignTemplate> lqw = new LambdaQueryWrapper<MybatiesFeignTemplate>();
                if(mybatiesTemplate != null && StringUtils.isNotBlank(mybatiesTemplate.getName())){
                    lqw.like(MybatiesFeignTemplate::getName, mybatiesTemplate.getName() == null? "":mybatiesTemplate.getName());
                }
                if(mybatiesTemplate != null && StringUtils.isNotBlank(mybatiesTemplate.getCode())){
                    lqw.like(MybatiesFeignTemplate::getCode, mybatiesTemplate.getCode() == null? "":mybatiesTemplate.getCode().toUpperCase());
                }

                lqw.orderByDesc(MybatiesFeignTemplate::getCreateDate);
                return lqw;
            }
        };
        PageResult<MybatiesFeignTemplate> result = page.doPagingQuery(pageRequest);

        PageResult<MybatiesFeignTemplateVo> mybatiesPageResultVo = new PageResult<>();
        mybatiesPageResultVo.setCode(result.getCode());
        mybatiesPageResultVo.setPageNo(result.getPageNo());
        mybatiesPageResultVo.setPageSize(result.getPageSize());
        mybatiesPageResultVo.setCount(result.getCount());
        mybatiesPageResultVo.setData(this.mybatiesFeignTemplateToVos(result.getData()));
        return mybatiesPageResultVo;
    }
}
