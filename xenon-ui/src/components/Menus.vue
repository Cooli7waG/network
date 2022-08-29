<template>
  <el-row style="background-color: #545c64">
    <el-col :span="20">
      <el-menu
          :default-active="autoActive()"
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
      <div style="float: right;margin-right: 10px" v-show="isShow">
        <div v-if="userAddress==undefined" style="line-height: 50px">
          <el-button type="text" style="color: #FFFFFF" @click="loginApp">{{ $t('menus.login') }}</el-button>
        </div>
        <ul v-else class="box">
          <li class="user">
            <a style="cursor: pointer;">
              <!--<img src="@/assets/metamask-logo.png" style="width: 25px">-->
              <span @click="loginApp">{{ formatString(userAddress) }}</span>
            </a>
            <ul class="down-menu">
              <li @click="gotoBrowser()">Browser</li>
              <li @click="gotoMyMiner()">My Miners</li>
              <li @click="Logout()">Logout</li>
            </ul>
          </li>
        </ul>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import {formatString} from "@/utils/data_format";
import {
  getMetaMaskLoginUserAddress,
  loginWithMetaMask,
  removeMetaMaskUserAddress,
} from "@/api/metamask_utils";

export default {
  name: 'Menus',
  props: {
    msg: String
  },
  data() {
    return {
      isShow: true,
      userAddress: undefined
    }
  },
  created() {
    this.getInfo();
  },
  methods: {
    autoActive(){
      let path = this.$route.path;
      if(path.startsWith("/miner/")){
        return "/miners"
      }
      if(path.startsWith("/account/")){
        return "/accounts"
      }
      if(path.startsWith("/tx/")){
        return "/txs"
      }
      return path;
    },
    gotoMyMiner() {
      this.$router.push("/account/" + this.userAddress)
    },
    gotoBrowser() {
      this.$router.push("/")
    },
    Logout() {
      this.$confirm('Are you sure want to logout?', 'Tips', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        removeMetaMaskUserAddress()
        this.userAddress = undefined;
        this.$router.push("/");
      }).catch(() => {

      });
    },
    getInfo() {
      let MateMaskAddress = getMetaMaskLoginUserAddress()
      this.userAddress = MateMaskAddress;
    },
    formatString(str) {
      return formatString(str, 15);
    },
    async loginApp() {
      if (this.userAddress != undefined) {
        this.$router.push("/user");
      }
      try {
        if (window.ethereum) {
          this.userAddress = await loginWithMetaMask();
          let path = this.$route.path;
          if(path == "/user"){
            this.$router.go(0);
          }else if(path.startsWith("/claim/")){
            this.$router.go(0);
          }else {
            this.$router.push("/user");
          }
        } else {
          this.$message.error(this.$t('common.msg.metaMaskNotFound'));
        }
      }catch (err){
        this.$message.error(this.$t('common.msg.LoginWithMetaMaskFailed'));
      }
    }
  }
}
</script>
<style scoped>
.box{
  list-style: none;
  width: 150px;
}
.down-menu{
  list-style: none;
  padding: 5px 0px;
}
a {
  text-decoration: none;
  color: #fff;
}

ul.box > li.user {
  width: 150px;
  height: 20px;
}

ul.box > li.user > a {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

ul.box > li.user:hover .down-menu {
  display: block;
}

ul.box > li > .down-menu {
  background: #fff;
  width: 150px;
  display: none;
  border: 1px solid silver;
  border-radius: 3px;
}

ul.box > li > .down-menu > li {
  padding: 3px 0px;
  justify-content: center;
  align-items: center;
  text-align: center;
  height: 40px;
  line-height: 40px;
  border-bottom: 1px solid #fff;
  cursor: pointer;
  padding: 0px 20px;
}

ul.box > li > .down-menu > li:hover {
  background-color: silver;
}

.login {
  background-color: #545c64;
  border-bottom: solid 1px var(--el-menu-border-color);
}
</style>
