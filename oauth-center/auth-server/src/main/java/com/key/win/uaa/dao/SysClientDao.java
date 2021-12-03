package com.key.win.uaa.dao;

import com.key.win.common.model.SysClient;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysClientDao {

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into oauth_client_details(client_id, resource_ids, client_secret,client_secret_str, scope, "
			+ " authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, "
			+ " additional_information, autoapprove, status ,if_limit , limit_count) "
			+ " values(#{clientId}, #{resourceIds}, #{clientSecret},#{clientSecretStr}, #{scope}, "
			+ " #{authorizedGrantTypes}, #{webServerRedirectUri}, #{authorities}, #{accessTokenValidity}, #{refreshTokenValidity}, "
			+ " #{additionalInformation}, #{autoapprove} ,0 , #{ifLimit} , #{limitCount} )")
	int save(SysClient client);

	@Delete("delete from oauth_client_details where id = #{id} and enable_flag = 1")
	int delete(String id);

	int updateByPrimaryKey(SysClient client);

	@Select("select t.* from oauth_client_details t where t.id = #{id} and t.enable_flag = 1  ")
	SysClient getById(String id);

	@Select("select t.*  from oauth_client_details t where t.client_id = #{clientId} and t.enable_flag = 1 ")
	SysClient getClient(String clientId);

	int count(@Param("params") Map<String, Object> params);

	List<SysClient> findList(@Param("params") Map<String, Object> params);

}
