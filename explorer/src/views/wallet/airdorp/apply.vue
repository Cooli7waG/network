<template>
  <el-container v-loading="loading" style="height: auto">
    <div class="contentDiv">
      <div class="titleDiv">Arkreen Website</div>
      <div class="btnDiv">Please confirm your account address:</div>
      <div class="btnDiv">{{ userAddress }}</div>
      <div v-show="!applyActive" class="btnDiv" style="color: red">{{ $t("common.msg.airdropEventNotStart") }}</div>
      <div v-show="msgActive" class="btnDiv" style="color: red">{{ resultMsg }}</div>
      <div class="btnDiv" style="color: green">{{ successMsg }}</div>
      <div class="btnDiv" style="margin-top: 10px">
        <el-button type="primary" @click="handleApplyGameMiner" :disabled="userAddress==null||!applyActive">Request</el-button>
      </div>
      <!--<div class="btnDiv">
        <el-button type="success" @click="gotoMinerList" :disabled="userAddress==null||!applyActive">View My Miners
        </el-button>
      </div>-->
    </div>
  </el-container>
  <div v-loading="dialogLoading" v-show="centerDialogVisible" class="el-dialog__wrapper" style="background-color: rgba(0,0,0,0.5)">
    <div role="dialog" aria-modal="true" aria-label="提示" class="el-dialog el-dialog--center"
         style="margin-top: 15vh; width: 30%;">
      <div class="el-dialog__header">
        <span class="el-dialog__title">Apply Game Miner</span>
      </div>
      <div class="el-dialog__body">
        <el-form :model="minerForm" :rules="rules" :label-position="labelPosition" ref="minerForm"
                 label-width="100px">
          <el-form-item prop="name">
            <el-input v-model="minerForm.name" placeholder="Please enter the username"></el-input>
          </el-form-item>
          <el-form-item prop="email">
            <el-input v-model="minerForm.email" placeholder="Please enter the email"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div class="el-dialog__footer">
        <span class="dialog-footer">
          <button type="button" class="el-button el-button--default" @click="centerDialogVisible = false">
            <span>Cancel</span>
          </button>
          <button type="button" class="el-button el-button--primary" @click="submitForm('minerForm')">
            <span>Apply</span>
          </button>
        </span>
      </div>
    </div>
  </div>
</template>

<script>
import {applyGameMiner,getApplyActiveInfo} from "@/api/miners";
import {
  addNetwork,
  getMetaMaskLoginUserAddress,
  personalSign,
  signTypedDataV3,
  signTypedDataV4, switchNetwork
} from "@/api/metamask_utils";

export default {
  name: 'apply',
  data() {
    return {
      loading: false,
      dialogLoading: false,
      msgActive: false,
      resultMsg: "Please add or switch to polygon mainnet network first!",
      successMsg: undefined,
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
        name: [
          {required: true, message: 'Please enter the user name', trigger: 'blur'}
        ],
        email: [
          {required: true, trigger: "blur", message: "Please enter your email"},
          {type: 'email', message: 'Please enter the correct email address', trigger: ['blur', 'change']}
        ]
      },
    };
  },
  created() {
    this.handleGetApplyActiveInfo();
    this.getUserAddress();
    //this.handleSignTypedDataV3();
    //this.handleSignTypedDataV4();
    //this.handleAddNetwork();
    //this.handleSwitchNetwork();
  },
  methods: {
    submitForm(formName) {
      this.dialogLoading = true;
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          try {
            //判断是否添加目标网络
            const result = await switchNetwork("0x89");
            if(!result){
              this.resultMsg = "Please add or switch to polygon mainnet network first!"
              this.msgActive = true;
              return ;
            }
            //
            this.minerForm.owner = this.userAddress;
            if(this.applyPersonalSign){
              let message = JSON.stringify(this.minerForm);
              this.minerForm.personalSign = await personalSign(message);
            }
            //
            applyGameMiner(JSON.stringify(this.minerForm)).then(rsp => {
              if (rsp.code == 0) {
                this.centerDialogVisible = false
                this.dialogLoading = false;
                this.msgActive = false;
                this.successMsg = "apply successful, please wait for the mail patiently!";
              } else {
                this.dialogLoading = false;
                this.msgActive = true;
                this.resultMsg = rsp.msg;
              }
            })
            this.minerForm.personalSign = undefined;
          } catch (err) {
            this.$message.error("apply failed, please try again!");
          }
        } else {
          this.dialogLoading = true;
          return false;
        }
      });
    },
    handleApplyGameMiner() {
      if (!this.applyActive) {
        this.$message.error(this.$t("common.msg.airdropEventNotStart"));
      } else {
        this.centerDialogVisible = true
      }
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
        } else {
          this.loading = false;
        }
      })
    },
    async handleSignTypedDataV3() {
      const sign = await signTypedDataV3();
      console.log("metamask signTypedDataV3:" + sign)
    },
    async handleSignTypedDataV4() {
      const sign = await signTypedDataV4();
      console.log("metamask signTypedDataV4:" + sign)
    },
    async handleAddNetwork(){
      const result = await addNetwork();
      console.log("metamask addNetwork:" + result)
    },
    async handleSwitchNetwork(){
      const result = await switchNetwork("0x89");
      console.log("metamask switchNetwork:" + result)
    }
  },
}
</script>
<style lang="scss" scoped>
.contentDiv {
  margin: 0 auto;
  background-color: #e7eaf3;
  height: 300px;
  border-bottom-left-radius: 5px;
  border-bottom-right-radius: 5px;
}

.btnDiv {
  height: 50px;
  text-align: center;
  line-height: 50px;
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
