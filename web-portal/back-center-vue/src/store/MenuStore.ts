import {VuexModule, Mutation, Action, getModule, Module} from 'vuex-module-decorators'
import store from '@/store'
import {MenuResponse} from '@/views/index/system/menu/interface/MenuResponse'

export interface MenuState {
  menus: Array<MenuResponse>
}

@Module({dynamic: true, store, name: 'menu'})
class MenuStore extends VuexModule implements MenuState {
  public menus!: Array<MenuResponse>

  get getMenus() {
    return this.menus
  }

  @Mutation
  private CHANGE_MENU(menus: Array<MenuResponse>) {
    this.menus = menus
  }

  @Action
  public changeMenu(menus: Array<MenuResponse>) {
    console.log('action:' + menus)
    this.CHANGE_MENU(menus)
  }
}

export const MenuModule = getModule(MenuStore)
