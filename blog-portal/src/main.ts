import { createApp } from 'vue'
import { MotionPlugin } from '@vueuse/motion'
import { pinia } from './stores'
import router from './router'
import App from './App.vue'
import './styles/main.css'

const app = createApp(App)

app.use(pinia)
app.use(router)
app.use(MotionPlugin)

app.mount('#app')
