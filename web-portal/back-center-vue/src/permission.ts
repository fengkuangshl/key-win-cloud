import router from './router'
import { Route, RouteConfig, RouteMeta } from 'vue-router'
import { PermissionModule } from '@/store/permission-store'
import getPageTitle from './common/utils/page-title'
import { MenuModule } from './store/menu-store'
import { MenuResponse } from './views/index/system/menu/interface/menu-response'
import { MenuApi } from './views/index/system/menu/menu-api'

// const whiteList = ['/login', '/auth-redirect', '/registe', '/404']

router.beforeEach(async (to: Route, from: Route, next: any): Promise<void> => {
  // to 将访问哪一个路径
  // from 代表从哪个路径跳转而来
  // next 是一个函数,表示放行
  //   next() 放行 next('/login') 强制跳转
  if (to.path === '/login') {
    return next()
  }
  // 获取token
  const token = localStorage.getItem('access_token')
  if (!token) {
    return next('/login')
  } else {
    const dynamicRoutes: Array<RouteConfig> = PermissionModule.getPermission
    if (dynamicRoutes.length === 0) {
      const { code, data, msg }: KWResponse.Result<Array<MenuResponse>> = await MenuApi()
      if (code === 0) {
        console.log(data)
        const menus: Array<MenuResponse> = data.filter(item => item.name.indexOf('vue') > -1) // 暂时先这么处理
        // this.menus = data
        MenuModule.changeMenu(menus)
        PermissionModule.GenerateRoutes()
        router.addRoutes(PermissionModule.dynamicRoutes)
        next({ ...to, replace: true })
      } else {
        console.log(msg || '获取当前用户菜单失败！')
      }
    } else {
      next()
    }
  }
})

router.afterEach((to: Route) => {
  // set page title
  document.title = getPageTitle((to.meta as RouteMeta).title)
})
