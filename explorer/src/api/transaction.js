import request from '@/utils/request.js'

export function transactionList(data){
    return request({
        url: '/account/transaction/list',
        method: 'get',
        params: data
    })
}
export function findByHash(txHash){
    return request({
        url: '/account/transaction/query?txHash='+txHash,
        method: 'get'
    })
}
