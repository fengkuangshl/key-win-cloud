package com.key.win.datalog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.key.win.common.model.basic.MybatisID;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据日志
 * </p>
 *
 * @author Tophua
 * @since 2020/5/7
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@TableName("Data_Log")
public class DataLog  extends MybatisID {

    private String content;

    private String fkId;
}
