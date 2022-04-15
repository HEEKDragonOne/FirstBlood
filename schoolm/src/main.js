import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './assets/global.css'
import * as ElIcons from '@element-plus/icons-vue'
import request from './request/request'

const app = createApp(App)
for (const icname in ElIcons){
    app.component(icname,ElIcons[icname])
}
app.config.globalProperties.$http = request
app.use(store)
app.use(router)
app.use(ElementPlus,{size: 'default'})
app.mount('#app')
