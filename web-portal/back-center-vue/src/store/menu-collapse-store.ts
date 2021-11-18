import { VuexModule, Mutation, Action, getModule, Module } from 'vuex-module-decorators'
import store from '@/store'

export interface MenuCollapseState {
  isCollapseMenu: boolean
}

@Module({ dynamic: true, store, name: 'menuCollapse' })
class MenuCollapseStore extends VuexModule implements MenuCollapseState {
  public isCollapseMenu = false

  get getCollapseMenuState() {
    return this.isCollapseMenu
  }

  @Mutation
  private CHANGE_COLLAPSEMENU(isCollapseMenu: boolean) {
    this.isCollapseMenu = isCollapseMenu
  }

  @Action
  public changeCollapseMenu(isCollapseMenu: boolean) {
    console.log('isCollapseMenu:' + isCollapseMenu)
    this.CHANGE_COLLAPSEMENU(isCollapseMenu)
  }
}

export const MenuCollapseModule = getModule(MenuCollapseStore)
