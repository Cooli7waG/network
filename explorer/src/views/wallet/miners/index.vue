<template>
  <el-row :gutter="24">
    <el-col :span="20">
      <div v-show="false" class="mt-4">
        <el-input v-model="data.query.address" :placeholder="$t('miners.query.searchPlaceHolder')" v-on:keydown.enter="search">
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
          :currentPage="data.query.page.currentPage"
          :page-size="data.query.page.pageSize"
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
            <router-link :to="'/wallet/miner/'+scope.row.address">{{scope.row.address}}</router-link>
          </template>
        </el-table-column>
        <el-table-column prop="minerType" :label="$t('miners.table.minerType')" width="180" >
          <template #default="scope">
            {{scope.row.minerType?Constant.MinerType[scope.row.minerType]:''}}
          </template>
        </el-table-column>
        <el-table-column prop="earningMint" :label="$t('miners.table.earningMint')">
          <template #default="scope">
            {{Number(scope.row.earningMint).toLocaleString()}} AKRE
          </template>
        </el-table-column>
        <!--<el-table-column prop="earningService" :label="$t('miners.table.earningService')"  />-->
        <el-table-column prop="power" :label="$t('miners.table.power')">
          <template #default="scope">
            {{formatPowerNotUnit(scope.row.power)}}
          </template>
        </el-table-column>
        <el-table-column prop="totalEnergyGeneration" :label="$t('miners.table.totalEnergyGeneration')">
          <template #default="scope">
            {{formatPowerNotUnit(scope.row.totalEnergyGeneration)}}
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
import { formatDate,formatString,formatPowerNotUnit } from '@/utils/data_format.js'
import {deviceList} from '@/api/miners.js'
import {onMounted, reactive} from "vue";
import {getMetaMaskLoginUserAddress} from "@/api/metamask_utils";
import {useRoute, useRouter} from "vue-router";

export default {
  name:"wallet-miner-list",
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
    formatPowerNotUnit() {
      return formatPowerNotUnit
    },
    Constant() {
      return Constant
    }
  },
  setup() {
    const router = useRouter();
    const data = reactive({
      query: {
        address: undefined,
        page: {
          currentPage: 1,
          pageSize: 20,
          total: 0
        }
      },
      tableList: []
    })
    const route = useRoute()

    const changUrl = () =>{
      let url = router.currentRoute.value.path;
      router.push({path:url,query:{pageSize:data.query.page.pageSize,currentPage:data.query.page.currentPage}});
    }

    const pageSizeChange = (pageSize) => {
      data.query.page.pageSize = pageSize
      data.query.page.currentPage = 1
      changUrl()
      loadDeviceList()
    }
    const pageCurrentChange = (currentPage) => {
      data.query.page.currentPage = currentPage
      changUrl()
      loadDeviceList()
    }

    const search = () => {
      data.query.page.currentPage = 1
      changUrl()
      loadDeviceList()
    }

    const loadDeviceList = () => {
      if (data.query.address == undefined) {
        return;
      }
      const params = {
        offset: data.query.page.currentPage,
        limit: data.query.page.pageSize,
        address: data.query.address
      }
      deviceList(params).then((result) => {
        data.query.page.total = result.data.total
        data.tableList = result.data.items
      }).catch((err) => {
        console.log(err);
      });
    }

    onMounted(() => {
      data.query.address = getMetaMaskLoginUserAddress();
      data.query.page.currentPage = Number(route.query.currentPage==undefined?1:route.query.currentPage)
      data.query.page.pageSize = Number(route.query.pageSize==undefined?20:route.query.pageSize)
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
