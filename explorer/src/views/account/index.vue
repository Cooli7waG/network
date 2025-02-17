<template>
  <el-breadcrumb style="margin-bottom: 20px;">
    <el-breadcrumb-item :to="{ path: '/' }">{{$t('account.path.home')}}</el-breadcrumb-item>
    <el-breadcrumb-item >{{$t('account.path.accounts')}}</el-breadcrumb-item>
  </el-breadcrumb>
  <el-row :gutter="24">
    <el-col :span="20">
      <div class="mt-4">
        <el-input v-model="data.query.address" :placeholder="$t('account.query.searchPlaceHolder')" v-on:keydown.enter="search">
          <template #append>
            <el-button type="primary" @click="search">{{$t('account.query.searchButton')}}</el-button>
          </template>
        </el-input>
      </div>
    </el-col>
  </el-row>
  <el-row :gutter="24">
    <el-col :span="24">
      <el-pagination
          v-model:currentPage="data.query.page.currentPage"
          v-model:page-size="data.query.page.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="data.query.page.total"
          @size-change="pageSizeChange"
          @current-change="pageCurrentChange"
          style="margin-top: 5px"/>
      <el-table :data="data.tableList" stripe border   style="width: 100%;margin-top: 5px">
        <el-table-column prop="address" :label="$t('account.table.address')" width="210px" :show-overflow-tooltip=true>
          <template #default="scope">
            <router-link v-if="scope.row.accountType==2" :to="'/miner/'+scope.row.address">{{scope.row.address}}</router-link>
            <router-link v-else :to="'/account/'+scope.row.address">{{scope.row.address}}</router-link>
          </template>
        </el-table-column>
        <el-table-column prop="accountType" :label="$t('account.table.accountType')" width="180" >
          <template #default="scope">
            {{Constant.AccountType[scope.row.accountType]}}
          </template>
        </el-table-column>
        <el-table-column prop="balance" :label="$t('account.table.balance')">
          <template #default="scope">
            {{scope.row.balance}}
          </template>
        </el-table-column>
        <el-table-column prop="earningMint" :label="$t('account.table.earningMint')" >
          <template #default="scope">
            {{formatToken(scope.row.earningMint)}}
          </template>
        </el-table-column>
        <el-table-column prop="earningService" :label="$t('account.table.earningService')">
          <template #default="scope">
            {{formatToken(scope.row.earningService)}}
          </template>
        </el-table-column>
        <el-table-column prop="amountMiner" :label="$t('account.table.amountMiner')">
          <template #default="scope">
            <router-link v-if="scope.row.accountType==1&&scope.row.amountMiner>0" :to="{name:'Miners',params:{ownerAddress:scope.row.address}}">{{scope.row.amountMiner}}</router-link>
            <span v-else>{{scope.row.amountMiner}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('account.table.createTime')" width="180">
          <template #default="scope">
            {{formatDate(scope.row.createTime, "yyyy-MM-dd hh:mm:ss")}}
          </template>
        </el-table-column>
      </el-table>

    </el-col>
  </el-row>
</template>

<script>
import { formatDate,formatString,formatToken,getTokenFixed } from '@/utils/data_format.js'
import {list} from '@/api/account.js'
import {onMounted, reactive} from "vue";
import {useRoute, useRouter} from 'vue-router'
import {toEther} from '@/utils/utils.js'
import Constant from '@/utils/constant.js'
export default {
  name:"account-list",
  props: {
    msg: String
  },
  computed: {
    formatDate() {
      return formatDate
    },
    formatString() {
      return formatString
    },
    formatToken() {
      return formatToken
    },
    getTokenFixed() {
      return getTokenFixed
    },
    Constant() {
      return Constant
    }
  },
  setup(){
    const router = useRouter();
    const data = reactive({
      query:{
        address:'',
        page:{
          currentPage:1,
          pageSize:20,
          total:0
        }
      },
      tableList :[]
    })
    const changUrl = () =>{
      let url = router.currentRoute.value.path;
      router.push({path:url,query:{pageSize:data.query.page.pageSize,currentPage:data.query.page.currentPage}});
    }
    const pageSizeChange = (pageSize) => {
      data.query.page.pageSize = pageSize
      data.query.page.currentPage = 1
      changUrl()
      loadList()
    }
    const pageCurrentChange = (currentPage) => {
      data.query.page.currentPage=currentPage
      changUrl()
      loadList()
    }

    const search=()=>{
      router.push({
        name:"AccountInfo",
        path: '/account/'+data.query.address,
        params:{
          address:data.query.address
        }
      })
    }

    const loadList=()=>{
      const params={
        offset:data.query.page.currentPage,
        limit:data.query.page.pageSize
      }
      list(params).then((result)=>{
          data.query.page.total=result.data.total

          const list=[]
          result.data.items.forEach(item=>{
            const fixed = getTokenFixed(item.balance);
            item.balance=item.balance==null?0:toEther(item.balance,fixed)
            list.push(item)
          })
          data.tableList=list
      }).catch((err) =>{
        console.log(err);
      });
    }
    const route = useRoute()
    onMounted(() => {
      data.query.page.currentPage = Number(route.query.currentPage==undefined?1:route.query.currentPage)
      data.query.page.pageSize = Number(route.query.pageSize==undefined?20:route.query.pageSize)
      loadList()
    })

    return {
      data,
      pageSizeChange,
      pageCurrentChange,
      search
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
.el-pagination{
  justify-content: flex-end;
}
</style>
