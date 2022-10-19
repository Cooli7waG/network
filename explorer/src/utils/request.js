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
        }
        return response.data
    },
    error => {
        let { message } = error;
        if (message == "Network Error") {
            //message = "后端接口连接异常";
            message = "Network Error,can't connect to Service";
        }
        else if (message.includes("timeout")) {
            //message = "系统接口请求超时";
            message = "System interface request timeout";
        }
        else if (message.includes("Request failed with status code")) {
            //message = "系统接口" + message.substr(message.length - 3) + "异常";
            message = "System interface " + message.substr(message.length - 3) + " error";
        }

        ElMessage.error(message)
        return Promise.reject(error)
    })

export default request;
