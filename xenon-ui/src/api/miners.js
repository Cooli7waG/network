import request from '@/utils/request.js'

export function deviceList(data){
    return request({
        url: '/device/device/list',
        method: 'get',
        params: data
    })
}
export function queryByMiner(minerAddress){
    return request({
        url: '/device/device/queryByMiner?minerAddress='+minerAddress,
        method: 'get'
    })
}


export function register(data){
    return request({
        url: '/device/device/register',
        method: 'post',
        data:data
    })
}

export function onboard(data){
    return request({
        url: '/device/device/onboard',
        method: 'post',
        data:data
    })
}
