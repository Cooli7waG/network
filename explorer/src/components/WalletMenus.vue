<template>
  <!-- 头部菜单 -->
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
          <img src="../assets/logo-arkreen.png"
               style="width: 40px;height: 40px;margin-top: 8px;margin-inline-end: 15px;">
        </div>
        <el-menu-item index="/wallet">{{ $t('menus.dashboard') }}</el-menu-item>
        <el-menu-item index="/wallet/miners">{{ $t('menus.miners') }}</el-menu-item>
        <el-menu-item index="/wallet/txs">{{ $t('menus.txs') }}</el-menu-item>
        <el-menu-item index="/wallet/withdraw">{{ $t('menus.withdraw') }}</el-menu-item>
      </el-menu>
    </el-col>
    <el-col :span="4" class="login">
      <div style="float: right;margin-right: 10px" v-show="isShow">
        <div style="line-height: 50px">
          <el-badge :is-dot="withdrawStatus" style="margin-top: 15px;height: 40px">
            <el-button type="text" style="color: #FFFFFF;margin-top: -25px" @click="openDrawer">
              <i class="iconfont icon-qianbao wallet-img"/>
            </el-button>
          </el-badge>
        </div>
      </div>
    </el-col>
  </el-row>
  <!-- 侧滑菜单 -->
  <div v-show="drawer" tabindex="-1" class="el-drawer__wrapper" style="z-index: 9999;">
    <div role="document" tabindex="-1" class="el-drawer__container el-drawer__open" @click="closeDrawer">
      <div tabindex="-1" class="el-drawer rtl" style="width: 350px;right: 0;margin-top: 58px" @click="stopPropagation">
        <header id="el-drawer__title" class="el-drawer__header">
          <ul class="box" style="text-align: left">
            <li class="user">
              <a style="cursor: pointer">My Wallet</a>
            </li>
          </ul>
          <span style="cursor: pointer"
                @click="copyAddress">{{ userAddress == undefined ? "" : formatAddress(userAddress) }}</span>
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
          <!-- Balance展示-->
          <div v-loading="loadBalance" style="border-radius: 5px;border: 1px solid rgb(229, 232, 235);">
            <el-row>
              <el-col :span="11" class="balance-div">
                <div><span style="color: #72767b;font-size: 12px">Mining Reward</span></div>
                <div><span style="font-size: 18px">{{ Number(Number(user.earningMint).toFixed(3)).toLocaleString() }}</span>
                </div>
              </el-col>
              <el-col :span="2" class="balance-div">

              </el-col>
              <el-col :span="11" class="balance-div">
                <div><span style="color: #72767b;font-size: 12px">AKRE Balance</span></div>
                <div><span style="font-size: 18px">{{ Number(Number(user.balance).toFixed(3)).toLocaleString() }}</span>
                </div>
              </el-col>
              <el-col :span="24" class="transfer-btn" @click="transferBalance" :disabled="withdrawStatus">
                <span style="font-size: 16px;line-height: 30px" >Withdraw Mining Reward</span>
              </el-col>
            </el-row>
          </div>
          <div style="text-align: center" v-show="withdrawStatus">
            <span style="font-size: 12px;color: red">Withdrawal failed: </span>
            <span style="font-size: 12px;color: red;cursor: pointer;text-decoration-line: underline" @click="gotoRouter('/wallet/withdraw')">retry</span>
          </div>
          <!-- 菜单项 -->
          <div style="margin-top: 25px">
            <el-row class="login_select btn_xenon" @click="gotoRouter('/wallet/apply')">
              <el-col :span="24">
                <el-icon>
                  <Promotion/>
                </el-icon>
                <span style="margin-left: 5px"><b> Request a GameMiner</b></span>
              </el-col>
            </el-row>
            <el-row class="login_select btn_xenon" @click="gotoRouter('/')">
              <el-col :span="24">
                <i class="iconfont icon-liulanqi" style="color: black"></i>
                <span style="margin-left: 5px"><b> Arkreen Explorer</b></span>
              </el-col>
            </el-row>
            <el-row class="login_select btn_xenon" @click="addAkreToken">
              <el-col :span="24">
                <el-icon>
                  <FolderAdd/>
                </el-icon>
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
    <el-empty description="Please login to your wallet first">
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
      By connecting your wallet and using ARKREEN,you agree to our <span class="privacySpan">Terms of Service</span> and
      <span class="privacySpan">Privacy Policy</span>
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
import {formatString} from "@/utils/data_format";
import {
  addToken, personalSign,
  getMetaMaskLoginUserAddress,
  loginWithMetaMask, isPrivacyPolicy,
  removeMetaMaskUserAddress, switchNetwork, setPrivacyPolicy
} from "@/api/metamask_utils";
import {accountInfo, withdraw} from "@/api/account";
import {hexToBytes} from "@/utils/utils";
import MetaMaskOnboarding from '@metamask/onboarding';
import {balanceOf, etherNonces, etherWithdraw, getTransactionStatus} from '@/api/contract_utils'
import bs58 from 'bs58'

