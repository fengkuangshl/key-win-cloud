package com.key.win.activiti.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessDefinitionVo {
    private String id;
    private String deploymentId;
    private String name;
    private String resourceName;
    private String key;
    private int version;

}
