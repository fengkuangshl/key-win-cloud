package com.key.win.es.dao;

import com.key.win.es.entity.ServiceLogDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * ELK收集ocp中info.info日志查询接口
 */
@Repository
public interface ServiceLogDao extends ElasticsearchRepository<ServiceLogDocument, String> {

}