export default {
  name: 'WalletMenus',
  props: {
    msg: String
  },
  data() {
    return {
      loading: false,
      dialogVisible: false,
      drawer: false,
      direction: 'rtl',
      isShow: true,
      loadBalance: false,
      userAddress: undefined,
      isInit: false,
      withdrawStatus: false,
      user: {
        earningMint: 0,
        balance: 'loading...'
      }
    }
  },
  created() {
    this.getInfo();
    this.listenMetaMask();
    this.loadFindByAddress();
  },
  methods: {
    async checkWithdraw(nonce) {
      let newNonce = await etherNonces();
      if(nonce != newNonce){
        this.withdrawStatus = true
      }else {
        this.withdrawStatus = false
      }
    },
    async transferBalance() {
      if(!await switchNetwork("0x13881")){
        this.$message.error(this.$t('common.msg.metaMaskNetWorkNotFound'));
        this.loading = false;
        return;
      }else {
        this.handleWithdraw();
      }
    },
    handleWithdraw() {
      this.loadBalance = true;
      accountInfo(getMetaMaskLoginUserAddress()).then(async (result) => {
        if (result.code == 0) {
          this.user.earningMint = result.data.earningMint
          if(result.data.earningMint<=0){
            this.loadBalance = false;
            this.$message.error("Not have earning mint that can be withdraw!")
            return;
          }

          const form = await this.handlePersonalSign(result.data.earningMint);
          console.log("withdraw request data:" + JSON.stringify(form))
          if(form == undefined){
            this.loadBalance = false;
            this.$message.error("Please unlock metamask and try again!")
            return;
          }
          withdraw(form).then(async rsp => {
            console.log(rsp)
            if (rsp.code == 0) {
              let originData = rsp.data.originData;
              let sig = rsp.data.sig;
              console.log("withdraw server result:" + JSON.stringify(rsp.data))
              try {
                let hash = await etherWithdraw(""+originData.value, originData.nonce, sig.v, "0x"+sig.r, "0x"+sig.s)
                console.info("withdraw hash Result hash:" + hash)
                this.$message.info("The withdraw has been submitted. Please wait...")
                let status = await getTransactionStatus(hash);
                console.log('Transaction Status: ' + status);
                if (status == 0) {
                  this.$message.error("withdraw failed")
                } else {
                  this.$message.success("withdraw success,Please check your wallet!")
                  this.getInfo();
                  this.loadBalance = true;
                  await this.loadFindByAddress();
                }
              } catch (err) {
                let str = 'Error: user rejected transaction';
                if (err.toString().indexOf(str) != -1) {
                  this.$message.warning("user rejected transaction")
                }
                console.log(err)
              }
            } else {
              this.loadBalance = false;
              this.$message.error("withdraw failed:" + rsp.msg)
            }
          })
        } else {
          this.loadBalance = false;
          this.$message.error("Failed to query the latest reward data,Please try again!")
        }
      }).catch((err) => {
        console.log(err);
        this.loadBalance = false;
      });
    },
    async handlePersonalSign(amount) {
      try {
        const form = {
          address: getMetaMaskLoginUserAddress(),
          amount: amount
        }
        let message = JSON.stringify(form);
        console.log("Withdraw Personal Sign Message:" + message)
        const requestSig = await personalSign(message);
        let str = requestSig.substring(2);
        console.log("Withdraw Personal Sign Result:" + str)
        form.requestSig = bs58.encode(hexToBytes(str))
        return form;
      }catch (err){
        return undefined;
      }
    },
    async loadFindByAddress() {
      if (!this.isInit) {
        this.loadBalance = true;
      }
      const userAddress = getMetaMaskLoginUserAddress();
      if (userAddress == undefined || userAddress == null) {
        return;
      }
      accountInfo(getMetaMaskLoginUserAddress()).then((result) => {
        if (result.code == 0) {
          this.user.earningMint = result.data.earningMint.toLocaleString();
          this.isInit = true
          this.checkWithdraw(result.data.nonce);
        }
        this.loadBalance = false;
      }).catch((err) => {
        console.log(err);
        this.loadBalance = false;
      });
      try {
        let balance = await balanceOf()
        this.user.balance = balance / Math.pow(10, 18)
      }catch (err){
        this.user.balance = '0'
      }
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
      if (path.startsWith("/wallet/withdraw/")) {
        return "/wallet/withdraw"
      }
      if (path == "/wallet") {
        return "/wallet"
      }
      return path;
    },
    gotoRouter(router) {
      this.closeDrawer()
      this.$router.push(router)
    },
    getInfo() {
      this.userAddress = getMetaMaskLoginUserAddress();
    },
    async addAkreToken() {
      const result = await addToken();
      if (result) {
        this.$message.success("success!");
      } else {
        this.$message.error("failed!");
      }
    },
    formatString(str) {
      return formatString(str, 15);
    },
    async loginApp() {
      // 先判断用户是否安装MetaMask
      if (!MetaMaskOnboarding.isMetaMaskInstalled()) {
        this.$message.error(this.$t('common.msg.metaMaskNotFound'));
        return;
      }
      //
      if (!await switchNetwork("0x13881")) {
        this.$message.error(this.$t('common.msg.metaMaskNetWorkNotFound'));
        return;
      }
      //首次使用弹出确认对话框
      if (isPrivacyPolicy()) {
        await this.loginMetaMask();
      } else {
        this.dialogVisible = true;
        this.closeDrawer();
      }
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
        this.$router.push("/wallet?t=" + new Date().getMilliseconds());
      }).catch(() => {

      });
    },
    async loginMetaMask() {
      this.loading = true;
      setPrivacyPolicy(true);
      this.dialogVisible = false;
      if (this.userAddress != undefined) {
        this.$router.push("/wallet?t=" + new Date().getMilliseconds());
      }
      try {
        if (window.ethereum) {
          this.userAddress = await loginWithMetaMask();
          this.closeDrawer();
          let path = this.$route.path;
          if (path == "/wallet") {
            this.$router.push("/wallet?t=" + new Date().getMilliseconds());
          } else if (path.startsWith("/wallet/claim/")) {
            this.$router.go(0);
          } else {
            this.$router.push("/wallet?t=" + new Date().getMilliseconds());
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
    async listenMetaMask() {
      // 先判断用户是否安装MetaMask
      if (MetaMaskOnboarding.isMetaMaskInstalled()) {
        // eslint-disable-next-line no-undef
        ethereum.on('accountsChanged', async () => {
          if (this.userAddress != undefined) {
            removeMetaMaskUserAddress();
            this.userAddress = undefined;
            this.$router.push("/wallet?t=" + new Date().getMilliseconds());
          } else {
            this.userAddress = await loginWithMetaMask();
          }
        });
      }
    }
  }
}
</script>
<style scoped>
.el-badge__content {
  height: 5px !important;
  width: 5px !important;
  padding: 0;
  right: 0;
  border-radius: 50%;
}
.notLogin {
  width: 100%;
  height: 850px;
  background-color: #FFFFFF;
}

.balance-div {
  height: 80px;
  text-align: center;
  padding-top: 15px;
}

.transfer-btn {
  height: 30px;
  text-align: center;
  background-color: #ffd04b;
  border-bottom-left-radius: 5px;
  border-bottom-right-radius: 5px;
  cursor: pointer;
  user-select: none;
}

.privacySpan {
  color: #409EFF;
  cursor: pointer
}

.btn_xenon {
  text-align: left;
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
