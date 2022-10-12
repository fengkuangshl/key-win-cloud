package com.key.win.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.key.win.common.jsonEnum.JsonEnum;
import com.key.win.common.jsonEnum.TextureEnumDeserializer;
import com.key.win.common.jsonEnum.TextureEnumSerializerCode;
import org.apache.commons.lang3.StringUtils;

@JsonSerialize(using = TextureEnumSerializerCode.class)
@JsonDeserialize(using = TextureEnumDeserializer.class)
public enum UserTypeEnum implements JsonEnum {

    NORMAL(0, "普通"),
    ADMIN(1, "管理员");
    @EnumValue
    private int code;
    private String text;

    private UserTypeEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }


    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    @Override
    public Object selectEnumByName(String value) {
        if (StringUtils.isNotBlank(value)) {
            for (UserTypeEnum userTypeEnum : UserTypeEnum.values()) {
                if (userTypeEnum.name().equals(value)) {
                    return userTypeEnum;
                }
            }
        }
        return null;
    }
}
