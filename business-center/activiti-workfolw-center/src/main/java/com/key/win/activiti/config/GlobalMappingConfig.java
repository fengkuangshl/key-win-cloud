package com.key.win.activiti.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Data
@Component
public class GlobalMappingConfig {

    @Value("${global.mapping.bpmn.upload.path:file:D:/dev-env/workspace-key-win-cloud/key-win-cloud/web-portal/back-center/src/main/activiti/static/bpmn/}")
    private String bpmnUploadPath;
    @Value("${global.mapping.bpmn.url.prefix:http://127.0.0.1:8066/bpmn/}")
    private String bpmnUrlPrefix;
}
