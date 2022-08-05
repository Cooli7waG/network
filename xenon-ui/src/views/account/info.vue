<template>
  <el-breadcrumb >
      <el-breadcrumb-item :to="{ path: '/' }">{{$t('accountinfo.path.home')}}</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/accounts' }">{{$t('accountinfo.path.accounts')}}</el-breadcrumb-item>
      <el-breadcrumb-item >{{$t('accountinfo.path.info')}}</el-breadcrumb-item>
  </el-breadcrumb>
  <div v-if="data.account">
    <el-row :gutter="20">
      <el-col :span="4">{{$t('accountinfo.info.address')}}:</el-col>
      <el-col :span="16">{{data.account.address}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">{{$t('accountinfo.info.accountType')}}:</el-col>
      <el-col :span="16">{{Constant.AccountType[data.account.accountType]}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">{{$t('accountinfo.info.earningMint')}}:</el-col>
      <el-col :span="16">{{data.account.earningMint}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">{{$t('accountinfo.info.earningService')}}:</el-col>
      <el-col :span="16">{{data.account.earningService}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">{{$t('accountinfo.info.amountMiner')}}:</el-col>
      <el-col :span="16">{{data.account.amountMiner}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">{{$t('accountinfo.info.balance')}}:</el-col>
      <el-col :span="16">{{data.account.balance}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">{{$t('accountinfo.info.createTime')}}:</el-col>
      <el-col :span="16">{{formatDate(data.account.createTime, "yyyy-MM-dd hh:mm:ss")}}</el-col>
    </el-row>
  </div>
  <div v-if="data.noData">
    <el-col :sm="12" :lg="6">
      <el-result
          icon="warning"
          :title="$t('accountinfo.msg.noData')"
      >
        <template #extra>
          <router-link to="/accounts">{{$t('common.button.back')}}</router-link>
        </template>
      </el-result>
    </el-col>
  </div>
</template>

<script>
import Constant from '@/utils/constant.js'
import {findByAddress} from '@/api/account.js'
import {onMounted, reactive} from "vue";
import { useRoute  } from 'vue-router'
import {formatDate} from "@/utils/data_format";
import {toEther} from '@/utils/utils.js'
export default {
  props: {
    msg: String
  },
  computed: {
    formatDate() {
      return formatDate
    },
    Constant() {
      return Constant
    }
  },
  setup(){

    const route = useRoute()
    const data = reactive({
      query:{
        address:''
      },
      account:'',
      noData:false
    })
    const loadFindByAddress=()=>{
      findByAddress(data.query.address).then((result)=>{
        data.account=result.data
        if(result.data==null){
          data.noData=true
        }else{
          data.account.balance=toEther(data.account.balance,8)
        }
      }).catch((err) =>{
        console.log(err);
      });
    }

    onMounted(() => {
      console.log("onMounted")
      data.query.address=route.params.address;
      loadFindByAddress()
    })

    return {
      data
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
