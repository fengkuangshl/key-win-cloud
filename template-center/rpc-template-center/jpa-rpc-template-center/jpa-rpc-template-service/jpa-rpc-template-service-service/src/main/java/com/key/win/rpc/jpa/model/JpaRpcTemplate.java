package com.key.win.rpc.jpa.model;

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
@Entity(name = "jpa_rpc_template")
@Where(clause = "enable_Flag = false")
public class JpaRpcTemplate extends BaseModel {

    private String name;
    private String code;

}
