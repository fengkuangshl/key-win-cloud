import Vue from 'vue'
import Vuex from 'vuex'
import StorageSupport from './storage'
Vue.use(Vuex)
/** 暴露sessionStorage */
export const session = new StorageSupport(window.sessionStorage)

/** 暴露localStorage */
export const local = new StorageSupport(window.localStorage)

export default new Vuex.Store({
  state: {},
  mutations: {},
  actions: {},
  modules: {}
})
