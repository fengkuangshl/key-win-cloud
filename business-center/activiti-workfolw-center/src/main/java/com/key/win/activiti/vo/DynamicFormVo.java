package com.key.win.activiti.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicFormVo {
    //任务id
    private String taskId;
    private List<String> formData = new ArrayList<>();
}
