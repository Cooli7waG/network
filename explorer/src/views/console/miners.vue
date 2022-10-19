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
        <el-table-column prop="address" label="Address" width="450">
          <template #default="scope">
            <router-link :to="'/miner/'+scope.row.address">{{scope.row.address}}</router-link>
          </template>
        </el-table-column>
        <el-table-column prop="minerType" label="miner 类型" width="180" />
        <el-table-column prop="maker" label="Maker" width="180" />
        <el-table-column prop="locationType" label="LocationType" width="180" />
        <el-table-column prop="latitude" label="latitude" width="180" />
        <el-table-column prop="longitude" label="longitude" width="180" />
        <el-table-column prop="h3index" label="h3index" width="180" />
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
import { formatDate } from '@/utils/data_format.js'
import {deviceList} from '@/api/miners.js'
import {computed, onMounted, reactive} from "vue";
import { useRouter } from 'vue-router'
import { useStore, mapState } from "vuex"
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
    const store = useStore()
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
      if(store.state.accountType==1){
        params["accountId"]=store.state.accountId
      }else{
        params["address"]=store.state.address
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
