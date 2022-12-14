package com.key.win.uaa.server.token;

import com.key.win.uaa.server.service.ValidateCodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

public class SMSCodeTokenGranter extends AbstractTokenGranter {

	private static final String GRANT_TYPE = "mobile";

	private final UserDetailsService userDetailsService;

	private final ValidateCodeService validateCodeService;

	public SMSCodeTokenGranter(UserDetailsService userDetailsService, ValidateCodeService validateCodeService,
			AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
			OAuth2RequestFactory requestFactory) {
		this(userDetailsService, validateCodeService, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
	}

	protected SMSCodeTokenGranter(UserDetailsService userDetailsService, ValidateCodeService validateCodeService,
			AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
			OAuth2RequestFactory requestFactory, String grantType) {
		super(tokenServices, clientDetailsService, requestFactory, grantType);
		this.userDetailsService = userDetailsService;
		this.validateCodeService = validateCodeService;
	}

	@Override
	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

		Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
		String deviceId = parameters.get("deviceId"); // ???????????????????????????
		String validCode = parameters.get("validCode"); // ???????????????????????????

		if (StringUtils.isBlank(deviceId)) {
			throw new InvalidGrantException("????????????deviceId");
		}
		if (StringUtils.isBlank(validCode)) {
			throw new InvalidGrantException("??????????????????validCode");
		}

		// ????????????????????????
		String code = "";
		try {
			code = validateCodeService.getCode(deviceId);
			if (!validCode.equals(code)) {
				throw new InvalidGrantException("??????????????????");
			} else {
				// ???????????????
				validateCodeService.remove(deviceId);
			}
		} catch (Exception e) {
			throw new InvalidGrantException("??????????????????");
		}

		// ???????????????????????????
		UserDetails user = null;
		try {
			user = userDetailsService.loadUserByUsername(deviceId);
			if (!user.isEnabled()) {
				throw new InvalidGrantException("?????????????????????");
			}
		} catch (Exception e) {
			throw new InvalidGrantException("???????????????");
		}

		Authentication userAuth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		// ??????user.getAuthorities(): ??????????????????????????????????????????
		// org.springframework.security.core.userdetails.UserDetails ?????????, ?????????
		// user.getAuthorities()
		// ??????????????????null??????
		((AbstractAuthenticationToken) userAuth).setDetails(parameters);

		OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
		return new OAuth2Authentication(storedOAuth2Request, userAuth);
	}

}