package com.key.win.common.model.baseData;

import com.key.win.common.model.basic.MybatisID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SysBaseParam extends MybatisID {
    private String code;
    private String name;
    private String attr1;
    private String attr2;
    private String attr3;
    private String attr4;
    private String attr5;
}
