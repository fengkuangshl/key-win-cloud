package com.sharding.demo.dao;

import com.sharding.demo.model.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface OrderMapper {
    int save(Order info);

    /**
     * 批量保存
     * @param list
     * @return
     */
    int batchSave(List<Order> list);

}
