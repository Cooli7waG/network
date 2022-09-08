import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import store from './store/index.js'
import '@/utils/nprogress'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import i18n from './locales'
//import JsonViewer from "vue3-json-viewer"
//import Web3 from 'web3'
import Base64 from 'js-base64'


const app = createApp(App)

app.use(ElementPlus)
app.use(router)
app.use(store)
app.use(i18n)
app.use(Base64)
//app.use(JsonViewer)
//app.config.productionTip = false
//app.prototype.Web3 = Web3
app.mount('#app')
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
