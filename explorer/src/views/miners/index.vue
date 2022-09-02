<template>
  <el-breadcrumb style="margin-bottom: 20px;">
    <el-breadcrumb-item :to="{ path: '/' }">{{$t('miners.path.home')}}</el-breadcrumb-item>
    <el-breadcrumb-item >{{$t('miners.path.miners')}}</el-breadcrumb-item>
  </el-breadcrumb>
  <el-row :gutter="24">
    <el-col :span="20">
      <div class="mt-4">
        <el-input v-model="data.query.address" :placeholder="$t('miners.query.searchPlaceHolder')">
          <template #append>
            <el-button type="primary" @click="search">{{$t('miners.query.searchButton')}}</el-button>
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
        <el-table-column prop="address" :label="$t('miners.table.address')" width="210px" :show-overflow-tooltip=true>
          <template #default="scope">
            <router-link :to="'/miner/'+scope.row.address">{{scope.row.address}}</router-link>
          </template>
        </el-table-column>
        <el-table-column prop="minerType" :label="$t('miners.table.minerType')" width="180" >
          <template #default="scope">
            {{scope.row.minerType?Constant.MinerType[scope.row.minerType]:''}}
          </template>
        </el-table-column>
        <el-table-column prop="ownerAddress" :label="$t('miners.table.ownerAddress')" width="210px" :show-overflow-tooltip="true">
          <template #default="scope">
            <router-link :to="'/account/'+scope.row.ownerAddress">{{scope.row.ownerAddress}}</router-link>
          </template>
        </el-table-column>
        <el-table-column prop="earningMint" :label="$t('miners.table.earningMint')"/>
        <el-table-column prop="earningService" :label="$t('miners.table.earningService')"  />
        <el-table-column prop="power" :label="$t('miners.table.power')"  />
        <el-table-column prop="totalEnergyGeneration" :label="$t('miners.table.totalEnergyGeneration')">
          <template #default="scope">
            {{(scope.row.totalEnergyGeneration/1000/1000).toFixed(3)}}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('miners.table.createTime')" width="180">
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
import { formatDate,formatString } from '@/utils/data_format.js'
import {deviceList} from '@/api/miners.js'
import {onMounted, reactive} from "vue";
import { useRoute  } from 'vue-router'
export default {
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
          pageSize:20,
          total:0
        }
      },
      tableList :[]
    })
    const route = useRoute()

    const pageSizeChange = (pageSize) => {
      data.query.page.pageSize=pageSize
      loadDeviceList()
    }
    const pageCurrentChange = (currentPage) => {
      data.query.page.currentPage=currentPage
      loadDeviceList()
    }

    const search=()=>{
      data.query.page.currentPage=1
      data.query.page.pageSize=10
      loadDeviceList()
    }

    const loadDeviceList=()=>{
      const params={
        offset:data.query.page.currentPage,
        limit:data.query.page.pageSize,
        address:data.query.address
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
      data.query.address=route.params.ownerAddress;
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
