package com.key.win.activiti.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.key.win.activiti.model.FormData;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface FormDataDao extends BaseMapper<FormData> {


    //删除表单
    @Delete("DELETE FROM act_form_data WHERE PROC_DEF_ID_ = #{procDefId}")
    int deleteFormDataByProcDefId(@Param("procDefId") String procDefId);

    //读取表单
    @Select("SELECT Control_ID_,Control_VALUE_ from act_form_data  where PROC_INST_ID_ = #{procInstId}")
    List<HashMap<String, Object>> selectFormData(@Param("procInstId") String procInstId);
}
