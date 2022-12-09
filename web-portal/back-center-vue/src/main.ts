import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './plugins/element.js'
import './plugins/viewer.js'
import './assets/css/global.css'
import './assets/fonts/iconfont.css'
import './common/filters/global-filter'
import '@/permission'
import { plugins } from './plugins/index' // 引入全局插件
import uploader from 'vue-simple-uploader'

/* 左边工具栏以及编辑节点的样式 */
import 'bpmn-js/dist/assets/diagram-js.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css'
/* 右边工具栏样式 */
import 'bpmn-js-properties-panel/dist/assets/bpmn-js-properties-panel.css'

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

Vue.use(plugins)
Vue.use(uploader)
Vue.config.productionTip = false
Vue.prototype.$env = process.env
