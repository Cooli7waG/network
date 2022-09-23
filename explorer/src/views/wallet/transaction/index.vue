<template>
  <el-row :gutter="24">
    <el-form :model="data.query" ref="queryForm" :inline="true">
      <el-form-item label="TxHash" prop="txHash">
        <el-input style="width: 240px" v-model="data.query.txHash" placeholder="Please input transaction hash" clearable @keyup.enter.native="loadTransactionList"/>
      </el-form-item>
      <el-form-item label="TxType" prop="txType">
        <el-select style="width: 260px" v-model="data.query.txType" placeholder="Please select transaction type" clearable @change="loadTransactionList">
          <el-option v-for="item in data.txTypeOptions" :key="item.value" :label="item.label" :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadTransactionList">搜索</el-button>
        <el-button size="mini" >重置</el-button>
      </el-form-item>
    </el-form>
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
      <el-table :data="data.tableList" stripe border style="width: 100%;margin-top: 5px">
        <el-table-column prop="hash" :label="$t('txs.table.hash')" width="220" :show-overflow-tooltip=true>
          <template #default="scope">
            <router-link :to="'/wallet/tx/'+scope.row.hash">{{scope.row.hash }}</router-link>
          </template>
        </el-table-column>
        <el-table-column prop="height" :label="$t('txs.table.height')" width="180">
          <template #default="scope">
            <el-link @click="()=>{data.query.keyword=scope.row.height;search();}">{{ Number(scope.row.height).toLocaleString()}}</el-link>
          </template>
        </el-table-column>

        <el-table-column prop="txType" :label="$t('txs.table.txType')" width="180">
          <template #default="scope">
            {{ scope.row.txType ? Constant.TXType[scope.row.txType] : '' }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('txs.table.txTime')">
          <template #default="scope">
            {{ formatDate(scope.row.createTime, "yyyy-MM-dd hh:mm:ss") }}
          </template>
        </el-table-column>
      </el-table>
    </el-col>
  </el-row>
</template>

<script>
import Constant from '@/utils/constant.js'
import {formatDate, formatString} from '@/utils/data_format.js'
import {getOwnerTransactionList} from '@/api/transaction.js'
import {onMounted, reactive} from "vue";
import {useRoute} from 'vue-router'
import {getMetaMaskLoginUserAddress} from "@/api/metamask_utils";

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
  setup() {

    const data = reactive({
      query: {
        owner:undefined,
        txHash: '',
        txType: '',
        minerAddress: '',
        page: {
          currentPage: 1,
          pageSize: 20,
          total: 0
        }
      },
      minerOptions:[
      ],
      txTypeOptions:[
        {value:2,label:"TX_Onboard_Miner"},
        {value:3,label:"TX_Transfer_Miner"},
        {value:4,label:"TX_Terminate_Miner"},
        {value:5,label:"TX_Airdrop_Miner"},
        {value:6,label:"TX_Claim_Miner"},
        {value:9,label:"TX_Reward_PoGG"},
      ],
      tableList: []
    })
    const route = useRoute()

    const pageSizeChange = (pageSize) => {
      data.query.page.pageSize = pageSize
      loadTransactionList()
    }
    const pageCurrentChange = (currentPage) => {
      data.query.page.currentPage = currentPage
      loadTransactionList()
    }

    const search = () => {
      data.query.page.currentPage = 1
      data.query.page.pageSize = 10
      loadTransactionList()
    }

    const loadTransactionList = () => {
      let MateMaskAddress = getMetaMaskLoginUserAddress()
      if(MateMaskAddress==undefined || MateMaskAddress == null){
        return;
      }
      data.query.owner = MateMaskAddress;
      const params = {
        owner: data.query.owner,
        txType: data.query.txType,
        txHash: data.query.txHash,
        offset: data.query.page.currentPage,
        limit: data.query.page.pageSize
      }
      getOwnerTransactionList(params).then((result) => {
        data.query.page.total = result.data.total
        data.tableList = result.data.items
      }).catch((err) => {
        console.log(err);
      });
    }

    onMounted(() => {
      if (route.query.keyword) {
        data.query.keyword = route.query.keyword
      }
      loadTransactionList();
    })

    return {
      data,
      pageSizeChange,
      pageCurrentChange,
      search,
      loadTransactionList,
    }
  }
}
</script>
<style lang="scss" scoped>
.el-menu {
  border-right: solid 0px var(--el-menu-border-color) !important;
  list-style: none;
  position: relative;
  margin: 0;
  padding-left: 0;
  background-color: var(--el-menu-bg-color);
  box-sizing: border-box;
}

.box-card {
  .item {
    margin-bottom: 20px;

    .lable {
      font-weight: bold;
      margin-bottom: 6px;
    }
  }

}

.el-pagination {
  justify-content: flex-end;
}
</style>
