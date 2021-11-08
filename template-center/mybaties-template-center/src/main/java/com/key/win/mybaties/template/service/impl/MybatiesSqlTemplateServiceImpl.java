package com.key.win.mybaties.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.mybaties.template.dao.MybatiesSqlTemplateDao;
import com.key.win.mybaties.template.dao.MybatiesTemplateDao;
import com.key.win.mybaties.template.model.MybatiesTemplate;
import com.key.win.mybaties.template.service.MybatiesSqlTemplateService;
import com.key.win.mybaties.template.service.MybatiesTemplateService;
import com.key.win.page.MybatiesPageServiceTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MybatiesSqlTemplateServiceImpl implements MybatiesSqlTemplateService {

    private static final Logger log = LoggerFactory.getLogger(MybatiesSqlTemplateServiceImpl.class);

    @Autowired
    private MybatiesSqlTemplateDao mybatiesSqlTemplateDao;

    @Override
    public List<Map<String,Object>> getAll() {
        return mybatiesSqlTemplateDao.getAll();
    }
}
