<template>
  <el-breadcrumb style="margin-bottom: 20px;">
    <el-breadcrumb-item :to="{ path: '/' }">{{$t('txinfo.path.home')}}</el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: '/txs' }">{{$t('txinfo.path.txs')}}</el-breadcrumb-item>
    <el-breadcrumb-item >{{$t('txinfo.path.info')}}</el-breadcrumb-item>
  </el-breadcrumb>
  <div v-if="data.transaction">
    <el-row :gutter="20">
      <el-col :span="4">{{$t('txinfo.info.hash')}}:</el-col>
      <el-col :span="16">{{data.transaction.hash}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">{{$t('txinfo.info.status')}}:</el-col>
      <el-col :span="16">{{data.transaction.status}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">{{$t('txinfo.info.height')}}:</el-col>
      <el-col :span="16">{{data.transaction.height}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">{{$t('txinfo.info.txType')}}:</el-col>
      <el-col :span="16">{{data.transaction.txType?Constant.TXType[data.transaction.txType]:''}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">{{$t('txinfo.info.txTime')}}:</el-col>
      <el-col :span="16">{{formatDate(data.transaction.createTime, "yyyy-MM-dd hh:mm:ss")}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">{{$t('txinfo.info.txData')}}:</el-col>
      <el-col :span="16">{{data.transaction.data}}</el-col>
    </el-row>
  </div>
  <div v-if="data.noData">
    <el-col :sm="12" :lg="6">
      <el-result
          icon="warning"
          :title="$t('txinfo.msg.noData')"
      >
        <template #extra>
          <router-link to="/txs">{{ $t('common.button.back') }}</router-link>
        </template>
      </el-result>
    </el-col>
  </div>
</template>

<script>
import Constant from '@/utils/constant.js'
import {findByHash} from '@/api/transaction.js'
import {onMounted, reactive} from "vue";
import { useRoute  } from 'vue-router'
import {formatDate} from "@/utils/data_format";
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
        txHash:''
      },
      transaction:'',
      noData:false
    })
    const loadTransactionInfo=()=>{
      findByHash(data.query.txHash).then((result)=>{
        data.transaction=result.data
        if(result.data==null){
          data.noData=true
        }
      }).catch((err) =>{
        console.log(err);
      });
    }

    onMounted(() => {
      console.log("onMounted")
      data.query.txHash=route.params.hash;
      loadTransactionInfo()
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
