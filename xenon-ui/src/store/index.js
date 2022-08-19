import { createStore } from 'vuex'

export default createStore({
    state: {
        address: '',
        accountId:0,
        balance:0,
        accountType:1,
        mateMaskAddress:undefined
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
        },
        loginMateMask(mateMaskAddress){
            this.state.mateMaskAddress = mateMaskAddress;
        },
        getMateMask(){
            return this.state.mateMaskAddress
        },
        logoutMateMask(){
            this.state.mateMaskAddress = undefined;
        }
    }
})

