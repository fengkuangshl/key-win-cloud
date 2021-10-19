package com.key.win.jpa.template.service.impl;

import com.key.win.common.util.BeanUtils;
import com.key.win.common.web.Result;
import com.key.win.jpa.page.JpaPageServiceTemplate;
import com.key.win.jpa.template.model.JpaTemplate;
import com.key.win.jpa.template.repository.JpaTemplateRepository;
import com.key.win.jpa.template.service.JpaTemplateService;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class JpaTemplateServiceImpl implements JpaTemplateService {

    @Autowired
    private JpaTemplateRepository jpaTemplateRepostiory;

    @Override
    public JpaTemplate get(String id) {
        return jpaTemplateRepostiory.getOne(id);
    }

    @Override
    public Result saveOrUpdateJpaTemplate(JpaTemplate jpaTemplate) {
        JpaTemplate po = null;
        if(StringUtils.isNotBlank(jpaTemplate.getId())){
            po = this.get(jpaTemplate.getId());
            BeanUtils.copyPropertiesToPartField(jpaTemplate,po);
        }else {
            po = jpaTemplate;
        }
        jpaTemplateRepostiory.save(po);
        return Result.succeed("保存成功");
    }

    @Override
    public Result delete(String id) {
        jpaTemplateRepostiory.deleteById(id);
        return Result.succeed("删除成功");
    }

    @Override
    public PageResult<JpaTemplate> getJpaTemplateByPaged(PageRequest<JpaTemplate> t) {
        JpaPageServiceTemplate<JpaTemplate,JpaTemplate> q = new JpaPageServiceTemplate<JpaTemplate, JpaTemplate>(jpaTemplateRepostiory) {
            @Override
            protected Predicate constructPredicate(Root<JpaTemplate> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder, JpaTemplate jpaTemplate) {
                List<Predicate> predicates = new ArrayList<>();
                if(StringUtils.isNotBlank(jpaTemplate.getName())){
                    predicates.add(criteriaBuilder.like(root.get("name"),"%"+jpaTemplate.getName()+"%"));
                }
                if(StringUtils.isNotBlank(jpaTemplate.getCode())){
                    predicates.add(criteriaBuilder.equal(root.get("code"),jpaTemplate.getCode()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return q.doPagingQuery(t);
    }

    @Cacheable("getCacheTest")
    public String getCacheTest(){
        return "this is good boy!!";
    }
}
