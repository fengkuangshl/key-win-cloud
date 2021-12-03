package com.key.win.common.model.auth;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.key.win.common.model.basic.MybatisID;
import lombok.Data;

/**
* @author 作者 gitgeek 
* @version 创建时间：2018-08-06 21:29
* 类说明 服务API实体
*/
@Data
public class SysService extends MybatisID {

    @JsonSerialize(using=ToStringSerializer.class)
    private String parentId;
    private String name;
    private String path;
    private Integer sort;
    private Integer isService;

}