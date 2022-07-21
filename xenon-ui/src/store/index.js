import { createStore } from 'vuex'

export default createStore({
    state: {
        address: '',
        accountId:0,
        balance:0,
        accountType:1,
    },
    mutations: {
        login(state,account) {
            state.address= account.address||''
            state.accountId=account.id||0
            state.balance=account.balance||0
            state.accountType=account.accountType||1
        },
        logout(state){
            state.address=''
            state.accountId=0
            state.balance=0
            state.accountType=1
        }
    }
})
