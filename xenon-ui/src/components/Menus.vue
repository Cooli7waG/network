<template>
  <el-menu
      :default-active="$route.path"
      class="el-menu-demo"
      mode="horizontal"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
      :router="true"
  >
    <el-menu-item index="/">{{$t('menus.dashboard')}}</el-menu-item>
    <el-menu-item index="/miners">{{$t('menus.miners')}}</el-menu-item>
    <el-menu-item index="/accounts">{{$t('menus.accounts')}}</el-menu-item>
    <el-menu-item index="/blocks">{{$t('menus.blocks')}}</el-menu-item>
    <el-menu-item index="/txs">{{$t('menus.txs')}}</el-menu-item>
  </el-menu>
</template>

<script>

export default {
  name: 'Menus',
  props: {
    msg: String
  }
}
</script>
<style scoped>

</style>
