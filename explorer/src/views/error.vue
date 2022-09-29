<template>
  <div  class="notLogin">
    <el-empty description="Please use the chrome browser and install the matemask wallet plugin" style="font-size:18px;color: #f12c0c;">
    </el-empty>
  </div>
</template>

<script>

export default {
  name: 'ERROR',
  props: {
    msg: String
  },
  setup: function () {
    return {
    }
  }
}
</script>
<style lang="scss" scoped>

</style>
