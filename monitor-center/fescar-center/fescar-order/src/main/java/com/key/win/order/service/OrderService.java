package com.key.win.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.exception.service.ServiceException;
import com.key.win.order.entity.OcpOrder;

public interface OrderService  extends IService<OcpOrder> {


    String create(String userId,String productId) throws ServiceException;

}
