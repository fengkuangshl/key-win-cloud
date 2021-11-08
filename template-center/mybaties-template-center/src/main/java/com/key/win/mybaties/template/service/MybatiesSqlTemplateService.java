package com.key.win.mybaties.template.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.mybaties.template.model.MybatiesTemplate;

import java.util.List;
import java.util.Map;

public interface MybatiesSqlTemplateService {


    public List<Map<String,Object>> getAll();
}
