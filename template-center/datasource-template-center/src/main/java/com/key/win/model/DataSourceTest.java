package com.key.win.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@TableName("data_source_test")
@Entity(name = "data_source_test")
public class DataSourceTest {

    @Id
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "password")
    private String password;

    @TableField(value = "url")
    private String url;

    @TableField(value = "driver_class_name")
    private String driverClassName;
}
