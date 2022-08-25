<template>
  <el-container v-loading="loading" style="height: auto">
    <div class="contentDiv">
      <div class="titleDiv">Arkreen Website</div>
      <div class="btnDiv">You Account Address:</div>
      <div class="btnDiv">{{ userAddress }}</div>
      <div v-show="!applyActive" class="btnDiv" style="color: red">{{$t("common.msg.airdropEventNotStart")}}</div>
      <div class="btnDiv">
        <el-button type="primary" @click="handleApplyGameMiner" :disabled="userAddress==null||!applyActive">Apply Game Miner</el-button>
      </div>
      <div class="btnDiv">
        <el-button type="success" @click="gotoMinerList" :disabled="userAddress==null||!applyActive">View My Miners</el-button>
      </div>
    </div>
  </el-container>
  <div v-loading="loading" v-show="centerDialogVisible" class="el-dialog__wrapper" style="z-index: 9999;background-color: rgba(0,0,0,0.5)">
    <div role="dialog" aria-modal="true" aria-label="提示" class="el-dialog el-dialog--center" style="margin-top: 15vh; width: 30%;">
      <div class="el-dialog__header">
        <span class="el-dialog__title">Apply Game Miner</span>
      </div>
      <div class="el-dialog__body">
        <el-form :model="minerForm" :rules="rules" size="medium" :label-position="labelPosition" ref="minerForm" label-width="100px">
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
import {applyGameMiner} from "@/api/miners";
import {getMetaMaskLoginUserAddress, personalSign} from "@/api/metamask_utils";

export default {
  name: 'Arkreen',
  data() {
    return {
      loading : false,
      applyActive:false,
      labelPosition:'top',
      centerDialogVisible: false,
      userAddress: null,
      minerForm: {
        name: 'test',
        email: 'test@qq.com',
        personalSign:undefined
      },
      rules: {
        name: [
          { required: true, message: 'Please enter the user name', trigger: 'blur' }
        ],
        email: [
          { required: true, trigger: "blur", message: "Please enter your email" },
          { type: 'email', message: 'Please enter the correct email address', trigger: ['blur', 'change'] }
        ]
      },
    };
  },
  created() {
    this.getUserAddress();
  },
  methods: {
    submitForm(formName) {
      this.loading = true;
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          try {
            let message = this.minerForm.name + "(" + this.minerForm.email + ") Request Game Miner";
            this.minerForm.personalSign = await personalSign(message);;
            applyGameMiner(this.minerForm).then(rsp =>{
              console.log("applyGameMiner result:"+JSON.stringify(rsp))
              if(rsp.code == 0){
                this.centerDialogVisible = false
                this.loading = false;
                this.$message.success("apply successful, please wait for the mail patiently");
              }else {
                this.loading = false;
                this.$message.error(rsp.msg);
              }
            })
          }catch (err){
            this.$message.error("apply failed, please try again!");
          }
        } else {
          this.loading = true;
          return false;
        }
      });
    },
    handleApplyGameMiner() {
      if(!this.applyActive){
        this.$message.error(this.$t("common.msg.airdropEventNotStart"));
      }else {
        this.centerDialogVisible = true
      }
    },
    gotoMinerList() {
      this.$router.push("/account/" + this.userAddress)
    },
    getUserAddress() {
      this.userAddress = getMetaMaskLoginUserAddress()
      if(this.userAddress == undefined || this.userAddress == null){
        this.$message.error(this.$t('common.msg.notLoginWithMetaMask'));
        return;
      }
    },
    changeApplyActive(){
      this.applyActive = !this.applyActive;
    }
  },
}
</script>
<style lang="scss" scoped>
.contentDiv {
  margin: 0 auto;
  background-color: #e7eaf3;
  height: 350px;
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
