package com.key.win.activiti.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessTaskCompleteFormVo {
    private String taskId;
    private String audit;
}
