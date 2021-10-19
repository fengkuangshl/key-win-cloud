package com.key.win.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.key.win.api.feign.ItemFeignClient;
import com.key.win.api.feign.UserFeignClient;
import com.key.win.common.exception.service.ServiceException;
import com.key.win.common.util.RandomUtil;
import com.key.win.common.util.UUIDUtils;
import com.key.win.order.entity.OcpOrder;
import com.key.win.order.dao.OcpOrderMapper;
import com.key.win.order.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OcpOrderMapper, OcpOrder> implements OrderService {

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private ItemFeignClient itemFeignClient;

    @Override
    @GlobalTransactional
    public String create(String userId,String productId)  throws ServiceException {
        log.info("-----> 开始新建订单");
        OcpOrder order = OcpOrder.builder()
                .userId(userId)
                .createTime(new Date())
                .orderSn(RandomUtil.generateOrderCode())
                .id(UUIDUtils.getGUID32())
                .build();
        this.save(order);
        log.info("-----> 结束新建订单");


        log.info("-----> 开始扣除库存");
        itemFeignClient.deductInventory(productId);
        log.info("-----> 结束扣除库存");


        log.info("-----> 开始扣除用户金额，如果用户金额不够直接报错，回滚数据");
        userFeignClient.deductionAmount(userId);
        return order.getId();
    }
}
