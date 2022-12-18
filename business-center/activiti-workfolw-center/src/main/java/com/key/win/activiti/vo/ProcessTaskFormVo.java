package com.key.win.activiti.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessTaskFormVo {
    //任务id
    private String taskId;
    //审批意见
    private String audit;
    //审批人
    private String assignee;

    private String processInstanceId;
}
