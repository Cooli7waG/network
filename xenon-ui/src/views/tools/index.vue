<template>
  <div class="common-layout">
    <el-container>
      <el-container>
        <el-aside width="200px">
          <el-menu
              default-active="/tools/register"
              class="el-menu-vertical-demo"
              :router="true"
          >
            <el-menu-item index="/tools/sign">
              <span>编解码</span>
            </el-menu-item>
            <el-menu-item index="/tools/register">
              <span>Miner注册</span>
            </el-menu-item>
            <el-menu-item index="/tools/onboard">
              <span>Miner绑定</span>
            </el-menu-item>
            <el-menu-item index="/tools/virtualminer">
              <span>Virtual Miner</span>
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
import {findByAddress} from '@/api/account.js'
import {onMounted, reactive } from "vue";
import { useRouter } from 'vue-router'
import { useStore } from "vuex"
export default {
  props: {
    msg: String
  },
  computed: {
    formatDate() {
      return formatDate
    }
  },
  setup(){
    const store = useStore()
    const data = reactive({
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
        const  account=result.data
        if(account){
          account.address=data.account.address
          store.commit('login',account)
        }else{
          store.commit('login',data.account)
        }
      }).catch((err) =>{
        console.log(err);
      });
    }

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
