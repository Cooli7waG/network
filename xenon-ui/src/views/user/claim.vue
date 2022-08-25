<template>
  <el-container style="height: auto">
    <div class="contentDiv">
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
import Buffer from "vue-buffer";
import {Base64} from "js-base64";

export default {
  name: 'Arkreen',
  data() {
    return {
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
      console.log("this.$route.params.address:"+this.$route.params.address)
      let str = this.$route.params.address;
      let obj = JSON.parse(Base64.decode(str));
      console.log("minerAddress:"+obj.minerAddress)
      console.log("ownerAddress:"+obj.ownerAddress)
      this.minerForm.minerAddress = obj.minerAddress;
      this.minerForm.ownerAddress = obj.ownerAddress;
    },
    async submitForm() {
      if (window.ethereum) {
        const newAccounts = await ethereum.request({
          method: 'eth_requestAccounts',
        });
        this.userAddress = newAccounts[0];
      } else {
        this.$message.error(this.$t('common.msg.metaMaskNotFound'));
        return;
      }
      //let message = this.minerForm.minerAddress + "(" + this.minerForm.userAddress + ") Request Game Miner";
      //let message = JSON.stringify(this.minerForm);
      let message = '{"version":1,"ownerAddress":"'+this.minerForm.ownerAddress+'","minerAddress":"'+this.minerForm.minerAddress+'"}'
      console.log("message:" + message)
      console.log("this.userAddress:" + this.userAddress)
      const msg = `0x${Buffer.from(message, 'utf8').toString('hex')}`;
      const sign = await ethereum.request({
        method: 'personal_sign',
        params: [msg, this.userAddress, ''],
      });
      //
      console.log("personalSign result:" + sign)
      this.minerForm.signature = sign;
      message = '{"version":1,"ownerAddress":"'+this.minerForm.ownerAddress+'","minerAddress":"'+this.minerForm.minerAddress+'","signature":"'+this.minerForm.signature+'"}'
      claimGameMiner(message).then(rsp => {
        console.log("claimGameMiner result:" + JSON.stringify(rsp))
        this.minerForm.signature = undefined;
      })
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
    async getUserAddress() {
      if (window.ethereum) {
        const newAccounts = await ethereum.request({
          method: 'eth_requestAccounts',
        });
        this.userAddress = newAccounts[0];
        //
        console.log("user address:"+this.userAddress)
      } else {
        this.$message.error(this.$t('common.msg.metaMaskNotFound'));
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
