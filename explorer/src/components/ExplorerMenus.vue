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
        <el-menu-item index="/">{{ $t('menus.dashboard') }}</el-menu-item>
        <el-menu-item index="/miners">{{ $t('menus.miners') }}</el-menu-item>
        <el-menu-item index="/accounts">{{ $t('menus.accounts') }}</el-menu-item>
        <el-menu-item index="/blocks">{{ $t('menus.blocks') }}</el-menu-item>
        <el-menu-item index="/txs">{{ $t('menus.txs') }}</el-menu-item>
      </el-menu>
    </el-col>
    <el-col :span="4" class="login">
      <div style="float: right;margin-right: 10px" v-show="isShow">
        <div style="line-height: 50px">
          <el-button type="text" style="color: #FFFFFF;" @click="this.$router.push('/wallet')">
            <i class="iconfont icon-qianbao wallet-img" style="font-size: 26px"/>
          </el-button>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import {formatString} from "@/utils/data_format";

export default {
  name: 'ExplorerMenus',
  props: {
    msg: String
  },
  data() {
    return {
      drawer: false,
      direction: 'rtl',
      isShow: true,
      userAddress: undefined
    }
  },
  methods: {
    autoActive() {
      let path = this.$route.path;
      if (path.startsWith("/miner/")) {
        return "/miners"
      }
      if (path.startsWith("/account/")) {
        return "/accounts"
      }
      if (path.startsWith("/tx/")) {
        return "/txs"
      }
      return path;
    },
    formatString(str) {
      return formatString(str, 15);
    }
  }
}
</script>
<style scoped>
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

</style>
