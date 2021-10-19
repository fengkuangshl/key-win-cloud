package com.key.win.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.exception.service.ServiceException;
import com.key.win.common.web.Result;
import com.key.win.item.entity.OcpItem;

public interface ItemService extends IService<OcpItem> {

    Result deductInventory(String productId) throws ServiceException;

}
