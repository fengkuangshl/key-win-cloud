package com.key.win.jpa.service.impl;

import com.key.win.common.util.BeanUtils;
import com.key.win.common.web.Result;
import com.key.win.jpa.model.JpaFeignTemplate;
import com.key.win.jpa.page.JpaPageServiceTemplate;
import com.key.win.jpa.repository.JpaFeignTemplateRepository;
import com.key.win.jpa.service.JpaFeignTemplateService;
import com.key.win.jpa.vo.JpaFeignTemplateVo;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class JpaFeignTemplateServiceImpl implements JpaFeignTemplateService {

    @Autowired
    private JpaFeignTemplateRepository jpaTemplateRepostiory;

    @Override
    public JpaFeignTemplateVo get(String id) {
        JpaFeignTemplate one = jpaTemplateRepostiory.getOne(id);
        JpaFeignTemplateVo vo = new JpaFeignTemplateVo();
        BeanUtils.copyProperties(one,vo);
        return  vo;
    }

    @Override
    public Result saveOrUpdateJpaFeignTemplate(JpaFeignTemplateVo jpaTemplate) {
        JpaFeignTemplate po = null;
        if (StringUtils.isNotBlank(jpaTemplate.getId())) {
            po = jpaTemplateRepostiory.getOne(jpaTemplate.getId());
            //BeanUtils.copyProperties(jpaTemplate,po);
        } else {
            //BeanUtils.copyProperties(jpaTemplate,po);
            po = new JpaFeignTemplate();
        }
        BeanUtils.copyPropertiesToPartField(jpaTemplate, po);
        jpaTemplateRepostiory.save(po);
        return Result.succeed("保存成功");
    }

    @Override
    public Result delete(String id) {
        jpaTemplateRepostiory.deleteById(id);
        return Result.succeed("删除成功");
    }

    @Override
    public PageResult<JpaFeignTemplateVo> getJpaFeignTemplateByPaged(PageRequest<JpaFeignTemplateVo> t) {
        JpaPageServiceTemplate<JpaFeignTemplateVo, JpaFeignTemplate> q = new JpaPageServiceTemplate<JpaFeignTemplateVo, JpaFeignTemplate>(jpaTemplateRepostiory) {
            @Override
            protected Predicate constructPredicate(Root<JpaFeignTemplateVo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder, JpaFeignTemplateVo jpaTemplate) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isNotBlank(jpaTemplate.getName())) {
                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + jpaTemplate.getName() + "%"));
                }
                if (StringUtils.isNotBlank(jpaTemplate.getCode())) {
                    predicates.add(criteriaBuilder.equal(root.get("code"), jpaTemplate.getCode()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        PageResult<JpaFeignTemplate> jpaFeignTemplateJpaPageResult = q.doPagingQuery(t);

        PageResult<JpaFeignTemplateVo> jpaFeignTemplateJpaPageResultVo = new PageResult<>();
        jpaFeignTemplateJpaPageResultVo.setCode(jpaFeignTemplateJpaPageResult.getCode());
        jpaFeignTemplateJpaPageResultVo.setPageNo(jpaFeignTemplateJpaPageResult.getPageNo());
        jpaFeignTemplateJpaPageResultVo.setPageSize(jpaFeignTemplateJpaPageResult.getPageSize());
        jpaFeignTemplateJpaPageResultVo.setCount(jpaFeignTemplateJpaPageResult.getCount());
        jpaFeignTemplateJpaPageResultVo.setData(this.jpaFeignTemplateToVo(jpaFeignTemplateJpaPageResult.getData()));
        return jpaFeignTemplateJpaPageResultVo;


    }

    private List<JpaFeignTemplateVo> jpaFeignTemplateToVo(List<JpaFeignTemplate> jpaFeignTemplates) {
        List<JpaFeignTemplateVo> jpaFeignTemplateVos = new ArrayList<>();
        if (jpaFeignTemplates != null && jpaFeignTemplates.size() > 0) {
            for (JpaFeignTemplate jft : jpaFeignTemplates) {
                JpaFeignTemplateVo vo = new JpaFeignTemplateVo();
                BeanUtils.copyProperties(jft, vo);
                jpaFeignTemplateVos.add(vo);
            }
        }
        return jpaFeignTemplateVos;
    }
}
