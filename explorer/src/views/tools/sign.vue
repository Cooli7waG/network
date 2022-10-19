<template>
  <el-tabs class="demo-tabs" >
    <el-tab-pane label="BASE58" name="bas58">
      <el-form :model="data.base58" label-width="120px">
        <el-form-item label="原始数据(HEX)">
          <el-input type='textarea' v-model="data.base58.data" :rows="4"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="base58Encode">Encode</el-button>
          <el-button type="primary" @click="base58Decode">Decode</el-button>
        </el-form-item>
        <el-form-item label="结果">
          <el-input type='textarea' v-model="data.base58.result" :rows="4"/>
        </el-form-item>
      </el-form>
    </el-tab-pane>
<!--    <el-tab-pane label="ED25519" name="ED25519" >
      <el-form :model="data.ED25519" label-width="120px">
        <el-form-item label="原始数据(HEX)">
          <el-input type='textarea' v-model="data.ED25519.data" :rows="4"/>
        </el-form-item>
        <el-form-item label="公钥">
          <el-input type='textarea' v-model="data.ED25519.publicKey" :rows="4"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="ED25519Sign">SIGN</el-button>
          <el-button type="primary" @click="ED25519Decode">Decode</el-button>
        </el-form-item>
        <el-form-item label="结果">
          <el-input type='textarea' v-model="data.ED25519.result" :rows="4"/>
        </el-form-item>
      </el-form>
    </el-tab-pane>-->
  </el-tabs>
</template>


<script>
import { formatDate } from '@/utils/data_format.js'
import {bytesToHex,hexToBytes} from '@/utils/utils.js'
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
      base58:{
        data:'',
        result:''
      },
      ED25519:{
        data:'',
        privateKey:'',
        publicKey:'',
        result:''
      },
      form :{
        minerJson:'',
        ownerPrivateKey:'',
        ownerJson:'',
        payerPrivateKey:'',
        payerJson:'',
      },
    })

    const base58Encode=()=>{
      try{
        data.base58.result=bs58.encode(hexToBytes(data.base58.data))
      }catch (e){
        ElMessage.error("操作失败")
      }
    }

    const base58Decode=()=>{
      try{
        data.base58.result=bytesToHex(bs58.decode(data.base58.data))
      }catch (e){
        ElMessage.error("操作失败")
      }
    }

    const ED25519Sign=()=>{
      try{
        data.base58.result=bs58.encode(hexToBytes(data.base58.data))

       /* const ED25519 =data.ED25519
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
        })*/
      }catch (e){
        ElMessage.error("操作失败")
      }
    }


  /*  const ownerSign= ()=>{
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
    }*/
    onMounted(() => {
      console.log("onMounted")
    })

    return {
      data,
      base58Encode,
      base58Decode,
      ED25519Sign
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
