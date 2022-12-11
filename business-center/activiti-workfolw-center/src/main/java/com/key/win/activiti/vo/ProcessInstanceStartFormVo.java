package com.key.win.activiti.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
@Data
public class ProcessInstanceStartFormVo {
    private String processDefinitionId;
    private String processDefinitionKey;
    private String name;
    private String businessKey;
    private Map<String, Object> variables = new HashMap();
}
