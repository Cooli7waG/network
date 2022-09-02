<template>
  <el-breadcrumb style="margin-bottom: 20px;">
    <el-breadcrumb-item :to="{ path: '/' }">{{$t('block.path.home')}}</el-breadcrumb-item>
    <el-breadcrumb-item >{{$t('block.path.blocks')}}</el-breadcrumb-item>
  </el-breadcrumb>
  <el-row :gutter="24">
    <el-col :span="20">
      <div class="mt-4">
        <el-input v-model="data.query.block" :placeholder="$t('block.query.searchPlaceHolder')">
          <template #append>
            <el-button type="primary" @click="search">{{$t('block.query.searchButton')}}</el-button>
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
          style="margin-top: 5px"
      />
      <el-table :data="data.tableList" stripe border   style="width: 100%;margin-top: 5px">
        <el-table-column prop="address" :label="$t('block.table.height')" width="450">
          <template #default="scope">
            <router-link :to="{name:'Transaction',params:{height:scope.row.height}}">{{scope.row.height}}</router-link>
          </template>
        </el-table-column>
        <el-table-column prop="amountTransaction" :label="$t('block.table.amountTransaction')" width="200">
          <template #default="scope">
            <router-link v-if="scope.row.amountTransaction>0" :to="{name:'Transaction',params:{height:scope.row.height}}">{{scope.row.amountTransaction}}</router-link>
            <span v-else>{{scope.row.amountTransaction}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="blockIntervalTime" :label="$t('block.table.blockTime')" width="180" />
        <el-table-column prop="createTime" :label="$t('block.table.createTime')">
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
import {list} from '@/api/block.js'
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
        block:'',
        page:{
          currentPage:1,
          pageSize:20,
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
        path: '/account/'+data.query.block,
        params:{
          address:data.query.block
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
