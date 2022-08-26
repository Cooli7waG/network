<template>
  <el-container style="height: auto">
    <div class="contentDiv" v-loading="loading">
      <div class="titleDiv">Arkreen Website</div>
      <div class="btnDiv" style="padding: 10px;">You Game Miner Address:</div>
      <div class="btnDiv" style="padding: 10px;">{{ minerForm.minerAddress }}</div>
      <div class="btnDiv">
        <el-button type="primary" @click="handleClaimGameMiner" :disabled="userAddress==null">Claim</el-button>
      </div>
    </div>
  </el-container>
</template>

<script>
import {claimGameMiner} from "@/api/miners";
import {Base64} from "js-base64";
import {getMetaMaskLoginUserAddress, personalSign,personalEcRecover} from "@/api/metamask_utils";

export default {
  name: 'Arkreen',
  data() {
    return {
      loading:false,
      labelPosition:'top',
      centerDialogVisible: false,
      userAddress:undefined,
      minerForm: {
        version: 1,
        ownerAddress: undefined,
        minerAddress: undefined,
        signature:undefined
      }
    };
  },
  created() {
    this.getUserAddress();
    this.handleGetMinerAddress();
  },
  methods: {
    handleGetMinerAddress(){
      let str = this.$route.params.address;
      let obj = JSON.parse(Base64.decode(str));
      this.minerForm.minerAddress = obj.minerAddress;
      this.minerForm.ownerAddress = obj.ownerAddress;
    },
    async submitForm() {
      this.loading = true;
      try {
        //let message = this.minerForm.minerAddress + "(" + this.minerForm.userAddress + ") Request Game Miner";
        //let message = JSON.stringify(this.minerForm);
        let message = '{"version":1,"ownerAddress":"'+this.minerForm.ownerAddress+'","minerAddress":"'+this.minerForm.minerAddress+'"}'
        this.minerForm.signature = await personalSign(message);
        //
        message = '{"version":1,"ownerAddress":"'+this.minerForm.ownerAddress+'","minerAddress":"'+this.minerForm.minerAddress+'","signature":"'+this.minerForm.signature+'"}'
        claimGameMiner(message).then(rsp => {
          console.log("claimGameMiner result:" + JSON.stringify(rsp))
          this.minerForm.signature = undefined;
          if(rsp.code == 0){
            this.$message.success("game miner claim success!");
            this.$router.push("/account/" + this.minerForm.ownerAddress)
          }else {
            this.$message.error(rsp.msg);
          }
        })
      }catch (err){
        this.$message.error("claim failed, please try again!");
      }
      this.loading = false;
    },
    handleClaimGameMiner() {
      this.$confirm('Click "OK" to confirm the receipt', 'Tips', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        this.submitForm();
      }).catch(() => {

      });
    },
    getUserAddress() {
      this.userAddress = getMetaMaskLoginUserAddress()
      if(this.userAddress == undefined || this.userAddress == null){
        this.$message.error(this.$t('common.msg.notLoginWithMetaMask'));
        return;
      }
    }
  },
}
</script>
<style lang="scss" scoped>
.contentDiv {
  margin: 0 auto;
  background-color: #e7eaf3;
  height: 250px;
  border-bottom-left-radius: 5px;
  border-bottom-right-radius: 5px;
}

.btnDiv {
  height: 50px;
  text-align: center;
  line-height: 50px;
}

.titleDiv {
  width: 100%;
  background-color: #545c64;
  height: 50px;
  border-top-left-radius: 5px;
  border-top-right-radius: 5px;
  text-align: center;
  line-height: 50px;
}
</style>
