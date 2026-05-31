import { createApp } from 'vue'
import { MotionPlugin } from '@vueuse/motion'
import { pinia } from '@/plugins/stores'
import router from '@/plugins/router'
import App from './App.vue'
import '@/assets/styles/main.css'

const app = createApp(App)

app.use(pinia)
app.use(router)
app.use(MotionPlugin)

app.mount('#app')
