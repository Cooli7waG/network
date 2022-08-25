<template>
  <div class="common-layout">
    <el-container>
      <el-header style="z-index: 999">
        <Menus/>
      </el-header>
      <el-container>
        <el-main>
          <router-view />
        </el-main>
        <el-footer>
          © 2022 . aitos.io
        </el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import Menus from '@/components/Menus.vue'
export default {
  name: 'App',
  components: {
    Menus
  }
}
</script>

<style lang="scss">
body {
  margin: 0;
  width: 100%;
  min-width: 320px;
  min-height: 100vh;
  line-height: 1.4;
  font-size: 16px;
  font-weight: 400;
  color: var(--text-color);
  background-color: var(--bg-color);
  direction: ltr;
  font-family: Helvetica,Arial,sans-serif;
  font-synthesis: none;
  text-rendering: optimizeLegibility;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  transition: background-color var(--el-transition-duration-fast);
}
.el-container{
  width: 100% !important;
  .el-header{
    --el-header-padding: 0 0px;
    position: relative;
    width: 100%;
    height: 60px !important;
  }
  .el-container{
    height: calc(100vh - 80px);
    .el-main{
    }
    .el-footer{
      height: 10px;
      text-align: center;
    }
  }
}
</style>
