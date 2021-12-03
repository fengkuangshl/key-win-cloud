package com.key.win.common.auth.details;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.io.Serializable;


/**
 * @author 作者 owen
 * @version 创建时间：2017年11月12日 上午22:57:51
 * blog: https://blog.51cto.com/13005375
 * code: https://gitee.com/owenwangwen/key-win-cloud
 * 类说明 客户端应用信息
 */
@Data
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DefaultClientDetails extends BaseClientDetails implements Serializable {
    
    private String id ;
    //限流标识  
    private long ifLimit;
    //限流次数
    private long limitCount;


    public DefaultClientDetails(String clientId, String resourceIds, String scopes,
                                String grantTypes, String authorities, String redirectUris) {
        super(clientId, resourceIds, scopes, grantTypes, authorities, redirectUris);
    }


}
