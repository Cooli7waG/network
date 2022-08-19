<template>
  <el-row style="background-color: #545c64">
    <el-col :span="20">
      <el-menu
          :default-active="$route.path"
          class="el-menu-demo"
          mode="horizontal"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
          :router="true">
        <el-menu-item index="/">{{ $t('menus.dashboard') }}</el-menu-item>
        <el-menu-item index="/miners">{{ $t('menus.miners') }}</el-menu-item>
        <el-menu-item index="/accounts">{{ $t('menus.accounts') }}</el-menu-item>
        <el-menu-item index="/blocks">{{ $t('menus.blocks') }}</el-menu-item>
        <el-menu-item index="/txs">{{ $t('menus.txs') }}</el-menu-item>
      </el-menu>
    </el-col>
    <el-col :span="4" class="login">
      <div style="float: right;line-height: 50px;margin-right: 10px">
        <el-button v-if="userAddress==undefined" type="text" style="color: #FFFFFF" @click="loginApp">{{ $t('menus.login') }}</el-button>
        <el-button v-else type="text" style="color: #FFFFFF" >{{ formatString(userAddress) }}</el-button>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import {formatString} from "@/utils/data_format";
export default {
  name: 'Menus',
  props: {
    msg: String
  },
  data(){
    return {
      userAddress:undefined
    }
  },
  methods:{
    formatString(str){
      return formatString(str,5);
    },
    async loginApp() {
      if (window.ethereum) {
        const newAccounts = await ethereum.request({
          method: 'eth_requestAccounts',
        });
        this.userAddress = newAccounts[0];
        this.$router.push("/user");
      } else {
        this.$message.error(this.$t('common.msg.metaMaskNotFound'));
      }
    }
  }
}
</script>
<style scoped>
.login{
  background-color: #545c64;
  border-bottom: solid 1px var(--el-menu-border-color);
}
</style>
