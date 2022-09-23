import request from '@/utils/request.js'

export function statisticsRewardByDay(data){
    return request({
        url: '/account/accountreward/statisticsRewardByDay',
        method: 'post',
        data:data
    })
}

export function statisticsByDateTimeRange(data){
    return request({
        url: '/account/accountreward/statisticsByDateTimeRange',
        method: 'post',
        data:data
    })
}
