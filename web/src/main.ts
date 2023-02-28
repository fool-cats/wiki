import { createApp } from 'vue'
import App from './App.vue'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import router from './router'
import store from './store'
import * as Icons from '@ant-design/icons-vue'
// import "./assets/css/tailwind.css"

const app = createApp(App)
app.use(store).use(Antd).use(router).mount('#app')

const icons: any = Icons
for(const i of icons){
    app.component(i,icons[i])
}

console.log('enviroment:',process.env.NODE_ENV);
