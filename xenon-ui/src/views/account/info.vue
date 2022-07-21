<template>
  <el-breadcrumb >
    <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: '/accounts' }">账户列表</el-breadcrumb-item>
    <el-breadcrumb-item >账户详情</el-breadcrumb-item>
  </el-breadcrumb>
  <div v-if="data.account">
    <el-row :gutter="20">
      <el-col :span="4">Address:</el-col>
      <el-col :span="16">{{data.account.address}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">账户类型:</el-col>
      <el-col :span="16">{{Constant.AccountType[data.account.accountType]}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">nonce:</el-col>
      <el-col :span="16">{{data.account.nonce}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">balance:</el-col>
      <el-col :span="16">{{data.account.balance}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">创建时间:</el-col>
      <el-col :span="16">{{formatDate(data.account.createTime, "yyyy-MM-dd hh:mm:ss")}}</el-col>
    </el-row>
  </div>
  <div v-if="data.noData">
    <el-col :sm="12" :lg="6">
      <el-result
          icon="warning"
          title="没有找到数据"
      >
        <template #extra>
          <router-link to="/accounts">返回</router-link>
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
