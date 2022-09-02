<template>
  <el-form ref="registerForm" :model="data.form" label-width="120px">
    <el-form-item label="Miner地址">
      <el-input v-model="data.form.address" />
    </el-form-item>
    <el-form-item label="Miner类型">
      <el-select v-model="data.form.minerType" >
        <el-option label="VIRTUAL MINER" value="1" />
        <el-option label="API MINER" value="2" />
        <el-option label="DTU MINER" value="3" />
        <el-option label="LIGHT SOLAR MINER" value="4" />
      </el-select>
    </el-form-item>
    <el-form-item label="设备制造商名称">
      <el-input v-model="data.form.maker" />
    </el-form-item>
    <el-form-item label="基金会签名">
      <el-input v-model="data.form.foundationSignature" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="registerSubmit">注册</el-button>
    </el-form-item>
  </el-form>
</template>


<script>
import { formatDate } from '@/utils/data_format.js'
import {hexToBytes} from '@/utils/utils.js'
import {register} from '@/api/miners.js'
import {onMounted, reactive, ref, unref} from "vue";
import {ElMessage} from "element-plus";
import bs58 from 'bs58'
export default {
  props: {
    msg: String
  },
  computed: {
    formatDate() {
      return formatDate
    }
  },
  setup(){
    const registerForm = ref()
    const data = reactive({
      form :{
        address:'',
        minerType:'1',
        maker:'',
        foundationSignature:'',
      }
    })

    const registerSubmit=()=>{
          const formData=JSON.parse(JSON.stringify(data.form))
           formData.address=bs58.encode(hexToBytes(formData.address))
          console.log(formData.address)
          register(formData).then(result=>{
            if(result.code==0){
              ElMessage.success("注册成功")
              unref(registerForm).resetFields()
            }else if(result.code==1008){
              ElMessage.error("签名错误")
            }else if(result.code==3001){
              ElMessage.error("Miner已存在")
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
      registerSubmit
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
