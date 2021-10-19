package com.key.win.jpa.template.model;

import com.key.win.jpa.config.JpaAuditListener;
import com.key.win.jpa.model.BaseModel;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@EntityListeners( value = {AuditingEntityListener.class, JpaAuditListener.class})
@Entity(name = "jpa_template")
@Where(clause = "enable_Flag = false")
public class JpaTemplate extends BaseModel {
    private String name;
    private String code;

}
