package com.key.win.mybaties.template.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.key.win.common.model.base.MybatisID;
import lombok.Data;

@Data
@TableName("mybaties_template")
public class MybatiesTemplate extends MybatisID {

    private String name;
    private String code;

}
