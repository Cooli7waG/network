<template>
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
        <el-table-column prop="hash" label="交易Hash" width="450">
          <template #default="scope">
            <router-link :to="'/tx/'+scope.row.hash">{{scope.row.hash}}</router-link>
          </template>
        </el-table-column>
        <el-table-column prop="height" label="块高" width="180" >
          <template #default="scope">
            <el-link @click="()=>{data.query.keyword=scope.row.height;search();}">{{scope.row.height}}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="txType" label="交易类型" width="180" >
          <template #default="scope">
            {{scope.row.txType?Constant.TXType[scope.row.txType]:''}}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="交易时间" >
          <template #default="scope">
            {{formatDate(scope.row.createTime, "yyyy-MM-dd hh:mm:ss")}}
          </template>
        </el-table-column>
      </el-table>
    </el-col>
  </el-row>
</template>

<script>
import Constant from '@/utils/constant.js'
import { formatDate } from '@/utils/data_format.js'
import {transactionList} from '@/api/transaction.js'
import {onMounted, reactive} from "vue";
import { useRouter } from 'vue-router'
import { useStore, mapState } from "vuex"
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
    const store = useStore()
    const data = reactive({
      query:{
        keyword:'',
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
      loadTransactionList()
    }
    const pageCurrentChange = (currentPage) => {
      data.query.page.currentPage=currentPage
      loadTransactionList()
    }

    const search=()=>{
      router.push({
        name:"Transaction",
        path: '/txs',
        query:{
          keyword:data.query.keyword
        }
      })
    }

    const loadTransactionList=()=>{
      const params={
        offset:data.query.page.currentPage,
        limit:data.query.page.pageSize
      }
      if(store.state.accountType==1){
        params["accountId"]=store.state.accountId
      }else{
        params["fromAddress"]=store.state.address
      }
      transactionList(params).then((result)=>{
        data.query.page.total=result.data.total
        data.tableList=result.data.items
      }).catch((err) =>{
        console.log(err);
      });
    }

    onMounted(() => {
      console.log("onMounted")
      loadTransactionList()
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
