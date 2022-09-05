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

export function reportDataList(data){
    return request({
        url: '/block/pogg/reportDataList',
        method: 'post',
        data:data
    })
}


export function getApplyActiveInfo(){
    return request({
        url: '/device/device/applyAirdrop',
        method: 'post'
    })
}

export function getMinersByOwnerAddress(data){
    return request({
        url: '/device/device/getMinersByOwnerAddress',
        method: 'post',
        data:data
    })
}

export function applyGameMiner(data){
    return request({
        url: '/device/device/apply',
        method: 'post',
        data:data
    })
}

export function claimGameMiner(data){
    return request({
        url: '/device/device/claim',
        method: 'post',
        data:data
    })
}


