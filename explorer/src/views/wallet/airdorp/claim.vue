<template>
  <el-container style="height: auto">
    <div class="contentDiv" v-loading="loading">
      <div class="titleDiv">Arkreen Website</div>
      <div class="btnDiv" style="padding: 10px;">Your GamingMiner Address :</div>
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
      v-model="dialogWaitNftVisible"
      title="Please wait..."
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      width="35%"
      :show-close="false">
    <div style="text-align: center"><el-icon class="is-loading" style="font-size: 36px"><Loading /></el-icon></div>
    <div style="margin-top: 30px">
      <span>NFT is minting for you. Please do not close or refresh the browser. This process takes about 30-120 seconds. Please wait...</span>
    </div>
  </el-dialog>
</template>

<script>
import {claimGameMiner,nftsign} from "@/api/miners";
import {Base64} from "js-base64";
import {getMetaMaskLoginUserAddress} from "@/api/metamask_utils";
import {etherGameMinerOnboard, getNftBalanceOf, getTransactionStatus} from "@/api/contract_utils";
import MetaMaskOnboarding from "@metamask/onboarding";

export default {
  name: 'Claim GamingMiner',
  data() {
    return {
      loading: false,
      isDialog: false,
      labelPosition: 'top',
      centerDialogVisible: false,
      userAddress: undefined,
      dialogVisible: false,
      dialogWaitNftVisible: false,
      isCheckNftMiner: false,
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
    async testNft(){
      let hash = "0xcf8d12769b46cab7d059f894b8ff79c5c49eee539ac41c77ae41c330d2901bb6";
      this.submitForm(hash);
    },
    async mintNft() {
      if(this.isCheckNftMiner){
        let count = await getNftBalanceOf();
        if(count>0){
          this.$message.error("gaming miner nft count should be 0")
          return;
        }
      }
      let owner = getMetaMaskLoginUserAddress();
      let miner = this.minerForm.minerAddress;
      let data = {
        "ownerAddress": owner,
        "minerAddress": miner
      }
      nftsign(data).then(async rsp => {
        if (rsp.code == 0) {
          let bAirDrop = false;
          let sig = rsp.data.sig;
          let originData = rsp.data.originData;
          let v = sig.v;
          let r = '0x' + sig.r;
          let s = '0x' + sig.s;
          let sigRegister = {v, r, s}
          let hash = await etherGameMinerOnboard(owner, miner, bAirDrop, originData.deadline, sigRegister);
          this.dialogWaitNftVisible = true;
          let status = await getTransactionStatus(hash);
          this.dialogWaitNftVisible = false;
          if (status == 0) {
            this.$message.error("nft mint failed!")
          } else {
            this.$message.success("nft mint success!")
            this.submitForm(hash);
          }
        } else {
          this.$message.error("mint failed:" + rsp.msg);
        }
      })
    },
    handleGetMinerAddress() {
      let str = this.$route.params.address;
      let obj = JSON.parse(Base64.decode(str));
      this.minerForm.minerAddress = obj.minerAddress;
      this.minerForm.ownerAddress = obj.ownerAddress;
    },
    submitForm(txHash) {
      this.loading = true;
      try {
        let message = {
          version:1,
          ownerAddress: this.minerForm.ownerAddress,
          minerAddress: this.minerForm.minerAddress,
          txHash: txHash
        }
        claimGameMiner(message).then(rsp => {
          this.minerForm.signature = undefined;
          if (rsp.code == 0) {
            this.$message.success("gaming miner claim success!");
            this.$router.push("/wallet/miners")
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
      // 先判断用户是否安装MetaMask
      if (!MetaMaskOnboarding.isMetaMaskInstalled()) {
        this.$message.error(this.$t('common.msg.metaMaskNotFound'));
        return;
      }
      this.$confirm('Click "OK" to confirm the receipt', 'Tips', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        this.mintNft();
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
