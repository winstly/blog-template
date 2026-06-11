import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { pinia } from '@/plugins/stores'
import router from '@/plugins/router'
import App from './App.vue'
import '@/assets/styles/main.css'

const app = createApp(App)

app.use(pinia)
app.use(router)
app.use(ElementPlus)

app.mount('#app')
