package com.key.win.param.service.impl;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.key.win.common.model.baseData.SysParam;
import com.key.win.common.model.baseData.SysParamTree;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.page.MybatiesPageServiceTemplate;
import com.key.win.param.dao.SysParamTreeDao;
import com.key.win.param.service.SysParamTreeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysParamTreeServiceImpl extends ServiceImpl<SysParamTreeDao, SysParamTree> implements SysParamTreeService {
    @Override
    public List<SysParamTree> getSysParamTree(SysParamTree sysParamTree) {
        LambdaQueryWrapper<SysParamTree> lambdaQueryWrapper = new LambdaQueryWrapper<SysParamTree>();
        if (sysParamTree != null) {
            if (StringUtils.isNotBlank(sysParamTree.getCode())) {
                lambdaQueryWrapper.eq(SysParamTree::getCode, sysParamTree.getCode());
            }
            if (StringUtils.isNotBlank(sysParamTree.getName())) {
                lambdaQueryWrapper.eq(SysParamTree::getName, sysParamTree.getName());
            }
            if (StringUtils.isNotBlank(sysParamTree.getAttr1())) {
                lambdaQueryWrapper.eq(SysParamTree::getAttr1, sysParamTree.getAttr1());
            }
            if (StringUtils.isNotBlank(sysParamTree.getAttr2())) {
                lambdaQueryWrapper.eq(SysParamTree::getAttr2, sysParamTree.getAttr2());
            }
            if (StringUtils.isNotBlank(sysParamTree.getAttr3())) {
                lambdaQueryWrapper.eq(SysParamTree::getAttr3, sysParamTree.getAttr3());
            }
            if (StringUtils.isNotBlank(sysParamTree.getAttr4())) {
                lambdaQueryWrapper.eq(SysParamTree::getAttr4, sysParamTree.getAttr4());
            }
            if (StringUtils.isNotBlank(sysParamTree.getAttr5())) {
                lambdaQueryWrapper.eq(SysParamTree::getAttr5, sysParamTree.getAttr5());
            }
            if (StringUtils.isNotBlank(sysParamTree.getParentId())) {
                lambdaQueryWrapper.eq(SysParamTree::getParentId, sysParamTree.getParentId());
            }
        }
        List<SysParamTree> list = this.list(lambdaQueryWrapper);
        return list;
    }

    @Override
    public PageResult<SysParamTree> getSysParamTreeByPaged(PageRequest<SysParamTree> t) {

        MybatiesPageServiceTemplate<SysParamTree, SysParamTree> mybatiesPageServiceTemplate = new MybatiesPageServiceTemplate<SysParamTree, SysParamTree>(this.baseMapper) {
            @Override
            protected AbstractWrapper constructWrapper(SysParamTree sysParamTree) {
                LambdaQueryWrapper<SysParam> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                if (StringUtils.isNotBlank(sysParamTree.getName())) {
                    lambdaQueryWrapper.
                            like(SysParam::getCode, "%" + sysParamTree.getCode() + "%")
                            .or()
                            .like(SysParam::getName, "%" + sysParamTree.getCode() + "%")
                            .or()
                            .like(SysParam::getAttr1, "%" + sysParamTree.getCode() + "%")
                            .or()
                            .like(SysParam::getAttr2, "%" + sysParamTree.getCode() + "%")
                            .or()
                            .like(SysParam::getAttr3, "%" + sysParamTree.getCode() + "%")
                            .or()
                            .like(SysParam::getAttr4, "%" + sysParamTree.getCode() + "%")
                            .or()
                            .like(SysParam::getAttr5, "%" + sysParamTree.getCode() + "%");
                }

                return lambdaQueryWrapper;
            }
        };

        return mybatiesPageServiceTemplate.doPagingQuery(t);
    }
}
