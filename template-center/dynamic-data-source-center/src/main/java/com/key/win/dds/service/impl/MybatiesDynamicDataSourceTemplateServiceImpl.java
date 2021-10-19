package com.key.win.dds.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.dds.dao.MybatiesDynamicDataSourceTemplateDao;
import com.key.win.dds.model.MybatiesDynamicDataSourceTemplate;
import com.key.win.dds.service.MybatiesDynamicDataSourceTemplateService;
import com.key.win.page.MybatiesPageServiceTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MybatiesDynamicDataSourceTemplateServiceImpl extends ServiceImpl<MybatiesDynamicDataSourceTemplateDao, MybatiesDynamicDataSourceTemplate> implements MybatiesDynamicDataSourceTemplateService {
    private static final Logger log = LoggerFactory.getLogger(MybatiesDynamicDataSourceTemplateServiceImpl.class);



    @Cacheable("getCacheTest")
    public String getCacheTest(){
        return "this is good boy!!";
    }

    @Override
    public List<MybatiesDynamicDataSourceTemplate> getMybatiesDynamicDataSourceTemplateByCondition(String name,String code) {
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
    public List<MybatiesDynamicDataSourceTemplate> getMybatiesDynamicDataSourceTemplateByLike(String name) {
        return  this.baseMapper.selectList(new QueryWrapper<MybatiesDynamicDataSourceTemplate>().like("name",name));
    }

    public PageResult<MybatiesDynamicDataSourceTemplate> findMybatiesDynamicDataSourceTemplateByPaged(PageRequest<MybatiesDynamicDataSourceTemplate> pageRequest) {
        MybatiesPageServiceTemplate<MybatiesDynamicDataSourceTemplate, MybatiesDynamicDataSourceTemplate> page = new MybatiesPageServiceTemplate<MybatiesDynamicDataSourceTemplate, MybatiesDynamicDataSourceTemplate>(this.baseMapper) {
            @Override
            protected Wrapper<MybatiesDynamicDataSourceTemplate> constructWrapper(MybatiesDynamicDataSourceTemplate mybatiesTemplate) {
                LambdaQueryWrapper<MybatiesDynamicDataSourceTemplate> lqw = new LambdaQueryWrapper<MybatiesDynamicDataSourceTemplate>();
                if(mybatiesTemplate != null && StringUtils.isNotBlank(mybatiesTemplate.getName())){
                    lqw.like(MybatiesDynamicDataSourceTemplate::getName, mybatiesTemplate.getName() == null? "":mybatiesTemplate.getName());
                }
                if(mybatiesTemplate != null && StringUtils.isNotBlank(mybatiesTemplate.getCode())){
                    lqw.like(MybatiesDynamicDataSourceTemplate::getCode, mybatiesTemplate.getCode() == null? "":mybatiesTemplate.getCode().toUpperCase());
                }

                lqw.orderByDesc(MybatiesDynamicDataSourceTemplate::getCreateDate);
                return lqw;
            }
        };
        return  page.doPagingQuery(pageRequest);
    }
}
