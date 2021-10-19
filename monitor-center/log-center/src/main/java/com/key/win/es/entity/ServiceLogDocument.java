package com.key.win.es.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

/**
 * 日志对象，映射es中的索引ocp-log-*
 */
@Data
@Document(indexName = "ocp-log-*", type = "doc")
public class ServiceLogDocument {
    @Id
    private String id;
    private Date timestamp;
    private String message;
    private String threadName;
    private String serverPort;
    private String serverIp;
    private String logLevel;
    private String appName;
    private String classname;
    private String contextTraceId;
    private String currentTraceId;
}