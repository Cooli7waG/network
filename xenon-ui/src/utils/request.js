import axios from 'axios';
import {  ElMessage  } from 'element-plus'

const config = {
    baseURL: process.env.VUE_APP_BASE_API,
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
    },
};

const request = axios.create(config);

request.interceptors.response.use(
    response => {
        if (response.data.code) {
            switch (response.status) {
                case 301:
                    console.log('登录过期');
            }
        }
        return response.data
    },
    error => {
        let { message } = error;
        if (message == "Network Error") {
            message = "后端接口连接异常";
        }
        else if (message.includes("timeout")) {
            message = "系统接口请求超时";
        }
        else if (message.includes("Request failed with status code")) {
            message = "系统接口" + message.substr(message.length - 3) + "异常";
        }

        ElMessage.error(message)
        return Promise.reject(error)
    })

export default request;
