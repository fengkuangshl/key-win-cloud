package com.key.win.jpa.query;

import com.key.win.jpa.repository.BaseRepository;
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
 */
public abstract class QueryServiceTemplate<T, RT> {

    public final static String DEFALUT_ORDER_NAME = "createDate";

    private final BaseRepository baseRepository;

    /**
     * @param baseRepository
     */
    public QueryServiceTemplate(BaseRepository baseRepository) {
        super();
        this.baseRepository = baseRepository;
    }

    /**
     * 分页查询模板类，
     *
     * @return 泛型分页返回值，包含总记录数和当前页List数据
     */
    public List<RT> doQuery(T t) {
        //排序方法
        List<Sort.Order> orderList = new ArrayList<Sort.Order>();


        List<Sort.Order> sos = this.getDefaultQueryOrder(t);
        if (sos != null && sos.size() > 0) {
            orderList.addAll(sos);
        } else {
            orderList.add(new Sort.Order(Sort.Direction.DESC, QueryServiceTemplate.DEFALUT_ORDER_NAME));
        }

        Sort sort = Sort.by(orderList);
        Specification<RT> specification = (Specification<RT>) (root, criteriaQuery, criteriaBuilder) -> constructPredicate(root, criteriaQuery, criteriaBuilder, t);
        List<RT> list = baseRepository.findAll(specification, sort);
        return list;
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
     *
     * @param root
     * @param criteriaQuery
     * @param criteriaBuilder
     * @param t
     * @return Predicate
     */
    abstract protected Predicate constructPredicate(Root<RT> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder, T t);

}