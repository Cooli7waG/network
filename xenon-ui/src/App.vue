<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <Menus/>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
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
  font-synthesis: none;
  text-rendering: optimizeLegibility;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  transition: background-color var(--el-transition-duration-fast);
  overflow: hidden;
}
.el-header{
  --el-header-padding: 0 0px;
}
</style>
