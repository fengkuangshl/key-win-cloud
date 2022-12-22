package com.key.win.activiti.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivitiHistoryVo {
    private String id;
    private String name;
    private String assignee;
    private Date createTime;
    private Date startTime;
    private String taskDefinitionKey;
    private String processInstanceId;
    private String processInstanceName;
    private String processDefinitionId;
    private Boolean isRecover;
    private Boolean isAbandon;
}
