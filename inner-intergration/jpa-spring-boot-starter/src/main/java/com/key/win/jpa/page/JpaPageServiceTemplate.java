package com.key.win.jpa.page;

import com.key.win.common.web.OrderDir;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.jpa.repository.BaseRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @param <T> 查询条件泛型
 * @param <RT> 分页返回值泛型
 */
public abstract class JpaPageServiceTemplate<T, RT> {

    private final static String DEFALUT_ORDER_NAME = "createDate";

    private final BaseRepository baseRepository;

	/**
	 *
	 * @param baseRepository
	 */
	public JpaPageServiceTemplate(BaseRepository baseRepository) {
        super();
        this.baseRepository = baseRepository;
    }

    /**
     * 分页查询模板类，
     *
     * @param pageParam 翻页、排序参数model
     * @return 泛型分页返回值，包含总记录数和当前页List数据
     */
    public PageResult<RT> doPagingQuery(PageRequest<T> pageParam) {
        PageResult<RT> pageResult = new PageResult<RT>();
        //排序方法
        List<Sort.Order> orderList = new ArrayList<Sort.Order>();

        if (StringUtils.isNotBlank(pageParam.getSortName()) && pageParam.getSortDir() != null) {
            if (pageParam.getSortDir() == OrderDir.ASC) {
                orderList.add(new Sort.Order(Sort.Direction.ASC, pageParam.getSortName()));
            } else {
                orderList.add(new Sort.Order(Sort.Direction.DESC, pageParam.getSortName()));
            }
        } else {// 不指定排序，默认按id降序
            List<Sort.Order> sos = this.getDefaultQueryOrder(pageParam.getT());
            if (sos != null && sos.size() > 0) {
                orderList.addAll(sos);
            } else {
                orderList.add(new Sort.Order(Sort.Direction.DESC, JpaPageServiceTemplate.DEFALUT_ORDER_NAME));
            }
        }
        Sort sort =  Sort.by(orderList);
        Pageable pageable = org.springframework.data.domain.PageRequest.of(pageParam.getHbPageNo(), pageParam.getPageSize(), sort);
        Specification<T> specification = (Specification<T>) (root, criteriaQuery, criteriaBuilder) -> constructPredicate(root, criteriaQuery, criteriaBuilder, pageParam.getT());
        Page<RT> page = baseRepository.findAll(specification, pageable);
        pageResult.setCount(page.getTotalElements());
        pageResult.setPageNo(pageParam.getPageNo());
        pageResult.setPageSize(pageParam.getPageSize());
        pageResult.setData(page.getContent());
        return pageResult;
    }

    /**
     * 供子类重写，指定默认的Criteria排序；
     * 默认返回空，框架会按创建时间crtDttm降序
     */
    protected List<Sort.Order> getDefaultQueryOrder(T t) {
        return null;
    }

    /**
     * 供子类OverWrite，根据传入的查询条件构造
     * @param root
     * @param criteriaQuery
     * @param criteriaBuilder
     * @param t
     * @return Predicate
     */
    abstract protected Predicate constructPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder, T t);

}