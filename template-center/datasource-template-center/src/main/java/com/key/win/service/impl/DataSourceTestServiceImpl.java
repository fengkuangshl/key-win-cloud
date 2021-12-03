package com.key.win.service.impl;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.key.win.common.util.BeanUtils;
import com.key.win.common.web.Result;
import com.key.win.dao.DataSourceTestDao;
import com.key.win.model.DataSourceTest;
import com.key.win.page.MybatiesPageServiceTemplate;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.service.DataSourceTestService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DataSourceTestServiceImpl extends ServiceImpl<DataSourceTestDao, DataSourceTest> implements DataSourceTestService {
    @Resource
    private DataSourceTestDao dataSourceTestDao;

    @Override
    public Result saveOrUpdateDataSourceTemplate(DataSourceTest dataSourceTest) {
        DataSourceTest po = null;
        if (StringUtils.isNotBlank(dataSourceTest.getId())) {
            po = this.getById(dataSourceTest.getId());
            //BeanUtils.copyProperties(jpaTemplate,po);
        } else {
            //BeanUtils.copyProperties(jpaTemplate,po);
            po = new DataSourceTest();
            //po = jpaTemplate;
        }
        BeanUtils.copyPropertiesToPartField(dataSourceTest, po);
        boolean b = this.saveOrUpdate(po);
        return b ? Result.succeed("操作成功") : Result.succeed("操作失败");
    }

    @Override
    public DataSourceTest get(String id) {
        DataSourceTest dataSourceTest = dataSourceTestDao.selectById(id);
        if (dataSourceTest == null) {
            return new DataSourceTest();
        }
        return dataSourceTest;

    }

    @Override
    public PageResult<DataSourceTest> findDataSourceTemplateByPage(PageRequest<DataSourceTest> pageRequest) {
        MybatiesPageServiceTemplate<DataSourceTest, DataSourceTest> page = new MybatiesPageServiceTemplate<DataSourceTest, DataSourceTest>(this.baseMapper) {
            @Override
            protected AbstractWrapper constructWrapper(DataSourceTest dataSourceTest) {
                LambdaQueryWrapper<DataSourceTest> lqw = new LambdaQueryWrapper<DataSourceTest>();
                if (dataSourceTest != null && StringUtils.isNotBlank(dataSourceTest.getName())) {
                    lqw.like(DataSourceTest::getName, dataSourceTest.getName() == null ? "" : dataSourceTest.getName());
                }
                if (dataSourceTest != null && StringUtils.isNotBlank(dataSourceTest.getPassword())) {
                    lqw.like(DataSourceTest::getPassword, dataSourceTest.getPassword() == null ? "" : dataSourceTest.getPassword());
                }
                if (dataSourceTest != null && StringUtils.isNotBlank(dataSourceTest.getUrl())) {
                    lqw.like(DataSourceTest::getUrl, dataSourceTest.getUrl() == null ? "" : dataSourceTest.getUrl());
                }
                if (dataSourceTest != null && StringUtils.isNotBlank(dataSourceTest.getDriverClassName())) {
                    lqw.like(DataSourceTest::getDriverClassName, dataSourceTest.getDriverClassName() == null ? "" : dataSourceTest.getDriverClassName());
                }
                return lqw;
            }
        };
        PageResult<DataSourceTest> dataSourceTestPageResult = page.doPagingQuery(pageRequest);

        PageResult<DataSourceTest> dataSourcePageResultVo = new PageResult<>();
        dataSourcePageResultVo.setPageNo(dataSourceTestPageResult.getPageNo());
        dataSourcePageResultVo.setPageSize(dataSourceTestPageResult.getPageSize());
        dataSourcePageResultVo.setCount(dataSourceTestPageResult.getCount());
        dataSourcePageResultVo.setData(this.list());
        return dataSourcePageResultVo;
    }

    @Override
    public Result delete(String id) {
        boolean b = this.removeById(id);
        return b ? Result.succeed("操作成功") : Result.succeed("操作失败");
    }
}
