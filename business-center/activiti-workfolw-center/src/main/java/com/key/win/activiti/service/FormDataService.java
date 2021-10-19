package com.key.win.activiti.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.activiti.model.FormData;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface FormDataService extends IService<FormData> {

    List<HashMap<String, Object>> selectFormData(String procInstId);

    int deleteFormDataByProcDefId(String procDefId);

    public List<FormData> getFormDataByCondition(FormData formData);
}
