package com.key.win.jpa.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
public class BaseModel implements Serializable{

	@Id
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")//UUIDGenerator
    @GeneratedValue(generator = "uuid2")
    @Column(length = 36)
    private String id;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(updatable = false)
    private Date createDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updateDate;

    @CreatedBy
    @Column(updatable = false)
    private String createUserId;
    @Column(updatable = false)
    private String createUserName;

    @LastModifiedBy
    private String updateUserId;
    private String updateUserName;



    @Version
    private Long version;

    @Column(nullable = false)
    private Boolean enableFlag = Boolean.FALSE;
    
}
