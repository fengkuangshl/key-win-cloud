interface ISettings {
  title: string // Overrides the default title
  activePath: string
  accessToken: string
}

// You can customize below settings :)
const settings: ISettings = {
  title: 'key-win后台管理',
  activePath: 'activePath',
  accessToken: 'access_token'
}

export default settings
