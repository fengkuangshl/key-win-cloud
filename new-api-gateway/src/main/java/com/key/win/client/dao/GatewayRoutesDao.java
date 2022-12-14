package com.key.win.client.dao;

import com.key.win.client.entity.GatewayRoutes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
@SuppressWarnings("all")
public interface GatewayRoutesDao {
    int deleteByPrimaryKey(String id);

    int insert(GatewayRoutes record);

    int insertSelective(GatewayRoutes record);

    GatewayRoutes selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(GatewayRoutes record);

    int updateByPrimaryKey(GatewayRoutes record);

    List<GatewayRoutes> findAll(Map map);
}