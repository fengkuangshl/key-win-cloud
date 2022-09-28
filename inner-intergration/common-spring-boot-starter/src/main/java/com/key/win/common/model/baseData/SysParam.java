package com.key.win.common.model.baseData;

import com.baomidou.mybatisplus.annotation.TableName;
import com.key.win.common.model.baseData.enums.SysParamEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@TableName("sys_param")
@EqualsAndHashCode(callSuper = true)
public class SysParam extends SysBaseParam {
    private SysParamEnum type;
}
