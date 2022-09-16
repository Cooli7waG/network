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
        <div style="height: 59px;line-height: 59px;padding-left: 15px">
          <img src="../assets/logo-arkreen.png" style="width: 40px;height: 40px;margin-top: 8px;margin-inline-end: 15px;">
        </div>
        <el-menu-item index="/wallet/miners">{{ $t('menus.miners') }}</el-menu-item>
        <el-menu-item index="/wallet/txs">{{ $t('menus.txs') }}</el-menu-item>
      </el-menu>
    </el-col>
    <el-col :span="4" class="login">
      <div style="float: right;margin-right: 10px" v-show="isShow">
        <div style="line-height: 50px">
          <el-button type="text" style="color: #FFFFFF" @click="openDrawer">
            <i class="iconfont icon-qianbao wallet-img"/>
          </el-button>
        </div>
      </div>
    </el-col>
  </el-row>
  <div v-show="drawer" tabindex="-1" class="el-drawer__wrapper" style="z-index: 9999;">
    <div role="document" tabindex="-1" class="el-drawer__container el-drawer__open" @click="closeDrawer">
      <div tabindex="-1" class="el-drawer rtl" style="width: 350px;right: 0;margin-top: 58px" @click="stopPropagation">
        <header id="el-drawer__title" class="el-drawer__header">
          <ul class="box" style="text-align: left">
            <li class="user">
              <a style="cursor: pointer">My Wallet</a>
            </li>
          </ul>
          <span style="cursor: pointer" @click="copyAddress">{{ userAddress == undefined ? "" : formatAddress(userAddress) }}</span>
        </header>
        <div style="height: 1px;width: 100%;background-color: silver"></div>
        <!-- 未登录 -->
        <section class="el-drawer__body" v-if="userAddress==undefined">
          <span>If you don't have a </span>
          <el-tooltip class="item" effect="dark"
                      content="A crypto wallet is an application or hardware device that allows individuals to store and retrieve digital items."
                      placement="top-start">
            <el-button style="border: 0px #FFFFFF solid;font-size: 16px"><b>wallet</b></el-button>
          </el-tooltip>
          <span>yet, you can select a provider and create one now.</span>
          <div style="margin-top: 25px">
            <el-row class="login_select">
              <el-col :span="18">
                <img src="../assets/metamask-logo.png" style="width: 25px;vertical-align:sub">
                <span style="margin-left: 10px"><b>MetaMask</b></span>
              </el-col>
              <el-col :span="5">
                <el-button type="primary" round @click="loginApp">Popular</el-button>
              </el-col>
            </el-row>
          </div>
        </section>
        <!-- 已登录 -->
        <section class="el-drawer__body" v-else>
          <div v-loading="loadBalance" style="border-radius: 5px;border: 1px solid rgb(229, 232, 235);">
            <el-row>
              <el-col :span="11" class="balance-div">
                <div><span style="color: #72767b;font-size: 12px">Mining Reward</span></div>
                <div><span style="font-size: 18px">{{user.earningMint}}</span></div>
              </el-col>
              <el-col :span="2" class="balance-div">

              </el-col>
              <el-col :span="11" class="balance-div">
                <div><span style="color: #72767b;font-size: 12px">AKRE Balance</span></div>
                <div><span style="font-size: 18px">{{user.balance}}</span></div>
              </el-col>
              <el-col :span="24" class="transfer-btn" @click="transferBalance">
                <span style="font-size: 16px;line-height: 30px">Withdraw Mining Reward</span>
              </el-col>
            </el-row>
          </div>
          <div style="margin-top: 25px">
            <el-row class="login_select btn_xenon" @click="gotoApplyGameMiner">
              <el-col :span="24">
                <el-icon><Promotion /></el-icon>
                <span style="margin-left: 5px"><b>Request a GameMiner</b></span>
              </el-col>
            </el-row>
            <el-row class="login_select btn_xenon" @click="gotoBrowser">
              <el-col :span="24">
                <i class="iconfont icon-liulanqi" style="color: black"></i>
                <span style="margin-left: 5px"><b>Arkreen Explorer</b></span>
              </el-col>
            </el-row>
            <el-row class="login_select btn_xenon" @click="addAkreToken">
              <el-col :span="24">
                <el-icon><FolderAdd /></el-icon>
                <span style="margin-left: 10px"><b>Add Token to MetaMask</b></span>
              </el-col>
            </el-row>
            <el-row class="login_select btn_xenon" @click="Logout">
              <el-col :span="24">
                <i class="iconfont icon-tuichu" style="color: black"></i>
                <span style="margin-left: 10px"><b>Log out</b></span>
              </el-col>
            </el-row>
          </div>
        </section>
      </div>
    </div>
  </div>
  <!-- 用户未登录 -->
  <div v-if="userAddress==undefined" class="notLogin">
    <el-empty description="Please login to your wallet first" >
      <el-button type="primary" @click="drawer = true">Login Wallet</el-button>
    </el-empty>
  </div>
  <!-- 用户隐私协议对话框 -->
  <el-dialog v-model="dialogVisible" :show-close=false title="" width="30%" center style="z-index: 999">
    <div style="text-align: center;margin-top: -20px">
      <h1>Welcome to ARKREEN</h1>
    </div>
    <div style="text-align: center">
      <img src="../assets/img/acb78eda-89b9-4e9d-a20d-46ba09b8e612.png" style="width: 200px;">
    </div>
    <div style="margin-top: 15px;font-weight: bold">
      By connecting your wallet and using ARKREEN,you agree to our <span class="privacySpan">Terms of Service</span> and <span class="privacySpan">Privacy Policy</span>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false" :disabled="loading">Cancel</el-button>
        <el-button type="primary" @click="loginMetaMask" :disabled="loading">Acccpt and sign</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import {formatString, getTokenFixed} from "@/utils/data_format";
