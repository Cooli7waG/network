<template>
  <el-breadcrumb style="margin-bottom: 20px;">
    <el-breadcrumb-item :to="{ path: '/' }">{{$t('account.path.home')}}</el-breadcrumb-item>
    <el-breadcrumb-item >{{$t('account.path.accounts')}}</el-breadcrumb-item>
  </el-breadcrumb>
  <el-row :gutter="24">
    <el-col :span="20">
      <div class="mt-4">
        <el-input v-model="data.query.address" :placeholder="$t('account.query.searchPlaceHolder')">
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
      />
      <el-table :data="data.tableList" stripe border   style="width: 100%">
        <el-table-column prop="address" :label="$t('account.table.address')" width="450">
          <template #default="scope">
            <router-link :to="'/account/'+scope.row.address">{{scope.row.address}}</router-link>
          </template>
        </el-table-column>
        <el-table-column prop="accountType" :label="$t('account.table.accountType')" width="180" >
          <template #default="scope">
            {{Constant.AccountType[scope.row.accountType]}}
          </template>
        </el-table-column>
        <el-table-column prop="balance" :label="$t('account.table.balance')" width="200" align="right"/>
        <el-table-column prop="earningMint" :label="$t('account.table.earningMint')" width="180" />
        <el-table-column prop="earningService" :label="$t('account.table.earningService')" width="180" />
        <el-table-column prop="amountMiner" :label="$t('account.table.amountMiner')" width="180" />
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
import { formatDate } from '@/utils/data_format.js'
import {list} from '@/api/account.js'
import {onMounted, reactive} from "vue";
import { useRouter } from 'vue-router'
import {toEther} from '@/utils/utils.js'
import Constant from '@/utils/constant.js'
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

    const data = reactive({
      query:{
        address:'',
        page:{
          currentPage:1,
          pageSize:10,
          total:0
        }
      },
      tableList :[]
    })
    const router = useRouter()

    const pageSizeChange = (pageSize) => {
      data.query.page.pageSize=pageSize
      loadList()
    }
    const pageCurrentChange = (currentPage) => {
      data.query.page.currentPage=currentPage
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
            item.balance=toEther(item.balance,8)
            list.push(item)
          })
          data.tableList=list
      }).catch((err) =>{
        console.log(err);
      });
    }

    onMounted(() => {
      console.log("onMounted")
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
