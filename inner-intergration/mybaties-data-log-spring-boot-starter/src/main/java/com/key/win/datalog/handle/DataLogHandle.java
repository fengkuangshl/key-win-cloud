package com.key.win.datalog.handle;

import cn.hutool.core.collection.CollUtil;
import com.key.win.datalog.annotation.DataLog;
import com.key.win.datalog.service.DataLogService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 数据日志处理
 * </p>
 *
 * @author Tophua
 * @since 2020/8/11
 */
@Component
public class DataLogHandle extends BaseDataLog {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLogHandle.class);

    @Autowired
    private DataLogService dataLogService;

    @Override
    public void setting() {
        // 设置排除某张表、某些字段
        this.addExcludeTableName("createDate");
        this.addExcludeFieldName("updateDate")
                .addExcludeFieldName("createUserId")
                .addExcludeFieldName("updateUserId")
                .addExcludeFieldName("createUserName")
                .addExcludeFieldName("updateUserName")
                .addExcludeFieldName("enableFlag");
    }

    @Override
    public boolean isIgnore(DataLog dataLog) {
        // 根据注解判断是否忽略某次操作
        return false;
    }

    @Override
    public void change(DataLog dataLog, DataLogVo data) {
        if (CollUtil.isEmpty(data.getDataChanges())) {
            return;
        }
        if (StringUtils.isBlank(data.getLogStr())) {
            return;
        }

        // 存库
        com.key.win.datalog.entity.DataLog d = new com.key.win.datalog.entity.DataLog();
        d.setContent(data.getLogStr());
        d.setFkId(data.getFkId());
        d.setVersion(0);
        dataLogService.save(d);
        LOGGER.info("存库成功：" + data.getLogStr());
    }

}
