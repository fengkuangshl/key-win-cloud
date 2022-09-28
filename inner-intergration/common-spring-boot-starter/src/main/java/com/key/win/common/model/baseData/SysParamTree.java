package com.key.win.common.model.baseData;

import com.baomidou.mybatisplus.annotation.TableName;
import com.key.win.common.model.baseData.enums.SysParamTreeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@TableName("sys_param_tree")
@EqualsAndHashCode(callSuper = true)
public class SysParamTree extends SysBaseParam {
    private String parentId;
    private SysParamTreeEnum type;
}
