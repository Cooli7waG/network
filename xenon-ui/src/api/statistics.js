import request from '@/utils/request.js'

export function getBlockchainstats(){
    return request({
        url: '/block/statistics/blockchainstats',
        method: 'get'
    })
}

export function getMinerStatistics(){
    return request({
        url: '/block/statistics/minerstats',
        method: 'get'
    })
}

export function getMinerLocation(){
    return request({
        url: '/device/device/getMinerLocation',
        method: 'post'
    })
}

export function loadMinersInfo(data){
    return request({
        url: '/device/device/loadMinersInfo',
        method: 'post',
        data:data
    })
}
