<template>
  <el-breadcrumb style="margin-bottom: 20px;">
    <el-breadcrumb-item :to="{ path: '/' }">{{ $t('txs.path.home') }}</el-breadcrumb-item>
    <el-breadcrumb-item>{{ $t('txs.path.txs') }}</el-breadcrumb-item>
  </el-breadcrumb>
  <el-row :gutter="24">
    <el-col :span="20">
      <div class="mt-4">
        <el-input
            v-model="data.query.keyword" :placeholder="$t('txs.query.searchPlaceHolder')"
            class="input-with-select"
        >
          <template #prepend>
            <el-select v-model="data.query.select" placeholder="Select" style="width: 115px">
              <el-option :label="$t('txs.query.searchType.1')" value="1"/>
              <el-option :label="$t('txs.query.searchType.2')" value="2"/>
              <el-option :label="$t('txs.query.searchType.3')" value="3"/>
            </el-select>
          </template>
          <template #append>
            <el-button type="primary" @click="search">{{ $t('txs.query.searchButton') }}</el-button>
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
      <el-table :data="data.tableList" stripe border style="width: 100%;margin-top: 5px">
        <el-table-column prop="hash" :label="$t('txs.table.hash')" width="220" :show-overflow-tooltip=true>
          <template #default="scope">
            <router-link :to="'/tx/'+scope.row.hash">{{scope.row.hash }}</router-link>
          </template>
        </el-table-column>
        <el-table-column prop="height" :label="$t('txs.table.height')" width="180">
          <template #default="scope">
            <el-link @click="()=>{data.query.keyword=scope.row.height;search();}">{{ scope.row.height }}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="miner" label="Miner Address" width="220" :show-overflow-tooltip=true>
          <template #default="scope">
            <router-link :to="'/miner/'+scope.row.miner">{{scope.row.miner }}</router-link>
          </template>
        </el-table-column>
        <el-table-column prop="miner" label="Owner Address" width="220" :show-overflow-tooltip=true>
          <template #default="scope">
            <router-link :to="'/caaount/'+scope.row.owner">{{scope.row.owner }}</router-link>
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
import {transactionList} from '@/api/transaction.js'
import {onMounted, reactive} from "vue";
import {useRoute, useRouter} from 'vue-router'

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
        select: "1",
        keyword: '',
        page: {
          currentPage: 1,
          pageSize: 10,
          total: 0
        }
      },
      tableList: []
    })
    const router = useRouter()
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
      const params = {
        offset: data.query.page.currentPage,
        limit: data.query.page.pageSize
      }
      if (data.query.select == "1") {
        params['hash'] = data.query.keyword
      } else if (data.query.select == "2") {
        params['height'] = data.query.keyword
      } else if (data.query.select == "3") {
        params['address'] = data.query.keyword
      }
      transactionList(params).then((result) => {
        data.query.page.total = result.data.total
        data.tableList = result.data.items
      }).catch((err) => {
        console.log(err);
      });
    }

    onMounted(() => {
      console.log("onMounted")
      if (route.query.keyword) {
        data.query.keyword = route.query.keyword
      }
      if (route.params.height) {
        data.query.select = "2"
        data.query.keyword = route.params.height;
      }
      loadTransactionList()
    })

    return {
      data,
      pageSizeChange,
      pageCurrentChange,
      search,
      loadTransactionList
    }
  }
}
</script>
<style lang="scss" scoped>
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
