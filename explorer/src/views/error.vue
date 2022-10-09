<template>
  <div class="notLogin">
    <el-empty :description="description" style="font-size:18px;color: #f12c0c;"></el-empty>
  </div>
  <div style="text-align: center">
    <el-button type="primary" class="downText" v-show="explorerInfo === 'Chrome'" @click="handleDownloadMetaMask('https://chrome.google.com/webstore/detail/metamask/nkbihfbeogaeaoehlefnkodbefgpgknn')">
      Download for <img class="downImg" src="../assets/img/chrome.png">
    </el-button>
    <el-button type="primary" class="downText" v-show="explorerInfo === 'Edge'" @click="handleDownloadMetaMask('https://microsoftedge.microsoft.com/addons/detail/metamask/ejbalbakoplchlghecdalmeeeajnimhm?hl=en-US')">
      Download for <img class="downImg" src="../assets/img/Edge.png">
    </el-button>
    <el-button type="primary" class="downText" v-show="explorerInfo === 'Firefox'" @click="handleDownloadMetaMask('https://addons.mozilla.org/en-US/firefox/addon/ether-metamask/')">
      Download for <img class="downImg" src="../assets/img/Firefox.png">
    </el-button>
    <el-button type="primary" class="downText" v-show="explorerInfo === 'Opera'" @click="handleDownloadMetaMask('https://addons.opera.com/en-gb/extensions/details/metamask-10/')">
      Download for <img class="downImg" src="../assets/img/opera.png">
    </el-button>
    <el-button type="primary" class="downText" v-show="explorerInfo === 'Brave'" @click="handleDownloadMetaMask('https://chrome.google.com/webstore/detail/metamask/nkbihfbeogaeaoehlefnkodbefgpgknn')">
      Download for <img class="downImg" src="../assets/img/Brave.png">
    </el-button>
  </div>
</template>

<script>

import {getExplorerInfo} from "@/api/browserUtils";

export default {
  name: 'ERROR',
  data() {
    return {
      description:"Please use the chrome browser and install the matemask wallet plugin!",
      explorerInfo: undefined
    }
  },
  created() {
    this.handleGetExplorerInfo();
  },
  methods: {
    handleDownloadMetaMask(href){
      window.open(href,'_blank');
    },
    handleGetExplorerInfo(){
      let explorerInfo= getExplorerInfo();
      this.explorerInfo = explorerInfo;
      if(explorerInfo == "Chrome" || explorerInfo == "Opera" || explorerInfo == "Brave" || explorerInfo == "Firefox" || explorerInfo == "Edge"){
        this.description = "Please install the matemask wallet plugin first!";
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.downText{
  border-radius: 25px;
  height: 50px;
  font-weight: bold;
}
.downImg{
  width: 30px;
  height: 30px;
  margin-left: 5px;
  margin-right: -5px
}
</style>
