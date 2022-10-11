package com.key.win.param.service.impl;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.key.win.common.model.baseData.SysParam;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.mybatis.page.MybatisPageServiceTemplate;
import com.key.win.param.dao.SysParamDao;
import com.key.win.param.service.SysParamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysParamServiceImpl extends ServiceImpl<SysParamDao, SysParam> implements SysParamService {
    @Override
    public  List<SysParam> getSysParam(SysParam sysParam) {
        LambdaQueryWrapper<SysParam> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (sysParam != null) {
            if (StringUtils.isNotBlank(sysParam.getCode())) {
                lambdaQueryWrapper.eq(SysParam::getCode, sysParam.getCode());
            }
            if (StringUtils.isNotBlank(sysParam.getName())) {
                lambdaQueryWrapper.eq(SysParam::getName, sysParam.getName());
            }
            if (StringUtils.isNotBlank(sysParam.getAttr1())) {
                lambdaQueryWrapper.eq(SysParam::getAttr1, sysParam.getAttr1());
            }
            if (StringUtils.isNotBlank(sysParam.getAttr2())) {
                lambdaQueryWrapper.eq(SysParam::getAttr2, sysParam.getAttr2());
            }
            if (StringUtils.isNotBlank(sysParam.getAttr3())) {
                lambdaQueryWrapper.eq(SysParam::getAttr3, sysParam.getAttr3());
            }
            if (StringUtils.isNotBlank(sysParam.getAttr4())) {
                lambdaQueryWrapper.eq(SysParam::getAttr4, sysParam.getAttr4());
            }
            if (StringUtils.isNotBlank(sysParam.getAttr5())) {
                lambdaQueryWrapper.eq(SysParam::getAttr5, sysParam.getAttr5());
            }
        }
        List<SysParam> list = this.list(lambdaQueryWrapper);
        return list;
    }

    @Override
    public PageResult<SysParam> getSysParamByPaged(PageRequest<SysParam> t) {

        MybatisPageServiceTemplate<SysParam,SysParam> mybatisPageServiceTemplate = new MybatisPageServiceTemplate<SysParam,SysParam>(this.baseMapper) {
            @Override
            protected AbstractWrapper constructWrapper(SysParam sysParam) {
                LambdaQueryWrapper<SysParam> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                if (StringUtils.isNotBlank(sysParam.getName())) {
                    lambdaQueryWrapper.
                            like(SysParam::getCode, "%" + sysParam.getCode() + "%")
                            .or()
                            .like(SysParam::getName, "%" + sysParam.getCode() + "%")
                            .or()
                            .like(SysParam::getAttr1, "%" + sysParam.getCode() + "%")
                            .or()
                            .like(SysParam::getAttr2, "%" + sysParam.getCode() + "%")
                            .or()
                            .like(SysParam::getAttr3, "%" + sysParam.getCode() + "%")
                            .or()
                            .like(SysParam::getAttr4, "%" + sysParam.getCode() + "%")
                            .or()
                            .like(SysParam::getAttr5, "%" + sysParam.getCode() + "%");
                }

                return lambdaQueryWrapper;
            }
        };

        return mybatisPageServiceTemplate.doPagingQuery(t);
    }
}
