package com.key.win.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.exception.service.ServiceException;
import com.key.win.common.web.Result;
import com.key.win.user.entity.OcpTql;

public interface UserService extends IService<OcpTql> {

    Result deductionAmount(String userId) throws ServiceException;

}
