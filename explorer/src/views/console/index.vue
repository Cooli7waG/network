<template>
  <div class="common-layout">
    <el-container>
      <el-header style="height: 90px;">
        <div v-if="!$store.state.address">
          <el-input v-model="data.account.address" placeholder="请输入Owner 公钥">
            <template #append>
              <el-button type="primary" @click="login">确定</el-button>
            </template>
          </el-input>
        </div>
        <div v-if="$store.state.address">
          <div>当前账户地址：{{$store.state.address}} <el-link @click="logout">退出</el-link></div>
          <div>账户类型: {{Constant.AccountType[$store.state.accountType]}}</div>
          <div>账户余额: {{$store.state.balance}}</div>
        </div>
      </el-header>
      <el-container v-if="$store.state.address">
        <el-aside width="200px">
          <el-menu
              :default-active="data.activeIndex"
              class="el-menu-vertical-demo"
              :router="true"
          >
            <el-menu-item index="/console/miners">
              <span>Miners</span>
            </el-menu-item>
            <el-menu-item index="/console/txs">
              <span>交易列表</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main>
          <router-view />
        </el-main>
      </el-container>

    </el-container>
  </div>
</template>

<script>
import { formatDate } from '@/utils/data_format.js'
import {findByAddress,register} from '@/api/account.js'
import {onMounted, reactive } from "vue";
import { useRouter } from 'vue-router'
import { useStore } from "vuex"
import {ElMessage} from "element-plus";
import {toEther} from '@/utils/utils.js'
import Constant from '@/utils/constant.js'
export default {
  props: {
    msg: String
  },
  computed: {
    formatDate() {
      return formatDate
    },
    Constant() {
      return Constant
    }
  },
  setup(){
    const store = useStore()
    const data = reactive({
      activeIndex:'',
      account:{
        address:'',
        balance:0,
        nonce:0,
      }
    })
    const router = useRouter()

    const login=()=>{
      loadAccount()
    }
    const logout=()=>{
      data.account.ownerAddress=''
      store.commit('logout')
    }

    const loadAccount=()=>{
      findByAddress(data.account.address).then((result)=>{
          let  account=result.data
          if(account){
            account.address=data.account.address
            account.balance=toEther(account.balance,8)
            store.commit('login',account)
          }else{
            account={
              address:data.account.address,
              balance:0,
              nonce:0,
            }
            store.commit('login',account)
          }
          data.activeIndex='/console/miners'
      }).catch((err) =>{
        console.log(err);
      });
    }
    /*const registerAccount=(address)=>{
      const form={
        address:address,
        accountType:1
      }
      register(form).then(result=>{
        if(result.code==0){
          loadAccount()
        }else{
          console.log(result)
        }
      }).catch(err=>{
        console.log(err)
      })
    }*/

    onMounted(() => {
      console.log("onMounted")
    })

    return {
      data,
      login,
      logout
    }
  }
}
</script>
<style lang="scss" scoped>
.box-card {
  .item{
    margin-bottom: 20px;
    .lable{
      font-weight: bold;
      margin-bottom: 6px;
    }
  }

}
</style>
