package com.key.win.refresh.vo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Data
@Component
public class RefreshScopeTemplateVo {

    @Value("${config.code}")
    private String code;
    @Value("${config.value}")
    private String value;

    public String toString() {
        return code + "------------" + value;
    }
}
