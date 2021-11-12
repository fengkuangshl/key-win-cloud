import { VuexModule, Module, Mutation, Action, getModule } from 'vuex-module-decorators'
import { RouteConfig } from 'vue-router'
import { constantRoutes } from '@/router'
import store from '@/store'
import { MenuModule } from './menu-store'
import { MenuResponse } from '@/views/index/system/menu/interface/menu-response'

export interface IPermissionState {
  routes: Array<RouteConfig>
  dynamicRoutes: Array<RouteConfig>
}

@Module({ dynamic: true, store, name: 'permission' })
class Permission extends VuexModule implements IPermissionState {
  public routes: Array<RouteConfig> = []
  public dynamicRoutes: Array<RouteConfig> = []
  public indexRoute: RouteConfig = {
    path: '/index',
    name: 'Index',
    redirect: '/home',
    component: () => import(/* webpackChunkName: "index" */ '@/views/index/Index.vue'),
    children: [
      {
        path: '/home',
        name: 'home',
        component: () => import(/* webpackChunkName: "home" */ '@/views/index/home/Home.vue')
      },
      {
        path: '/test',
        name: 'Test',
        component: () => import(/* webpackChunkName: "test" */ '@/views/index/test/Test.vue')
      }
    ]
  }

  public route404: RouteConfig = {
    path: '*',
    name: '/404',
    component: () => import(/* webpackChunkName: "test" */ '@/components/404.vue')
  }

  get getPermission() {
    return this.dynamicRoutes
  }

  @Mutation
  private SET_ROUTES(childrenRoutes: RouteConfig[]) {
    childrenRoutes.forEach(item => {
      (this.indexRoute.children as Array<RouteConfig>).push(item)
    })
    // (this.indexRoute.children as Array<RouteConfig>).push(this.route404)
    this.routes = constantRoutes.concat(this.indexRoute)
    this.routes.push(this.route404)
    this.dynamicRoutes = [this.indexRoute, this.route404]
  }

  @Action
  public GenerateRoutes() {
    const menus: Array<MenuResponse> = MenuModule.getMenus
    const routes: Array<RouteConfig> = []
    asyncRouter(menus, routes)
    this.SET_ROUTES(routes)
  }
}

export const asyncRouter = (menus: Array<MenuResponse>, routes: Array<RouteConfig>): void => {
  menus.forEach(menu => {
    if (menu.subMenus != null && menu.subMenus.length > 0) {
      asyncRouter(menu.subMenus, routes)
    } else {
      /**
         * Note: sub-menu only appear when route children.length >= 1
         * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
         *
         * hidden: true                   if set true, item will not show in the sidebar(default is false)
         * alwaysShow: true               if set true, will always show the root menu
         *                                if not set alwaysShow, when item has more than one children route,
         *                                it will becomes nested mode, otherwise not show the root menu
         * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
         * name:'router-name'             the name is used by <keep-alive> (must set!!!)
         * meta : {
              roles: ['admin','editor']    control the page roles (you can set multiple roles)
              title: 'title'               the name show in sidebar and breadcrumb (recommend set)
              icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
              noCache: true                if set true, the page will no be cached(default is false)
              affix: true                  if set true, the tag will affix in the tags-view
              breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
              activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
            }
         */
      const route: RouteConfig = {
        path: menu.url,
        name: menu.url.substr(1),
        component: loadViewsd(menu.path),
        meta: {
          title: menu.name
        }
      }
      routes.push(route)
    }
  })
}

export const loadViewsd = (view: string) => {
  return (resolve: any): void => require([`@/views/index/${view}.vue`], resolve)
}

export const PermissionModule = getModule(Permission)
