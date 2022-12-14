
package com.key.win.uaa.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.key.win.common.auth.props.PermitUrlProperties;
import com.key.win.common.constant.UaaConstant;
import com.key.win.common.feign.FeignInterceptorConfig;
import com.key.win.common.rest.RestTemplateConfig;
import com.key.win.uaa.server.token.PasswordEnhanceTokenGranter;
import com.key.win.uaa.server.token.SMSCodeTokenGranter;
import com.key.win.uaa.server.service.RedisAuthorizationCodeServices;
import com.key.win.uaa.server.service.RedisClientDetailsService;
import com.key.win.uaa.server.service.ValidateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author owen 624191343@qq.com
 * @version ???????????????2017???11???12??? ??????22:57:51
 * blog: https://blog.51cto.com/13005375
 * code: https://gitee.com/owenwangwen/key-win-cloud
 */

@Configuration
@SuppressWarnings("all")
@Import({RestTemplateConfig.class, FeignInterceptorConfig.class})
public class UAAServerConfig {


    /**
     * ?????? ClientDetails??????
     */
    @Bean
    public RedisClientDetailsService redisClientDetailsService(DataSource dataSource, RedisTemplate<String, Object> redisTemplate) {
        RedisClientDetailsService clientDetailsService = new RedisClientDetailsService(dataSource);
        clientDetailsService.setRedisTemplate(redisTemplate);
        return clientDetailsService;
    }


    @Bean
    public RandomValueAuthorizationCodeServices authorizationCodeServices(RedisTemplate<String, Object> redisTemplate) {
        RedisAuthorizationCodeServices redisAuthorizationCodeServices = new RedisAuthorizationCodeServices();
        redisAuthorizationCodeServices.setRedisTemplate(redisTemplate);
        return redisAuthorizationCodeServices;
    }


    /**
     * @author owen 624191343@qq.com
     * @version ???????????????2017???11???12??? ??????22:57:51 ??????token??????????????????
     * DefaultTokenServices????????????
     */
    @Component
    @Configuration
    @EnableAuthorizationServer
    @AutoConfigureAfter(AuthorizationServerEndpointsConfigurer.class)
    public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
        /**
         * ??????authenticationManager ????????? password grant type
         */
        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private UserDetailsService userDetailsService;

        @Autowired
        private ValidateCodeService validateCodeService ;
        
        @Autowired(required = false)
        private TokenStore tokenStore;

        @Autowired(required = false)
        private JwtAccessTokenConverter jwtAccessTokenConverter;

        @Autowired
        private WebResponseExceptionTranslator webResponseExceptionTranslator;

        @Autowired
        private RedisClientDetailsService redisClientDetailsService;

        @Autowired(required = false)
        private RandomValueAuthorizationCodeServices authorizationCodeServices;


        /**
         * ?????????????????????????????????????????????TokenStore???TokenGranter???OAuth2RequestFactory
         */
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

            //????????????
            endpoints.tokenStore(tokenStore).authenticationManager(authenticationManager)
                    // ??????
                    .userDetailsService(userDetailsService);

            if (tokenStore instanceof JwtTokenStore) {
                endpoints.accessTokenConverter(jwtAccessTokenConverter);
            }

            //???????????????
            endpoints.authorizationCodeServices(authorizationCodeServices);
            // ?????? ExceptionTranslationFilter ???????????????
            endpoints.exceptionTranslator(webResponseExceptionTranslator);
            
            //??????oauth ??????
            ClientDetailsService clientDetails = endpoints.getClientDetailsService();
            AuthorizationServerTokenServices tokenServices = endpoints.getTokenServices();
            AuthorizationCodeServices authorizationCodeServices = endpoints.getAuthorizationCodeServices();
            OAuth2RequestFactory requestFactory = endpoints.getOAuth2RequestFactory();
     
