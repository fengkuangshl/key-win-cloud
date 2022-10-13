package com.key.win.websocket.vo;

import com.key.win.common.util.SysUserUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WebsocketBaseMessage implements Serializable {

    // 发送人token
    @ApiModelProperty("发送人token")
    private String token = SysUserUtil.getToken();

    //发送人
    @ApiModelProperty("发送人")
    private String fromUserName = SysUserUtil.getUserName();

    // 信息
    @ApiModelProperty("发送信息")
    private String message;

    @ApiModelProperty("action")
    private String action;

    @ApiModelProperty("mapper")
    private String mapper;
}