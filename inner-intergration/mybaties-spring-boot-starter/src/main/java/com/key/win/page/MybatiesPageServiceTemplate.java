package com.key.win.page;
import com.key.win.common.web.OrderDir;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.key.win.common.web.CodeEnum;

/**
 * @param <T> 输入参数的泛型类型
 * @param <RT> 输出参数的泛型类型
 */
public abstract class MybatiesPageServiceTemplate<T, RT> {

    private final static String DEFALUT_ORDER_NAME = "createDate";

    private final BaseMapper baseMapper;

	/**
	 *
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
        Wrapper<RT> wrapper = this.constructWrapper(pageParam.getT());
        //进行分页
        String sql = wrapper.getSqlSelect();
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
     * @param t
     * @return Predicate
     */
    abstract protected Wrapper<RT> constructWrapper(T t);


}