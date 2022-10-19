import request from '@/utils/request.js'

export function start(data){
    return request({
        url: '/virtualminer/virtualminer/save',
        method: 'post',
        data:data
    })
}

export function terminate(data){
    return request({
        url: '/virtualminer/virtualminer/save',
        method: 'get',
        data:data
    })
}
