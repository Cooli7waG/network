<template>
  <div >
    <el-form ref="registerForm" :model="data.form" label-width="120px">
      <el-form-item label="原始数据">
        <el-input type='textarea' v-model="data.form.data" :rows="4"/>
      </el-form-item>
      <el-form-item label="私钥">
        <el-input type='text' v-model="data.form.privateKey" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="ownerSign">Ed25519签名</el-button>
        <el-button type="primary" @click="ownerSign">Ed25519 to Base58 签名</el-button>
        <el-button type="primary" @click="ownerSign">Base58 to Ed25519 验签</el-button>
        <el-button type="primary" @click="ownerSign">Base58 encode</el-button>
        <el-button type="primary" @click="ownerSign">Base58 decode</el-button>
      </el-form-item>
      <el-form-item label="新数据">
        <el-input type='textarea' v-model="data.form.data" :rows="4"/>
      </el-form-item>
    </el-form>
  </div>
</template>


<script>
import { formatDate } from '@/utils/data_format.js'
import {hexToBytes} from '@/utils/utils.js'
import {onMounted, reactive, ref, unref} from "vue";
import {ElMessage} from "element-plus";
import * as ed from '@noble/ed25519';
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
      steps:{
        active:0,
      },
      form :{
        minerJson:'',
        ownerPrivateKey:'',
        ownerJson:'',
        payerPrivateKey:'',
        payerJson:'',
      },
    })


    const ownerSign= ()=>{
      console.log(data.form.minerJson)
      const steps =data.steps
      const form =data.form
      ed.sign(new TextEncoder().encode(form.minerJson), form.ownerPrivateKey).then(signature=>{
        const base58Sign=bs58.encode(signature)
        const data=JSON.parse(form.minerJson)
        data['ownerSignature']=base58Sign
        form.ownerJson=JSON.stringify(data)
        console.log(form.ownerJson)
        steps.active=1
        ElMessage.success("Owner 签名成功")
      }).catch(err=>{
        console.log(err)
        ElMessage.error("Owner 签名失败")
      })
    }
    onMounted(() => {
      console.log("onMounted")
    })

    return {
      data,
      ownerSign
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
