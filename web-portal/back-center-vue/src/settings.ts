interface ISettings {
  title: string // Overrides the default title
  activePath: string
  accessToken: string
  developmentDomain: string
  userAcceptanceTestDomain: string
  productionDomain: string
  menuTypeItem: string
  menuTypeDirectory: string
}

// You can customize below settings :)
const settings: ISettings = {
  title: 'key-win后台管理',
  activePath: 'activePath',
  accessToken: 'access_token',
  developmentDomain: 'http://127.0.0.1:9200',
  userAcceptanceTestDomain: 'http://localhost:9200',
  productionDomain: 'http://127.0.0.1:9200',
  menuTypeItem: '菜单',
  menuTypeDirectory: '目录'
}

export default settings
