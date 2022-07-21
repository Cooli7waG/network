<template>
  <el-breadcrumb style="margin-bottom: 20px;">
    <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item >Miners列表</el-breadcrumb-item>
  </el-breadcrumb>
  <el-row :gutter="24">
    <el-col :span="20">
      <div class="mt-4">
        <el-input v-model="data.query.address" placeholder="请输入Miner Address">
          <template #append>
            <el-button type="primary" @click="search">搜索</el-button>
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
          :page-sizes="[100, 200, 300, 400]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="data.query.page.total"
          @size-change="pageSizeChange"
          @current-change="pageCurrentChange"
      />
      <el-table :data="data.tableList" stripe border   style="width: 100%">
        <el-table-column prop="address" label="Address" width="450">
          <template #default="scope">
            <router-link :to="'/miner/'+scope.row.address">{{scope.row.address}}</router-link>
          </template>
        </el-table-column>
        <el-table-column prop="minerType" label="miner 类型" width="180" >
          <template #default="scope">
            {{scope.row.minerType?Constant.MinerType[scope.row.minerType]:''}}
          </template>
        </el-table-column>
        <el-table-column prop="maker" label="Maker" width="180" />
        <el-table-column prop="locationType" label="LocationType"  />
        <el-table-column prop="latitude" label="latitude"  />
        <el-table-column prop="longitude" label="longitude"  />
        <el-table-column prop="h3index" label="h3index"  />
        <el-table-column prop="createTime" label="创建时间" width="180">
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
import {deviceList} from '@/api/miners.js'
import {onMounted, reactive} from "vue";
import { useRouter } from 'vue-router'
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
      loadDeviceList()
    }
    const pageCurrentChange = (currentPage) => {
      data.query.page.currentPage=currentPage
      loadDeviceList()
    }

    const search=()=>{
      router.push({
        name:"MinersInfo",
        path: '/miner/'+data.query.address,
        params:{
          address:data.query.address
        }
      })
    }

    const loadDeviceList=()=>{
      const params={
        offset:data.query.page.currentPage,
        limit:data.query.page.pageSize
      }
      deviceList(params).then((result)=>{
          data.query.page.total=result.data.total
          data.tableList=result.data.items
      }).catch((err) =>{
        console.log(err);
      });
    }

    onMounted(() => {
      console.log("onMounted")
      loadDeviceList()
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
