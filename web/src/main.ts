import { createApp } from 'vue'
import App from './App.vue'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import router from './router'
import store from './store'
import * as Icons from '@ant-design/icons-vue'
import axios from 'axios'
// import "./assets/css/tailwind.css"

// 全局配置域，避免每次请求都要写全路径
axios.defaults.baseURL = process.env.VUE_APP_Server



const app = createApp(App)
app.use(store).use(Antd).use(router).mount('#app')

const icons: any = Icons
for(const i of icons){
    app.component(i,icons[i])
}

console.log('enviroment:',process.env.NODE_ENV);
