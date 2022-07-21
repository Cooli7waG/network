<template>
  <el-menu
      default-active="/"
      class="el-menu-demo"
      mode="horizontal"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
      :router="true"
  >
    <el-menu-item index="/">首页</el-menu-item>
    <el-sub-menu index="2">
      <template #title>数据查询</template>
      <el-menu-item index="/txs" >交易列表</el-menu-item>
      <el-menu-item index="/miners">miners列表</el-menu-item>
      <el-menu-item index="/accounts">账户列表</el-menu-item>
    </el-sub-menu>
    <el-menu-item index="/console">Console</el-menu-item>
    <el-menu-item index="/tools">Tools</el-menu-item>
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
