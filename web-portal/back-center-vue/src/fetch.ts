import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { Message } from 'element-ui'
import settings from '@/settings'
import { local } from './store'

let baseURL = settings.devDomain
if (process.env.NODE_ENV === 'development') {
  // 设置默认本地开发
  baseURL = settings.devDomain
} else if (process.env.VUE_APP_CURRENTMODE === 'test') {
  // 测试
  baseURL = settings.uatDomain
} else if (process.env.VUE_APP_CURRENTMODE === 'prod') {
  // 默认正式
  baseURL = settings.prodDomain
}

const instance: AxiosInstance = axios.create({
  baseURL: baseURL,
  timeout: 3000
})

// eslint-disable-next-line @typescript-eslint/no-explicit-any
function errorHandle(err: any): Promise<unknown> {
  let response = err.response
  if (err.message === 'Network Error') {
    response = { data: {} }
    response.status = -1
  }
  switch (response?.status) {
    case -1:
      err.message = '网络异常，请稍后再试！'
      break
    case 400:
      err.message = response.data.msg || '请求错误'
      break
    case 401:
      local.clear('access_token')
      err.message = '未授权，请登录'
      break
    case 403:
      err.message = 'token值无效，请重新登录'
      break
    case 404:
      err.message = `请求地址出错: ${response.config.url}`
      break
    case 408:
      err.message = '请求超时'
      break
    case 500:
      err.message = '服务器内部错误'
      break
    case 501:
      err.message = '服务未实现'
      break
    case 502:
      err.message = '网关错误'
      break
    case 503:
      err.message = '服务不可用'
      break
    case 504:
      err.message = '网关超时'
      break
    case 505:
      err.message = 'HTTP版本不受支持'
      break
    default:
      err.message = '其他情况'
  }
  Message.error(err.message)
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
  err => {
    return errorHandle(err)
  }
)

instance.interceptors.response.use(
  (result: AxiosResponse): KWResponse.Result | KWResponse.PageResult | void => {
    return result.data as KWResponse.Result | KWResponse.PageResult | void
  },
  err => {
    return errorHandle(err)
  }
)

export default instance
