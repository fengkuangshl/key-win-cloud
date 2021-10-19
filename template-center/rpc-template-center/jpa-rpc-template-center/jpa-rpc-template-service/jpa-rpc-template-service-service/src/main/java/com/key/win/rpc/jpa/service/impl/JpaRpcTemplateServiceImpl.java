package com.key.win.rpc.jpa.service.impl;

import com.key.win.common.util.BeanUtils;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.jpa.page.JpaPageServiceTemplate;
import com.key.win.rpc.jpa.model.JpaRpcTemplate;
import com.key.win.rpc.jpa.repository.JpaRpcTemplateRepository;
import com.key.win.rpc.jpa.service.JpaRpcTemplateService;
import com.key.win.rpc.jpa.vo.JpaRpcTemplateVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@DubboService
public class JpaRpcTemplateServiceImpl implements JpaRpcTemplateService {

    @Autowired
    private JpaRpcTemplateRepository jpaRpcTemplateRepostiory;

    @Override
    public JpaRpcTemplateVo get(String id) {
        JpaRpcTemplate one = jpaRpcTemplateRepostiory.getOne(id);
        JpaRpcTemplateVo vo = new JpaRpcTemplateVo();
        BeanUtils.copyProperties(one,vo);
        return vo;
    }

    @Override
    public Result saveOrUpdateJpaRpcTemplate(JpaRpcTemplateVo jpaTemplate) {
        JpaRpcTemplate po = null;
        if(StringUtils.isNotBlank(jpaTemplate.getId())){
            po = jpaRpcTemplateRepostiory.getOne(jpaTemplate.getId());

        }else {
            po = new JpaRpcTemplate();
        }
        BeanUtils.copyPropertiesToPartField(jpaTemplate,po);
        jpaRpcTemplateRepostiory.save(po);
        return Result.succeed("保存成功");
    }

    @Override
    public Result delete(String id) {
        jpaRpcTemplateRepostiory.deleteById(id);
        return Result.succeed("删除成功");
    }

    @Override
    public PageResult<JpaRpcTemplateVo> findJpaRpcTemplateByPaged(PageRequest<JpaRpcTemplateVo> t) {
        JpaPageServiceTemplate<JpaRpcTemplateVo,JpaRpcTemplate> q = new JpaPageServiceTemplate<JpaRpcTemplateVo,JpaRpcTemplate>(jpaRpcTemplateRepostiory) {
            @Override
            protected Predicate constructPredicate(Root<JpaRpcTemplateVo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder, JpaRpcTemplateVo jpaTemplate) {
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
        PageResult<JpaRpcTemplate> jpaRpcTemplateVoPageResult = q.doPagingQuery(t);

        PageResult<JpaRpcTemplateVo> jpaFeignTemplateJpaPageResultVo = new PageResult<>();
        jpaFeignTemplateJpaPageResultVo.setCode(jpaRpcTemplateVoPageResult.getCode());
        jpaFeignTemplateJpaPageResultVo.setPageNo(jpaRpcTemplateVoPageResult.getPageNo());
        jpaFeignTemplateJpaPageResultVo.setPageSize(jpaRpcTemplateVoPageResult.getPageSize());
        jpaFeignTemplateJpaPageResultVo.setCount(jpaRpcTemplateVoPageResult.getCount());
        jpaFeignTemplateJpaPageResultVo.setData(this.jpaFeignTemplateToVo(jpaRpcTemplateVoPageResult.getData()));
        return jpaFeignTemplateJpaPageResultVo;
    }

    private List<JpaRpcTemplateVo> jpaFeignTemplateToVo(List<JpaRpcTemplate> jpaFeignTemplates) {
        List<JpaRpcTemplateVo> jpaFeignTemplateVos = new ArrayList<>();
        if (jpaFeignTemplates != null && jpaFeignTemplates.size() > 0) {
            for (JpaRpcTemplate jft : jpaFeignTemplates) {
                JpaRpcTemplateVo vo = new JpaRpcTemplateVo();
                BeanUtils.copyProperties(jft, vo);
                jpaFeignTemplateVos.add(vo);
            }
        }
        return jpaFeignTemplateVos;
    }
}
