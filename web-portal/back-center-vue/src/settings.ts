interface ISettings {
  title: string // Overrides the default title
  activePath: string
  accessToken: string
  devDomain: string
  uatDomain: string
  prodDomain: string
}

// You can customize below settings :)
const settings: ISettings = {
  title: 'key-win后台管理',
  activePath: 'activePath',
  accessToken: 'access_token',
  devDomain: 'http://127.0.0.1:9200',
  uatDomain: 'http://127.0.0.1:9200',
  prodDomain: 'http://127.0.0.1:9200'

}

export default settings
