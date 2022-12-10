interface ISettings {
  title: string // Overrides the default title
  activePath: string
  accessToken: string
  refreshToken: string
  menuTypeItem: string
  menuTypeDirectory: string
  defaultAvatar: string
  isEnablePermission: boolean
  isEnableWebSocket: boolean
  apiAuth: string
  apiUser: string
  apiFile: string
  apiParam: string
  apiLog: string
  apiActiviti: string
}

// You can customize below settings :)
const settings: ISettings = {
  title: 'key-win后台管理',
  activePath: 'activePath',
  accessToken: 'access_token',
  refreshToken: 'refresh_token',
  menuTypeItem: '菜单',
  menuTypeDirectory: '目录',
  defaultAvatar: require('./assets/head.png'),
  isEnablePermission: false,
  isEnableWebSocket: false,
  apiAuth: '/api-auth/',
  apiUser: '/api-user/',
  apiFile: 'api-file/',
  apiParam: '/api-param/',
  apiLog: '/api-log/',
  apiActiviti: '/api-activiti/'
}

export default settings
