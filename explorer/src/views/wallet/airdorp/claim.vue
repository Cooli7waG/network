<template>
  <el-container style="height: auto">
    <div class="contentDiv" v-loading="loading">
      <div class="titleDiv">Arkreen Website</div>
      <div class="btnDiv" style="padding: 10px;">Your GameMiner Address :</div>
      <div class="btnDiv" style="padding: 10px;">{{ minerForm.minerAddress }}</div>
      <div class="btnDiv">
        <el-button type="primary" @click="handleClaimGameMiner" :disabled="userAddress==null">Claim</el-button>
      </div>
      <div v-show="false" class="btnDiv">
        <el-button type="primary" @click="testNft" :disabled="userAddress==null">NFT</el-button>
      </div>
    </div>
  </el-container>
  <el-dialog
      v-model="dialogVisible"
      title="For NFT enthusiasts"
      width="35%"
      :before-close="handleClose">
    <span>{{dialogMsg}}</span>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">Cancel</el-button>
        <el-button type="primary" @click="mintNft">Continue</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import {claimGameMiner,nftsign} from "@/api/miners";
import {Base64} from "js-base64";
import {getMetaMaskLoginUserAddress, personalSign} from "@/api/metamask_utils";
import {etherGameMinerOnboard, getTransactionStatus} from "@/api/contract_utils";
import MetaMaskOnboarding from "@metamask/onboarding";
import {ElMessageBox } from 'element-plus'

export default {
  name: 'Claim GameMiner',
  data() {
    return {
      loading: false,
      isDialog: false,
      labelPosition: 'top',
      centerDialogVisible: false,
      userAddress: undefined,
      dialogVisible: false,
      dialogMsg: 'Arkreen Network can mint a NFT for your GamingMiner(), do you want continue?',
      minerForm: {
        version: 1,
        ownerAddress: undefined,
        minerAddress: undefined,
        signature: undefined
      }
    };
  },
  created() {
    this.getUserAddress();
    this.handleGetMinerAddress();
  },
  methods: {
    handleClose(){
      if(this.isDialog){
        ElMessageBox.alert('取消铸造后可在Miner列表中重新铸造！', '提示', {
          confirmButtonText: 'OK',
          showClose:false,
          callback: () => {
            this.$router.push("/wallet/miners")
          },
        })
      }else {
        this.$router.push("/wallet/miners")
      }
    },
    async testNft(){
      let bAirDrop = false;
      let deadline = (Date.now()/1000+(7*24*60*60)).toFixed(0);
      let v = 28;
      let r = '0x'+'ddbe771575e279d5b887dca9f953a733c9c5b7d1f236f454e52f91904438772a';
      let s = '0x'+'1bf4605dc73d294098f3f9aed7b7e98fac51633320aa0a735b3f1511e2c83bc2';
      let sigRegister = {
        v,
        r,
        s
      }
      let hash = await etherGameMinerOnboard(getMetaMaskLoginUserAddress(), this.minerForm.minerAddress, bAirDrop,deadline , sigRegister);
      console.log("etherGameMinerOnboard result hash:" + hash)
      this.$message.info("The nft mint has been submitted. Please wait...")
      let status = await getTransactionStatus(hash);
      console.log('Transaction Status: ' + status);
      if (status == 0) {
        this.$message.error("nft mint failed!")
      } else {
        this.$message.success("nft mint success!")
      }
      this.$router.push("/wallet/miners")
    },
    async mintNft() {
      let owner = getMetaMaskLoginUserAddress();
      let miner = this.minerForm.minerAddress;
      let data = {
        "ownerAddress": owner,
        "minerAddress": miner
      }
      nftsign(data).then(async rsp => {
        if (rsp.code == 0) {
          let bAirDrop = false;
          let v = rsp.data.v;
          let r = '0x' + rsp.data.r;
          let s = '0x' + rsp.data.s;
          let sigRegister = {
            v,
            r,
            s
          }
          let hash = await etherGameMinerOnboard(owner, miner, bAirDrop, rsp.data.deadline, sigRegister);
          console.log("etherGameMinerOnboard result hash:" + hash)
          this.$message.info("The nft mint has been submitted. Please wait...")
          let status = await getTransactionStatus(hash);
          console.log('Transaction Status: ' + status);
          if (status == 0) {
            this.$message.error("nft mint failed!")
          } else {
            this.$message.success("nft mint success!")
          }
          this.$router.push("/wallet/miners")
        } else {
          this.$message.error("mint failed:" + rsp.msg);
        }
      })
    },
    handleNft() {
      this.dialogMsg = 'Arkreen Network can mint a NFT for your GamingMiner('+this.minerForm.minerAddress+'), do you want continue?',
      this.dialogVisible = true;
    },
    handleGetMinerAddress() {
      let str = this.$route.params.address;
      let obj = JSON.parse(Base64.decode(str));
      this.minerForm.minerAddress = obj.minerAddress;
      this.minerForm.ownerAddress = obj.ownerAddress;
    },
    async submitForm() {
      // 先判断用户是否安装MetaMask
      if (!MetaMaskOnboarding.isMetaMaskInstalled()) {
        this.$message.error(this.$t('common.msg.metaMaskNotFound'));
        return;
      }
      this.loading = true;
      try {
        let message = '{"version":1,"ownerAddress":"' + this.minerForm.ownerAddress + '","minerAddress":"' + this.minerForm.minerAddress + '"}'
        this.minerForm.signature = await personalSign(message);
        message = '{"version":1,"ownerAddress":"' + this.minerForm.ownerAddress + '","minerAddress":"' + this.minerForm.minerAddress + '","signature":"' + this.minerForm.signature + '"}'
        claimGameMiner(message).then(rsp => {
          this.minerForm.signature = undefined;
          if (rsp.code == 0) {
            this.$message.success("game miner claim success!");
            this.handleNft();
          } else {
            this.$message.error(rsp.msg);
          }
        })
      } catch (err) {
        this.$message.error("claim failed, please check the metamask is unlocked and try again!");
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
      if (this.userAddress == undefined || this.userAddress == null) {
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
  width: 45%;
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
