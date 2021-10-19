package com.key.win.es.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * nginx日志对象,映射es中的索引kafka_nginxlogs-*
 */
@Data
@Document(indexName = "kafka_nginxlogs-*", type = "doc" )
public class NinxLogDocument {
    @Id
    private String id;
    private String lon ;
    private String lat ;
    
}