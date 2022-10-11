package com.key.win.mybaties.template.service.impl;

import com.key.win.mybaties.template.dao.MybatiesSqlTemplateDao;
import com.key.win.mybaties.template.service.MybatiesSqlTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
