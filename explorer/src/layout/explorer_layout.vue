<template>
  <el-container>
    <el-header>
      <Menus/>
    </el-header>
    <el-container style="margin-top:-1px;">
      <el-main style="overflow: auto;">
        <router-view />
      </el-main>
      <el-footer>
        {{ $t('footer')}}
      </el-footer>
    </el-container>
  </el-container>
</template>

<script>
import Menus from '@/components/ExplorerMenus.vue'
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

/* 设置浏览器滚动条的样式 */
::-webkit-scrollbar {
  width: 0px;
}
/* 滚动槽 */
::-webkit-scrollbar-track {
  -webkit-box-shadow: inset006pxrgba(0, 0, 0, 0.3);
  border-radius: 10px;
}
/* 滚动条滑块 */
::-webkit-scrollbar-thumb {
  border-radius: 10px;
  background: rgba(0, 0, 0, 0.1);
  -webkit-box-shadow: inset006pxrgba(0, 0, 0, 0.5);
}
::-webkit-scrollbar-thumb:window-inactive {
  background: rgba(255, 0, 0, 0.4);
}
</style>
