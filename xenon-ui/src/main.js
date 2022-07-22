import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import store from './store/index.js'
import '@/utils/nprogress'
import i18n from './locales'


const app = createApp(App)

app.use(ElementPlus)
app.use(router)
app.use(store)
app.use(i18n)
app.mount('#app')
