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
        url: '/block/pogg/onboard',
        method: 'post',
        data:data
    })
}

export function getReport(data){
    return request({
        url: '/block/pogg/getReport',
        method: 'post',
        data:data
    })
}
export function getReward(data){
    return request({
        url: '/block/pogg/getReward',
        method: 'post',
        data:data
    })
}
