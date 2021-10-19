package com.key.win.mybaties.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.key.win.mybaties.template.dao.MybatiesTemplateDao;
import com.key.win.mybaties.template.model.MybatiesTemplate;
import com.key.win.mybaties.template.service.MybatiesTemplateService;
import com.key.win.page.MybatiesPageServiceTemplate;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MybatiesTemplateServiceImpl extends ServiceImpl<MybatiesTemplateDao, MybatiesTemplate> implements MybatiesTemplateService {

    private static final Logger log = LoggerFactory.getLogger(MybatiesTemplateServiceImpl.class);



    @Cacheable("getCacheTest")
    public String getCacheTest(){
        return "this is good boy!!";
    }

    @Override
    public List<MybatiesTemplate> getMybatiesTemplateByCondition(String name,String code) {
        Map<String,Object> map = new HashMap<String,Object>();
        if(StringUtils.isNotBlank(name)){
            map.put("name",name);
        }
        if(StringUtils.isNotBlank(code)){
            map.put("code",code);
        }
        return this.baseMapper.selectByMap(map);
    }

    @Override
    public List<MybatiesTemplate> getMybatiesTemplateByLike(String name) {
        return  this.baseMapper.selectList(new QueryWrapper<MybatiesTemplate>().like("name",name));
    }

    public PageResult<MybatiesTemplate> findMybatiesTemplateByPaged(PageRequest<MybatiesTemplate> pageRequest) {
        MybatiesPageServiceTemplate<MybatiesTemplate, MybatiesTemplate> page = new MybatiesPageServiceTemplate<MybatiesTemplate, MybatiesTemplate>(this.baseMapper) {
            @Override
            protected Wrapper<MybatiesTemplate> constructWrapper(MybatiesTemplate mybatiesTemplate) {
                LambdaQueryWrapper<MybatiesTemplate> lqw = new LambdaQueryWrapper<MybatiesTemplate>();
                if(mybatiesTemplate != null && StringUtils.isNotBlank(mybatiesTemplate.getName())){
                    lqw.like(MybatiesTemplate::getName, mybatiesTemplate.getName() == null? "":mybatiesTemplate.getName());
                }
                if(mybatiesTemplate != null && StringUtils.isNotBlank(mybatiesTemplate.getCode())){
                    lqw.like(MybatiesTemplate::getCode, mybatiesTemplate.getCode() == null? "":mybatiesTemplate.getCode().toUpperCase());
                }

                lqw.orderByDesc(MybatiesTemplate::getCreateDate);
                return lqw;
            }
        };
        return  page.doPagingQuery(pageRequest);
    }
}
