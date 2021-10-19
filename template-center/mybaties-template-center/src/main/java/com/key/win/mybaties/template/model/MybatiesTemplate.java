package com.key.win.mybaties.template.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.key.win.common.MybatisID;
import lombok.Data;

@Data
@TableName("mybaties_template")
public class MybatiesTemplate extends MybatisID {

    private String name;
    private String code;

}
