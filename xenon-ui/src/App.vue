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

<style>

</style>
