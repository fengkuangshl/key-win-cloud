package com.key.win.jpa.template.repository;

import com.key.win.jpa.repository.BaseRepository;
import com.key.win.jpa.template.model.JpaTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTemplateRepository extends BaseRepository<JpaTemplate,String> {
}
