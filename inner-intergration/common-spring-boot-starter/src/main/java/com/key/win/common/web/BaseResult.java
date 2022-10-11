package com.key.win.common.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BaseResult implements Serializable {
    @ApiModelProperty("code")
    protected Integer code;
    @ApiModelProperty("提示信息")
    protected String msg;
}
