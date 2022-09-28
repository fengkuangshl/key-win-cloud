package com.key.win.common.model.baseData.enums;

public enum SysParamTreeEnum {

    district(0, "行政区");

    private int code;
    private String text;

    private SysParamTreeEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }
}
