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


// Add a request interceptor
axios.interceptors.request.use(function (config) {
    // Do something before request is sent
    console.log("request parameter:", config);
    
    return config;
  }, function (error) {
    // Do something with request error
    console.log("request error:", error);
    
    return Promise.reject(error);
  });

// Add a response interceptor
axios.interceptors.response.use(function (response) {
    // Any status code that lie within the range of 2xx cause this function to trigger
    // Do something with response data
    console.log("response data:", response)
    
    return response;
  }, function (error) {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    // Do something with response error
    console.log("response error:", error);
    
    return Promise.reject(error);
  });



const app = createApp(App)
app.use(store).use(Antd).use(router).mount('#app')

const icons: any = Icons
for(const i of icons){
    app.component(i,icons[i])
}

console.log('enviroment:',process.env.NODE_ENV);
