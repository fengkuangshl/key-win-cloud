package com.key.win.page;

import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.key.win.common.util.StringUtil;
import com.key.win.common.web.CodeEnum;
import com.key.win.common.web.OrderDir;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T>  输入参数的泛型类型
 * @param <RT> 输出参数的泛型类型
 */
public abstract class MybatiesPageServiceTemplate<T, RT> {

    private final static String DEFALUT_ORDER_NAME = "createDate";

    private final BaseMapper baseMapper;

    /**
     * @param baseMapper
     */
    public MybatiesPageServiceTemplate(BaseMapper baseMapper) {
        super();
        this.baseMapper = baseMapper;
    }

    /**
     * 分页查询模板类，
     *
     * @param pageParam 翻页、排序参数model
     * @return 泛型分页返回值，包含总记录数和当前页List数据
     */
    public PageResult<RT> doPagingQuery(PageRequest<T> pageParam) {

        //设备mybaties查询分页
        Page<RT> page = new Page<RT>();
        page.setCurrent(pageParam.getPageNo());
        page.setSize(pageParam.getPageSize());
        //构建Wrapper
        AbstractWrapper wrapper = this.constructWrapper(pageParam.getT());
        List orderList = new ArrayList();
        List defaultQueryOrder = this.getDefaultQueryOrder(pageParam.getT(), pageParam.getSortName());
        if (defaultQueryOrder != null && defaultQueryOrder.size() > 0) {
            orderList.addAll(defaultQueryOrder);
        } else if (wrapper instanceof AbstractLambdaWrapper) {
            // TODO 目前程序还没有一个好的办法自动装配orderby条件，只能通过重写getDefaultQueryOrder方法来解决LambdaWrapper排序的问题
        } else if (wrapper instanceof QueryWrapper) {
            if (StringUtil.isNotBlank(pageParam.getSortName())) {
                orderList.add(pageParam.getSortName());
            } else {
                orderList.add(DEFALUT_ORDER_NAME);
            }
        }
        if (orderList != null && orderList.size() > 0) {
            wrapper.orderBy(true, pageParam.getSortDir() == OrderDir.ASC, orderList.toArray());
        }
        IPage<RT> pages = baseMapper.selectPage(page, wrapper);
        //设置分页返回参数
        PageResult<RT> pageResult = new PageResult<RT>();
        pageResult.setCount(pages.getTotal());
        pageResult.setPageNo(pageParam.getPageNo());
        pageResult.setPageSize(pageParam.getPageSize());
        pageResult.setData(page.getRecords());
        pageResult.setCode(CodeEnum.SUCCESS.getCode());
        return pageResult;
    }

    /**
     * 供子类OverWrite，根据传入的查询条件构造
     *
     * @param t
     * @return Predicate
     */
    abstract protected AbstractWrapper constructWrapper(T t);


    /**
     * 供子类重写，指定默认的Criteria排序；
     * 默认返回空，框架会按创建时间crtDttm降序
     */
    protected List getDefaultQueryOrder(T t, String sortName) {
        return null;
    }


}