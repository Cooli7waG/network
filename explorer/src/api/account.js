import request from '@/utils/request.js'

export function findByAddress(address){
    return request({
        url: '/account/account/findByAddress/'+address,
        method: 'get'
    })
}

export function list(data){
    return request({
        url: '/account/account/list',
        method: 'get',
        params:data
    })
}


export function register(data){
    return request({
        url: '/account/account/register',
        method: 'post',
        data:data
    })
}
