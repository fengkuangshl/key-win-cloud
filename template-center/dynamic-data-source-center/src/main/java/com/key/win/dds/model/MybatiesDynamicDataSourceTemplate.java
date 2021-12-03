package com.key.win.dds.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.key.win.common.model.basic.MybatisID;
import lombok.Data;

@Data
@TableName("MYBATIES_DYNAMIC_DATA_SOURCE_TEMPLATE")
public class MybatiesDynamicDataSourceTemplate extends MybatisID {

    private String name;
    private String code;

}
