package com.key.win.activiti.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.key.win.activiti.dao.FormDataDao;
import com.key.win.activiti.model.FormData;
import com.key.win.activiti.service.FormDataService;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.runtime.TaskRuntime;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class FormDataServiceImpl extends ServiceImpl<FormDataDao, FormData> implements FormDataService {

    @Autowired
    private TaskRuntime taskRuntime;

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

    @Override
    public boolean completeTaskToDynamicForm(String taskId,String operationResult,String notes) {
        Task task = taskRuntime.task(taskId);
        List<FormData> list = new ArrayList();
        FormData formDataResult = new FormData();
        formDataResult.setProcInstId(task.getProcessInstanceId());
        formDataResult.setProcTaskId(taskId);
        formDataResult.setProcDefId(task.getProcessDefinitionId());
        formDataResult.setFormKey(task.getFormKey());
        formDataResult.setControlLabel("操作结果");
        formDataResult.setControlValue(operationResult);
        list.add(formDataResult);
        if(StringUtils.isNotBlank(notes)){
            FormData formDataNotes = new FormData();
            formDataNotes.setProcInstId(task.getProcessInstanceId());
            formDataNotes.setProcTaskId(taskId);
            formDataNotes.setProcDefId(task.getProcessDefinitionId());
            formDataNotes.setFormKey(task.getFormKey());
            formDataNotes.setControlLabel("备注内容");
            formDataNotes.setControlValue(notes);
            list.add(formDataNotes);
        }
        return super.saveBatch(list);
    }

}
