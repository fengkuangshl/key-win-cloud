package com.key.win.mybaties.template.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface MybatiesSqlTemplateDao {

    @Select("SELECT * FROM `data_log` d INNER JOIN mybaties_template m on d.fk_id = m.id ")
    public List<Map<String,Object>> getAll();
}
