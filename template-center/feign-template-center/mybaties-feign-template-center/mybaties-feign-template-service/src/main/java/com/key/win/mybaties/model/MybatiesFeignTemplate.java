package com.key.win.mybaties.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.key.win.common.MybatisID;
import lombok.Data;

@Data
@TableName("mybaties_feign_template")
public class MybatiesFeignTemplate extends MybatisID {

    private String name;
    private String code;

}
