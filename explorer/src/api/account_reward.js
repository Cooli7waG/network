import request from '@/utils/request.js'

export function getRewardList(data){
    return request({
        url: '/account/accountreward/list',
        method: 'post',
        data:data
    })
}


