package com.key.win.activiti.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessTaskVo {
    private String id;
    private String name;
    private String status;
    private String assignee;
    private String instanceName;
    private Date createTime;
}
