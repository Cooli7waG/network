<template>
  <el-steps :active="data.steps.active" align-center finish-status="success">
    <el-step title="Onwer签名" />
    <el-step title="Payer签名" />
    <el-step title="绑定Miner" />
  </el-steps>
  <div v-if="data.steps.active==0">
    <el-form ref="registerForm" :model="data.form" label-width="120px">
      <el-form-item label="数据">
        <el-input type='textarea' v-model="data.form.minerJson" :rows="4"/>
      </el-form-item>

      <el-form-item label="Onwer私钥">
        <el-input type='text' v-model="data.form.ownerPrivateKey"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="ownerSign">Owner签名</el-button>
      </el-form-item>
    </el-form>
  </div>
  <div v-if="data.steps.active==1">
    <el-form ref="registerForm" :model="data.form" label-width="120px">
      <el-form-item label="数据">
        <el-input type='textarea' v-model="data.form.ownerJson" :rows="4"/>
      </el-form-item>
      <el-form-item label="Payer私钥">
        <el-input type='text' v-model="data.form.payerPrivateKey"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="()=>data.steps.active--">上一步</el-button>
        <el-button type="primary" @click="payerSign">Payer 签名</el-button>
      </el-form-item>
    </el-form>
  </div>

  <div v-if="data.steps.active==2">
    <el-form ref="registerForm" :model="data.form" label-width="120px">
      <el-form-item label="数据">
        <el-input type='textarea' v-model="data.form.payerJson" :rows="4"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="()=>data.steps.active--">上一步</el-button>
        <el-button type="primary" @click="onboardSubmit">绑定</el-button>
      </el-form-item>
    </el-form>
  </div>

</template>


<script>
import { formatDate } from '@/utils/data_format.js'
import {toUint8Arr,hexToBytes} from '@/utils/utils.js'
import {onboard} from '@/api/miners.js'
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

   /* watch(()=>data.json,()=>{
      try{
        data.form=JSON.parse(data.json);
      }catch{
        ElMessage.error("json格式错误")
      }
    })*/

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

    const payerSign=async ()=>{
      const steps =data.steps
      const form =data.form
      ed.sign(new TextEncoder().encode(form.ownerJson), form.payerPrivateKey).then(signature=>{
        const base58Sign=bs58.encode(signature)
        const data=JSON.parse(form.ownerJson)
        data['payerSignature']=base58Sign
        form.payerJson=JSON.stringify(data)
        console.log(form.payerJson)
        steps.active=2
        ElMessage.success("Payer 签名成功")
      }).catch(err=>{
        console.log(err)
        ElMessage.error("Payer 签名失败")
      })
    }

    const onboardSubmit=()=>{
      const formData=JSON.parse(JSON.stringify(data.form.payerJson))
      onboard(formData).then(result=>{
        if(result.code==0){
          ElMessage.success("绑定成功")
          unref(registerForm).resetFields()
        }else if(result.code==3002){
          ElMessage.error("payer 签名错误")
        }else if(result.code==3003){
          ElMessage.error("onwer 签名错误")
        }else if(result.code==3004){
          ElMessage.error("Miner 签名错误")
        }else if(result.code==3005){
          ElMessage.error("Miner 不存在")
        }else if(result.code==3008){
          ElMessage.error("Miner 已绑定")
        }else if(result.code==4007){
          ElMessage.error("Onwer 账户不存在")
        }else if(result.code==4003){
          ElMessage.error("payer 账户不存在")
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
      ownerSign,
      payerSign,
      onboardSubmit
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
