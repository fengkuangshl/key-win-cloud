package com.key.win.mybaties.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MybatiesFeignTemplateVo {

    private String id;
    private Date createDate;
    private Date updateDate;
    private String createUserName;
    private String updateUserId;
    private String updateUserName;
    private Long version;
    private Boolean enableFlag;
    private String name;
    private String code;
}
