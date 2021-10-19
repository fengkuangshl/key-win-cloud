package com.key.win.rpc.jpa.repository;

import com.key.win.jpa.repository.BaseRepository;
import com.key.win.rpc.jpa.model.JpaRpcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaRpcTemplateRepository extends BaseRepository<JpaRpcTemplate,String> {
}
