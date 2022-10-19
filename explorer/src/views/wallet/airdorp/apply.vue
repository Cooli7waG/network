<template>
  <el-container v-loading="loading" :element-loading-text="LoadingText" style="height: auto">
    <div class="contentDiv">
      <div class="titleDiv">Request a GamingMiner</div>
      <div class="btnDiv">Please confirm your account address:</div>
      <div class="btnDiv">{{ userAddress }}</div>
      <div class="btnDiv">Please input your email address:</div>
      <div class="btnDiv">
        <el-form :model="minerForm" :rules="rules" :label-position="labelPosition" ref="minerForm" >
          <el-form-item prop="email" style="margin: 0px 15px">
            <el-input v-model="minerForm.email" placeholder="Please enter the email"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div class="msgClass" :class="{success:msgActive,failed:!msgActive}">{{ resultMsg }}</div>
      <div class="btnDiv" style="margin-top: 10px">
        <el-button type="primary" @click="submitForm('minerForm')" :disabled="userAddress==null||!applyActive||loading">Request</el-button>
      </div>
    </div>
  </el-container>

</template>

<script>
import {applyGameMiner,getApplyActiveInfo} from "@/api/miners";
import {
  getMetaMaskLoginUserAddress,
  personalSign, switchNetwork
} from "@/api/metamask_utils";
import MetaMaskOnboarding from "@metamask/onboarding";

export default {
  name: 'Request a GamingMiner',
  data() {
    return {
      loading: false,
      LoadingText:"loading...",
      dialogLoading: false,
      msgActive: false,
      resultMsg: undefined,
      applyActive: true,
      applyPersonalSign: false,
      labelPosition: 'top',
      centerDialogVisible: false,
      userAddress: null,
      minerForm: {
        name: undefined,
        email: undefined,
        owner: undefined,
        personalSign: undefined
      },
      rules: {
        email: [
          {required: true, trigger: "blur", message: this.$t("common.msg.PleaseEnterYourEmail")},
          {type: 'email', message: this.$t("common.msg.PleaseEnterTheCorrectEmailAddress"), trigger: ['blur', 'change']}
        ]
      },
    };
  },
  created() {
    this.handleGetApplyActiveInfo();
    this.getUserAddress();
  },
  methods: {
    submitForm(formName) {
      if (!this.applyActive) {
        this.$message.error(this.$t("common.msg.airdropEventNotStart"));
        return;
      }
      //
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.LoadingText = "Applying, please wait...";
          try {
            // 先判断用户是否安装MetaMask
            if(!MetaMaskOnboarding.isMetaMaskInstalled()){
              this.$message.error(this.$t('common.msg.metaMaskNotFound'));
              return;
            }
            //
            this.loading = true;
            if(!await switchNetwork("0x13881")){
              this.$message.error(this.$t('common.msg.metaMaskNetWorkNotFound'));
              this.loading = false;
              return;
            }
            //
            this.minerForm.owner = this.userAddress;
            try {
              if(this.applyPersonalSign){
                let message = JSON.stringify(this.minerForm);
                this.minerForm.personalSign = await personalSign(message);
              }
            }catch (err){
              this.loading = false;
              this.resultMsg = "signature failed,please try again!";
              if(err.code == 4001){
                this.resultMsg = "User denied message signature.";
              }
              return ;
            }
            //
            applyGameMiner(JSON.stringify(this.minerForm)).then(rsp => {
              if (rsp.code == 0) {
                this.centerDialogVisible = false
                this.loading = false;
                this.LoadingText = "loading...";
                this.msgActive = true;
                this.resultMsg = "apply successful, please wait for the mail patiently!";
                this.minerForm.email = undefined;
              } else {
                this.loading = false;
                this.LoadingText = "loading...";
                this.msgActive = false;
                this.resultMsg = rsp.msg;
              }
            })
            this.minerForm.personalSign = undefined;
          } catch (err) {
            this.$message.error("apply failed, please try again!");
          }
        } else {
          this.loading = false;
          this.LoadingText = "loading...";
          return false;
        }
      });
    },
    gotoMinerList() {
      this.$router.push("/wallet/miners")
    },
    getUserAddress() {
      this.userAddress = getMetaMaskLoginUserAddress()
      if (this.userAddress == undefined || this.userAddress == null) {
        this.$message.error(this.$t('common.msg.notLoginWithMetaMask'));
        return;
      }
    },
    changeApplyActive() {
      this.applyActive = !this.applyActive;
    },
    handleGetApplyActiveInfo(){
      getApplyActiveInfo().then(rsp => {
        if (rsp.code == 0) {
          this.loading = false;
          //this.applyActive =  rsp.data.applyActive;
          this.applyPersonalSign = rsp.data.applyPersonalSign;
          //this.applyPersonalSign = true;
        } else {
          this.loading = false;
        }
      })
    }
  },
}
</script>
<style lang="scss" scoped>
.msgClass{
  height: 40px;
  text-align: center;
  line-height: 40px;
  &.success {
    color: green;
  }
  &.failed{
    color: red;
  }
}
.contentDiv {
  margin: 0 auto;
  background-color: #e7eaf3;
  border-bottom-left-radius: 5px;
  border-bottom-right-radius: 5px;
  padding-bottom: 20px;
}

.btnDiv {
  height: 40px;
  text-align: center;
  line-height: 40px;
}

.titleDiv {
  width: 500px;
  background-color: #545c64;
  height: 50px;
  border-top-left-radius: 5px;
  border-top-right-radius: 5px;
  text-align: center;
  line-height: 50px;
  padding: 0px 15px;
}
</style>
