package com.key.win.common.remote.vo;


import lombok.Data;


@Data
public class ResponseVo<RT> {

    //code
    private String responseCode;
    //message
    private String responseMessage;

    private RT data;
}
