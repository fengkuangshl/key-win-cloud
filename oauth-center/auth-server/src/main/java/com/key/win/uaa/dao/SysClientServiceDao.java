package com.key.win.uaa.dao;

import com.key.win.common.model.auth.SysService;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * @Author: [gitgeek]
 * @Date: [2018-08-23 15:28]
 * @Description: [ ]
 * @Version: [1.0.0]
 * @Copy: [com.zzg]
 */
@Mapper
public interface SysClientServiceDao {


    @Insert("insert into sys_client_service(client_id, service_id) values(#{clientId}, #{serviceId})")
    int save(@Param("clientId") String clientId, @Param("serviceId") String serviceId);

    int delete(@Param("clientId") String clientId, @Param("serviceId") String serviceId);

    @Select("select t.service_id from sys_client_service t where t.clientId = #{clientId} and t.enable_flag = 1")
    Set<String> findServiceIdsByClientId(String clientId);

    List<SysService> findServicesBySlientIds(@Param("clientIds") Set<String> clientIds);


}
