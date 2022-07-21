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