            //tokenGranters??????oauth?????? ????????????/oauth/token??????????????????????????????AbstractTokenGranter ?????? 
            List<TokenGranter> tokenGranters = new ArrayList<>();
            //???????????????   GRANT_TYPE = "client_credentials"; 
            tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices, clientDetails, requestFactory));
            //????????????	  GRANT_TYPE = "password"; 	
            tokenGranters.add(new PasswordEnhanceTokenGranter(authenticationManager, tokenServices,clientDetails, requestFactory,validateCodeService));
            //???????????????   GRANT_TYPE = "authorization_code";
            tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetails,requestFactory));
            //????????????	  GRANT_TYPE = "refresh_token";
            tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetails, requestFactory));
            //????????????	  GRANT_TYPE = "implicit";
            tokenGranters.add(new ImplicitTokenGranter(tokenServices, clientDetails, requestFactory));
            //????????????	  GRANT_TYPE = "sms"; ??????ResourceOwnerPasswordTokenGranter??????
            tokenGranters.add(new SMSCodeTokenGranter( userDetailsService,  validateCodeService  ,  tokenServices, clientDetails, requestFactory));
            //????????????
            endpoints.tokenGranter(new CompositeTokenGranter(tokenGranters));

            

        }

        /**
         * ?????????????????? ??????id
         * ??????OAuth2????????????????????????
         */
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {


            clients.withClientDetails(redisClientDetailsService);
            redisClientDetailsService.loadAllClientToCache();
        }

        /**
         * ???????????????AuthorizationServer????????????????????????????????????ClientCredentialsTokenEndpointFilter???????????????
         */
        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

            // url:/oauth/token_key,exposes
            security.tokenKeyAccess("permitAll()")
                    /// public key for token
                    /// verification if using
                    /// JWT tokens
                    // url:/oauth/check_token
                    .checkTokenAccess("isAuthenticated()")
                    // allow check token
                    .allowFormAuthenticationForClients();

        }

    }

    @Configuration
    @EnableResourceServer
    @EnableConfigurationProperties(PermitUrlProperties.class)
    public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

        @Autowired
        private PermitUrlProperties permitUrlProperties;
        @Autowired(required = false)
        private TokenStore tokenStore;
        @Autowired
        private ObjectMapper objectMapper; //springmvc?????????????????????json?????????

        @Autowired
        private OAuth2WebSecurityExpressionHandler expressionHandler;

        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/health");
            web.ignoring().antMatchers("/oauth/user/token");
            web.ignoring().antMatchers("/oauth/client/token");
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

            if (tokenStore != null) {
                resources.tokenStore(tokenStore);
            }
            resources.stateless(true);
            resources.expressionHandler(expressionHandler);
            // ???????????????????????????
            resources.authenticationEntryPoint(new AuthenticationEntryPoint() {

                @Override
                public void commence(HttpServletRequest request, HttpServletResponse response,
                                     AuthenticationException authException) throws IOException, ServletException {

                    Map<String, String> rsp = new HashMap<>();

                    response.setStatus(HttpStatus.UNAUTHORIZED.value());

                    rsp.put("code", HttpStatus.UNAUTHORIZED.value() + "");
                    rsp.put("msg", authException.getMessage());

                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(objectMapper.writeValueAsString(rsp));
                    response.getWriter().flush();
                    response.getWriter().close();

                }
            });
            resources.accessDeniedHandler(new OAuth2AccessDeniedHandler() {

                @Override
                public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException authException) throws IOException, ServletException {

                    Map<String, String> rsp = new HashMap<>();
                    response.setContentType("application/json;charset=UTF-8");

                    response.setStatus(HttpStatus.UNAUTHORIZED.value());

                    rsp.put("code", HttpStatus.UNAUTHORIZED.value() + "");
                    rsp.put("msg", authException.getMessage());

                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(objectMapper.writeValueAsString(rsp));
                    response.getWriter().flush();
                    response.getWriter().close();

                }
            });

        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.requestMatcher(
                    /**
                     * ??????????????????????????????oauth2????????????
                     */
                    new RequestMatcher() {
                        private AntPathMatcher antPathMatcher = new AntPathMatcher();

                        @Override
                        public boolean matches(HttpServletRequest request) {
                            // ?????????????????????access_token??????
                            if (request.getParameter(OAuth2AccessToken.ACCESS_TOKEN) != null) {
                                return true;
                            }

                            // ?????????Authorization??????Bearer??????
                            String auth = request.getHeader(UaaConstant.AUTHORIZATION);
                            if (auth != null) {
                                if (auth.startsWith(OAuth2AccessToken.BEARER_TYPE)) {
                                    return true;
                                }
                            }

                            // ????????????url?????????????????????true??????????????????login.html??????
                            if (antPathMatcher.match(request.getRequestURI(), "/api-auth/oauth/userinfo")) {
                                return true;
                            }
                            if (antPathMatcher.match(request.getRequestURI(), "/api-auth/oauth/remove/token")) {
                                return true;
                            }
                            if (antPathMatcher.match(request.getRequestURI(), "/api-auth/oauth/get/token")) {
                                return true;
                            }
                            if (antPathMatcher.match(request.getRequestURI(), "/api-auth/oauth/refresh/token")) {
                                return true;
                            }

                            if (antPathMatcher.match(request.getRequestURI(), "/api-auth/oauth/token/list")) {
                                return true;
                            }

                            if (antPathMatcher.match("/**/clients/**", request.getRequestURI())) {
                                return true;
                            }

                            if (antPathMatcher.match("/**/services/**", request.getRequestURI())) {
                                return true;
                            }
                            if (antPathMatcher.match("/**/redis/**", request.getRequestURI())) {
                                return true;
                            }

                            return false;
                        }
                    }

            ).authorizeRequests().antMatchers(permitUrlProperties.getIgnored()).permitAll()
                    .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                    .anyRequest()
                    .authenticated();
        }

    }


}
