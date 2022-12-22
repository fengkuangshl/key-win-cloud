package com.key.win.activiti.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.key.win.activiti.dao.FormDataDao;
import com.key.win.activiti.model.FormData;
import com.key.win.activiti.service.FormDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class FormDataServiceImpl extends ServiceImpl<FormDataDao, FormData> implements FormDataService {

    @Override
    public List<HashMap<String, Object>> selectFormData(String procInstId) {
        return this.baseMapper.selectFormData(procInstId);
    }

    @Override
    public int deleteFormDataByProcDefId(String procDefId) {
        return this.baseMapper.deleteFormDataByProcDefId(procDefId);
    }

    @Override
    public List<FormData> getFormDataByCondition(FormData formData) {
        LambdaQueryWrapper<FormData> lqw = new LambdaQueryWrapper<FormData>();

        if(formData != null && StringUtils.isNotBlank(formData.getProcDefId())){
            lqw.eq(FormData::getProcDefId, formData.getProcDefId());
        }

        if(formData != null && StringUtils.isNotBlank(formData.getProcInstId())){
            lqw.eq(FormData::getProcInstId, formData.getProcInstId());
        }

        if(formData != null && StringUtils.isNotBlank(formData.getFormKey())){
            lqw.like(FormData::getFormKey, formData.getFormKey());
        }

        if(formData != null && StringUtils.isNotBlank(formData.getControlId())){
            lqw.eq(FormData::getControlId, formData.getControlId());
        }

        if(formData != null && StringUtils.isNotBlank(formData.getControlValue())){
            lqw.like(FormData::getControlValue, formData.getControlValue());
        }
        lqw.orderByAsc(FormData::getCreateDate);
        return this.baseMapper.selectList(lqw);
    }

}
