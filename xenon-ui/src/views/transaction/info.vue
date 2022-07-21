<template>
  <el-breadcrumb style="margin-bottom: 20px;">
    <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: '/txs' }">交易列表</el-breadcrumb-item>
    <el-breadcrumb-item >交易详情</el-breadcrumb-item>
  </el-breadcrumb>
  <div v-if="data.transaction">
    <el-row :gutter="20">
      <el-col :span="4">交易hash:</el-col>
      <el-col :span="16">{{data.transaction.hash}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">状态:</el-col>
      <el-col :span="16">{{data.transaction.status}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">Block高:</el-col>
      <el-col :span="16">{{data.transaction.height}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">交易类型:</el-col>
      <el-col :span="16">{{data.transaction.txType?Constant.TXType[data.transaction.txType]:''}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">交易时间:</el-col>
      <el-col :span="16">{{formatDate(data.transaction.createTime, "yyyy-MM-dd hh:mm:ss")}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">交易数据:</el-col>
      <el-col :span="16">{{data.transaction.data}}</el-col>
    </el-row>
  </div>
  <div v-if="data.noData">
    <el-col :sm="12" :lg="6">
      <el-result
          icon="warning"
          title="没有找到数据"
      >
        <template #extra>
          <router-link to="/txs">返回</router-link>
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