import {
  addToken,
  getMetaMaskLoginUserAddress,
  loginWithMetaMask, isPrivacyPolicy,
  removeMetaMaskUserAddress, switchNetwork, setPrivacyPolicy,
} from "@/api/metamask_utils";
import {findByAddress} from "@/api/account";
import {toEther} from "@/utils/utils";
import MetaMaskOnboarding from '@metamask/onboarding';

export default {
  name: 'WalletMenus',
  props: {
    msg: String
  },
  data() {
    return {
      loading:false,
      dialogVisible:false,
      drawer: false,
      direction: 'rtl',
      isShow: true,
      loadBalance: false,
      userAddress: undefined,
      user:{
        earningMint:0,
        balance:0
      }
    }
  },
  created() {
    this.getInfo();
    this.listenMetaMask();
  },
  methods: {
    transferBalance(){
      this.$message.warning("coming soon...")
    },
    loadFindByAddress(){
      this.loadBalance = true;
      const userAddress = getMetaMaskLoginUserAddress();
      if(userAddress == undefined || userAddress == null){
        return;
      }
      findByAddress(getMetaMaskLoginUserAddress()).then((result)=>{
        if(result.code == 0){
          this.user.earningMint = result.data.earningMint.toLocaleString();
          const fixed = getTokenFixed(result.data.balance);
          this.user.balance = toEther(result.data.balance,fixed);
        }
        this.loadBalance = false;
      }).catch((err) =>{
        console.log(err);
        this.loadBalance = false;
      });

    },
    copyAddress() {
      const input = document.createElement('input')
      input.value = this.userAddress;
      document.body.appendChild(input);
      input.select();
      document.execCommand("Copy");
      document.body.removeChild(input);
      this.$message.success("Copied!")
    },
    stopPropagation(event) {
      event.stopPropagation();
    },
    closeDrawer() {
      this.drawer = false
    },
    openDrawer() {
      this.drawer = true
      this.loadFindByAddress();
    },
    formatAddress(address) {
      return address.substring(0, 6) + "..." + address.substring(address.length - 4)
    },
    autoActive() {
      let path = this.$route.path;
      if (path.startsWith("/wallet/miner/")) {
        return "/wallet/miners"
      }
      if (path.startsWith("/wallet/account/")) {
        return "/wallet/accounts"
      }
      if (path.startsWith("/wallet/tx/")) {
        return "/wallet/txs"
      }
      if (path == "/wallet") {
        return "/wallet/miners"
      }
      return path;
    },
    gotoMyMiner() {
      this.closeDrawer()
      this.$router.push("/wallet/miners")
    },
    gotoBrowser() {
      this.closeDrawer()
      this.$router.push("/")
    },
    gotoApplyGameMiner(){
      this.$router.push("/wallet/apply");
      this.closeDrawer();
    },
    Logout() {
      this.$confirm('Are you sure want to logout?', 'Tips', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        removeMetaMaskUserAddress()
        this.closeDrawer()
        this.userAddress = undefined;
        this.$router.push("/wallet?t="+new Date().getMilliseconds());
      }).catch(() => {

      });
    },
    getInfo() {
      this.userAddress = getMetaMaskLoginUserAddress();
    },
    async addAkreToken() {
      const result = await addToken();
      if(result){
        this.$message.success("success!");
      }else {
        this.$message.error("failed!");
      }
    },
    formatString(str) {
      return formatString(str, 15);
    },
    async loginApp() {
      // 先判断用户是否安装MetaMask
      if(!MetaMaskOnboarding.isMetaMaskInstalled()){
        this.$message.error(this.$t('common.msg.metaMaskNotFound'));
        return;
      }
      //
      if(!await switchNetwork("0x13881")){
        this.$message.error(this.$t('common.msg.metaMaskNetWorkNotFound'));
        return;
      }
      //首次使用弹出确认对话框
      if(isPrivacyPolicy()){
        await this.loginMetaMask();
      }else {
        this.dialogVisible = true;
        this.closeDrawer();
      }
    },
    async loginMetaMask() {
      this.loading = true;
      setPrivacyPolicy(true);
      this.dialogVisible = false;
      if (this.userAddress != undefined) {
        this.$router.push("/wallet?t="+new Date().getMilliseconds());
      }
      try {
        if (window.ethereum) {
          this.userAddress = await loginWithMetaMask();
          this.closeDrawer();
          let path = this.$route.path;
          if (path == "/wallet") {
            this.$router.push("/wallet?t="+new Date().getMilliseconds());
          } else if (path.startsWith("/wallet/claim/")) {
            this.$router.go(0);
          } else {
            this.$router.push("/wallet?t="+new Date().getMilliseconds());
          }
        } else {
          this.$message.error(this.$t('common.msg.metaMaskNotFound'));
        }
        this.loading = false;
      } catch (err) {
        this.loading = false;
        this.$message.error(this.$t('common.msg.LoginWithMetaMaskFailed'));
      }
    },
    // 监听到用户退出登录时直接返回首页
    async listenMetaMask(){
      // 先判断用户是否安装MetaMask
      if(MetaMaskOnboarding.isMetaMaskInstalled()){
        // eslint-disable-next-line no-undef
        ethereum.on('accountsChanged', () => {
          if(this.userAddress != undefined){
            removeMetaMaskUserAddress();
            //this.$router.push("/wallet");
            this.$router.push("/wallet?t="+new Date().getMilliseconds());
          }
        });
      }
    }
  }
}
</script>
<style scoped>
.notLogin{
  width: 100%;
  height: 850px;
  background-color: #FFFFFF;
}
.balance-div{
  height: 80px;
  text-align: center;
  padding-top: 15px;
}
.transfer-btn{
  height: 30px;
  text-align: center;
  background-color: #ffd04b;
  border-bottom-left-radius: 5px;
  border-bottom-right-radius: 5px;
  cursor: pointer;
  user-select:none;
}
.privacySpan{
  color: #409EFF;
  cursor: pointer
}
.btn_xenon {
  text-align: center;
  cursor: pointer;
  background-color: aliceblue !important;
}
.btn_xenon:hover {
  background-color: rgb(83, 168, 255) !important;
}
.login_select {
  height: 45px;
  line-height: 45px;
  background-color: aliceblue;
  padding-right: 20px;
  padding-left: 6px;
  border-radius: 5px;
  margin-top: 10px;
  z-index: 99;
}

