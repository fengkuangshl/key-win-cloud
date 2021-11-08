import { VuexModule, Mutation, Action, getModule, Module } from 'vuex-module-decorators'
import store from '@/store'
import { LoginSuccessUserInfo } from '@/views/index/system/user/interface/User'

export interface UserState {
  user: LoginSuccessUserInfo
}

@Module({ dynamic: true, store, name: 'user' })
class UserStore extends VuexModule implements UserState {
  public user!: LoginSuccessUserInfo

  get getUser() {
    return this.user
  }

  @Mutation
  private CHANGE_USER(user: LoginSuccessUserInfo) {
    this.user = user
  }

  @Action
  public changeUser(user: LoginSuccessUserInfo) {
    console.log('action:' + user)
    this.CHANGE_USER(user)
  }
}

export const UserModule = getModule(UserStore)
