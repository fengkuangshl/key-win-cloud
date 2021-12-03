package com.key.win.rpc.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.key.win.common.model.basic.MybatisID;
import lombok.Data;

@Data
@TableName("mybaties_rpc_template")
public class MybatiesRpcTemplate extends MybatisID {

    private String name;
    private String code;

}