.el-drawer__open .el-drawer.rtl {
  animation: rtl-drawer-in .4s 0ms;
}

@keyframes rtl-drawer-out {
  0% {
    transform: translate(0)
  }
  to {
    transform: translate(100%)
  }
}

@keyframes rtl-drawer-in {
  0% {
    transform: translate(100%)
  }
  to {
    transform: translate(0)
  }
}

.el-drawer {
  position: absolute;
  box-sizing: border-box;
  background-color: #fff;
  display: flex;
}

.el-drawer__container {
  position: relative;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  height: 100%;
  width: 100%;
}

.el-drawer__header {
  align-items: center;
  color: #72767b;
  display: flex;
  margin-bottom: 2px !important;
  padding: var(--el-drawer-padding-primary);
  padding-bottom: 0;
}

.el-drawer__wrapper {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  overflow: hidden;
  margin: 0;
  background-color: rgba(0, 0, 0, 0.3);
}

.wallet-img {
  font-size: 26px;
}

.wallet-img:hover {
  color: silver;
}

.box {
  list-style: none;
  width: 150px;
}

.down-menu {
  list-style: none;
  padding: 5px 0px;
}

a {
  text-decoration: none;
  color: #000000;
  font-weight: bold;
}
li {
  text-align: left;
}
ul {
  padding-inline-start: 2px !important;
}
ul.box > li.user {
  width: 150px;
  height: 20px;
  z-index: 99999;
}

ul.box > li.user > a {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: left;
  align-items: center;

}

ul.box > li.user:hover .down-menu {
  display: block;
  background-color: silver;
}

ul.box > li > .down-menu {
  background: #FFFFFF;
  width: 150px;
  display: none;
  border-radius: 3px;
}

ul.box > li > .down-menu > li {
  padding: 3px 0px;
  justify-content: center;
  align-items: center;
  text-align: center;
  height: 40px;
  line-height: 40px;
  border-bottom: 1px solid rgb(229, 232, 235);
  cursor: pointer;
  padding: 0px 0px;
}

ul.box > li > .down-menu > li:hover {
  background-color: silver;
}

.login {
  background-color: #545c64;
  border-bottom: solid 1px var(--el-menu-border-color);
}
.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}
.el-icon-arrow-down {
  font-size: 12px;
}
</style>
