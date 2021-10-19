package com.key.win.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.web.Result;
import com.key.win.model.DataSourceTest;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;

public interface DataSourceTestService extends IService<DataSourceTest> {
    Result saveOrUpdateDataSourceTemplate(DataSourceTest dataSourceTest);

    DataSourceTest get(String id);

    PageResult<DataSourceTest> findDataSourceTemplateByPage(PageRequest<DataSourceTest> pageRequest);

    Result delete(String id);
}
