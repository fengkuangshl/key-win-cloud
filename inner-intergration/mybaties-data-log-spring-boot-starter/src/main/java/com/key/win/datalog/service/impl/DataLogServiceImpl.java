package com.key.win.datalog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.key.win.datalog.dao.DataLogDao;
import com.key.win.datalog.entity.DataLog;
import com.key.win.datalog.service.DataLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * DataLogServiceImpl
 * </p>
 *
 * @author Tophua
 * @since 2020/5/7
 */
@Service
public class DataLogServiceImpl extends ServiceImpl<DataLogDao, DataLog> implements DataLogService {
}
