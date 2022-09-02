import request from '@/utils/request.js'
export function list(data){
    return request({
        url: '/block/block/list',
        method: 'get',
        params:data
    })
}

