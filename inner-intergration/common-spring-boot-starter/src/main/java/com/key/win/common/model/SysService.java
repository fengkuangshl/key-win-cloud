package com.key.win.common.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* @author 作者 gitgeek 
* @version 创建时间：2018-08-06 21:29
* 类说明 服务API实体
*/
@Data
public class SysService implements Serializable {

    private static final long serialVersionUID = 749360940290141180L;
    @JsonSerialize(using=ToStringSerializer.class)
    private Long id;
    @JsonSerialize(using=ToStringSerializer.class)
    private Long parentId;
    private String name;
    private String path;
    private Integer sort;
    private Date createTime;
    private Date updateTime;
    private Integer isService;

}