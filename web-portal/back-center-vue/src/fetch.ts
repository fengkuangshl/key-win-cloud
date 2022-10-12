import axios, { AxiosError, AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { Message } from 'element-ui'
import settings from '@/settings'
import { local } from './store'
import { LoginResponse } from './views/login/interface/response'
import router from './router'
import { getHttpDomain } from './common/utils/get-env'
import * as qs from 'qs'

const instance: AxiosInstance = axios.create({
  baseURL: getHttpDomain(),
  timeout: 3000
})

function errorHandle(err: AxiosError): Promise<unknown> {
  if (err.code === 'ECONNABORTED' && err.message.indexOf('timeout') !== -1) {
    return onTimeoutError(err)
  } else if (err.response === undefined) {
    return onNetWorkError(err)
  }
  let message = ''
  switch (err.response?.status) {
    case 400:
      return oAuth2Error(err)
    case 401:
      return onAuthenticationError(err)
    case 403:
      message = '拒绝访问'
      break
    case 404:
      message = `请求地址出错: ${err.response?.config.url}`
      break
    case 408:
      message = '请求超时'
      break
    case 500:
      message = '服务器内部错误'
      break
    case 501:
      message = '服务未实现'
      break
    case 502:
      message = '网关错误'
      break
    case 503:
      message = '服务不可用'
      break
    case 504:
      message = '网关超时'
      break
    case 505:
      message = 'HTTP版本不受支持'
      break
    default:
      message = '其他情况'
  }
  Message.error(message)
  return Promise.reject(err)
}

instance.interceptors.request.use(
  (config: AxiosRequestConfig): AxiosRequestConfig => {
    config.headers = config.headers || {}
    config.headers['x-requested-with'] = 'XMLHttpRequest'
    if (config.url === '/api-auth/oauth/token') {
      config.headers['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8'
    } else {
      config.headers['Content-Type'] = 'application/json;charset=UTF-8'
      const token: string | null = localStorage.getItem('access_token')
      if (token) {
        config.headers.Authorization = 'Bearer ' + token
      }
    }
    return config
  },
  (err: AxiosError) => {
    return errorHandle(err)
  }
)

instance.interceptors.response.use(
  (result: AxiosResponse): KWResponse.Result | KWResponse.PageResult | void => {
    return result.data as KWResponse.Result | KWResponse.PageResult | void
  },
  (err: AxiosError) => {
    return errorHandle(err)
  }
)

async function oAuth2Error(err: AxiosError) {
  /* if (errorMessage == null) {
    errorMessage = errorCode == null ? "OAuth Error" : errorCode;
}

if ("invalid_client"===errorCode) {
    return new InvalidClientException(errorMessage);
} else if ("unauthorized_client"===errorCode) {
    return new UnauthorizedClientException(errorMessage);
} else if ("invalid_grant"===errorCode) {
    return new InvalidGrantException(errorMessage);
} else if ("invalid_scope"===errorCode) {
    return new InvalidScopeException(errorMessage);
} else if ("invalid_token"===errorCode) {
    return new InvalidTokenException(errorMessage);
} else if ("invalid_request"===errorCode) {
    return new InvalidRequestException(errorMessage);
} else if ("redirect_uri_mismatch"===errorCode) {
    return new RedirectMismatchException(errorMessage);
} else if ("unsupported_grant_type"===errorCode) {
    return new UnsupportedGrantTypeException(errorMessage);
} else if ("unsupported_response_type"===errorCode) {
    return new UnsupportedResponseTypeException(errorMessage);
} else {
    return (OAuth2Exception)("access_denied"===errorCode ? new UserDeniedAuthorizationException(errorMessage) : new OAuth2Exception(errorMessage));
} */
  const response = err.response as any
  const errorCode = response.data.error
  let message = 'Token失败，请重新登录'
  if (errorCode === 'invalid_client') {
    // return new InvalidClientException(errorMessage)
  } else if (errorCode === 'unauthorized_client') {
    // return new UnauthorizedClientException(errorMessage)
  } else if (errorCode === 'invalid_grant') {
    const msg = response.data.msg
    if (msg.indexOf('Invalid refresh token') === -1) {
      message = response.data.msg
    }

    // return new InvalidGrantException(errorMessage)
  } else if (errorCode === 'invalid_scope') {
    // return new InvalidScopeException(errorMessage)
  } else if (errorCode === 'invalid_token') {
    // return new InvalidTokenException(errorMessage)
  } else if (errorCode === 'invalid_request') {
    // return new InvalidRequestException(errorMessage)
  } else if (errorCode === 'redirect_uri_mismatch') {
    // return new RedirectMismatchException(errorMessage)
  } else if (errorCode === 'unsupported_grant_type') {
    // return new UnsupportedGrantTypeException(errorMessage)
  } else if (errorCode === 'unsupported_response_type') {
    // return new UnsupportedResponseTypeException(errorMessage)
  } else if (errorCode === 'access_denied') {
    // return OAuth2Exception(errorCode === 'access_denied' ? new UserDeniedAuthorizationException(errorMessage) : new OAuth2Exception(errorMessage))
  } else {
    //  s
  }
  Message.error(message)
  return goLogin(err)
}

async function goLogin(error: AxiosError) {
  local.clear(settings.accessToken)
  local.clear(settings.refreshToken)
  router.replace({
    path: '/login'
  })
  return error
}

async function onAuthenticationError(error: AxiosError) {
  local.clear(settings.activePath)
  const { code, data }: KWResponse.Result<LoginResponse> = await getNewToken()
  if (code === 200) {
    local.save(settings.accessToken, data.access_token)
    local.save(settings.refreshToken, data.refresh_token)
    return await instance.request(error.config)
  } else {
    return goLogin(error)
  }
}

function onTimeoutError(err: AxiosError) {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const originalRequest = err.config as any
  if (originalRequest.retry) {
    Message.error('网络超时，请稍后再试！')
    return Promise.reject(err)
  }
  originalRequest.retry = true
  return instance.request(originalRequest)
}

function onNetWorkError(err: AxiosError) {
  Message.error('网络异常，请稍后再试！')
  return Promise.reject(err)
}

async function getNewToken(): Promise<KWResponse.Result<LoginResponse>> {
  const requestRefreshToken: Token.RefreshTokenRequest = {
    grant_type: 'refresh_token',
    client_id: 'webApp',
    client_secret: 'webApp',
    refresh_token: local.getStr(settings.refreshToken)
  }
  return await instance.post(settings.apiAuth + 'oauth/token', qs.stringify(requestRefreshToken))
}

export default instance
