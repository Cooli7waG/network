<template>
  <el-form :model="data.form" label-width="120px">
    <el-form-item label="Miner私钥">
      <el-input type='text' v-model="data.form.privateKey" :rows="4"/>
    </el-form-item>
    <el-form-item label="基金会签名">
      <el-input type='textarea' v-model="data.form.foundationSignature" :rows="4"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="startSubmit">启动</el-button>

      <el-button type="primary" @click="startSubmit">Terminate</el-button>
    </el-form-item>
  </el-form>
</template>


<script>
import {start,terminate} from '@/api/virtualminer.js'
import {onMounted, reactive, ref, unref} from "vue";
import {ElMessage} from "element-plus";
export default {
  props: {
    msg: String
  },
  setup(){
    const registerForm = ref()
    const data = reactive({
      steps:{
        active:0,
      },
      form :{
        privateKey:'',
        foundationSignature:''
      },
    })

    const startSubmit=()=>{
      start(data.form).then(result=>{
        if(result.code==0){
          ElMessage.success("启动成功")
        }else if(result.code==3001){
          ElMessage.error("基金会签名错误")
        }else if(result.code==3002){
          ElMessage.error("Miner已启动")
        }else{
          ElMessage.error(result.msg)
        }
      }).catch(err=>{
        console.log(err);
      })
    }

    const terminateSubmit=()=>{
      terminate(data.form).then(result=>{
        if(result.code==0){
          ElMessage.success("启动成功")
          unref(registerForm).resetFields()
        }else if(result.code==3001){
          ElMessage.error("基金会签名错误")
        }else{
          ElMessage.error(result.msg)
        }
      }).catch(err=>{
        console.log(err);
      })
    }


    onMounted(() => {
      console.log("onMounted")
    })

    return {
      data,
      startSubmit,
      terminateSubmit
    }
  }
}
</script>
<style lang="scss" scoped>
.box-card {
  .item{
    margin-bottom: 20px;
    .lable{
      font-weight: bold;
      margin-bottom: 6px;
    }
  }

}
</style>
