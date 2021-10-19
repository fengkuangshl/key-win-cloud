package com.key.win.common.remote.enums;

public enum ResponseCodeEnum {
    SUCCESS("200", "操作成功"),
    E420("420", "JSON格式错误"),
    E440("440", "其它错误"),
    E500("500", "Error！！");
    private String code;

    private String name;

    ResponseCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }


    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
