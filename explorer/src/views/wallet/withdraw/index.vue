<template>
  <el-row :gutter="24">
    <el-form ref="queryForm" :inline="true" style="padding-left: 15px">
      <el-form-item>
        <span style="margin-right: 8px">Withdraw Time</span>
        <el-date-picker
            v-model="data.query.dateRangeValue"
            type="daterange"
            unlink-panels
            range-separator="To"
            start-placeholder="Start date"
            end-placeholder="End date"
            :shortcuts="data.shortcuts"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            @change="loadWithdrawList"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadWithdrawList">搜索</el-button>
      </el-form-item>
    </el-form>
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
          style="margin-top: 5px"/>
      <el-table v-loading="data.listLoad" :data="data.tableList" stripe border style="width: 100%;margin-top: 5px">
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
        <el-table-column prop="amount" :label="$t('txs.table.amount')" width="180">
          <template #default="scope">
            {{ Number(scope.row.amount).toLocaleString()}}
          </template>
        </el-table-column>
        <el-table-column prop="nonce" :label="$t('txs.table.nonce')" width="180">
          <template #default="scope">
            {{ scope.row.nonce}}
          </template>
        </el-table-column>
        <el-table-column prop="status" :label="$t('txs.table.status')" width="180">
          <template #default="scope">
            <el-tag v-if="scope.row.status" class="ml-2" type="success">Success</el-tag>
            <el-tag v-else class="ml-2" type="danger">Failed</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('txs.table.txTime')">
          <template #default="scope">
            {{ formatDate(scope.row.createTime, "yyyy-MM-dd hh:mm:ss") }}
          </template>
        </el-table-column>
        <el-table-column prop="operation" :label="$t('txs.table.operation')" style="text-align: center">
          <template #default="scope">
            <el-button :disabled="data.btnLoad" v-if="!scope.row.status" type="warning" @click="reWithdraw(scope.row)">Withdraw</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
  </el-row>
</template>

<script>
import Constant from '@/utils/constant.js'
import {formatDate, formatString} from '@/utils/data_format.js'
import {getMetaMaskLoginUserAddress, switchNetwork} from "@/api/metamask_utils";
import {etherNonces, etherWithdraw, getTransactionStatus} from "@/api/contract_utils";
import {transactionList} from "@/api/transaction";
import {onMounted, reactive} from "vue";
import {useRoute} from "vue-router";

export default {
  name:"wallet-withdraw",
  props: {
    msg: String
  },
  setup(){
    const route = useRoute()
    const data = reactive({
      listLoad:false,
      btnLoad:false,
      query: {
        owner:undefined,
        txHash: '',
        dateRangeValue: [],
        minerAddress: '',
        page: {
          currentPage: 1,
          pageSize: 20,
          total: 0
        }
      },
      minerOptions:[
      ],
      tableList: [],
      shortcuts : [
        {
          text: 'Last week',
          value: () => {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            return [start, end]
          },
        },
        {
          text: 'Last month',
          value: () => {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            return [start, end]
          },
        },
        {
          text: 'Last 3 months',
          value: () => {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            return [start, end]
          },
        },
      ]
    });
    onMounted(() => {
      data.query.page.pageSize = Number(route.query.pageSize==undefined?20:route.query.pageSize)
      data.query.page.currentPage = Number(route.query.currentPage==undefined?1:route.query.currentPage)
    })
    return {
      data
    }
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
  methods:{
    async reWithdraw(row) {
      this.data.btnLoad = true;
      if(!await switchNetwork("0x13881")){
        this.data.btnLoad = false;
        this.$message.error(this.$t('common.msg.metaMaskNetWorkNotFound'));
        return;
      }else {
        try {
          let hash = await etherWithdraw("" + row.data.metaTx.originData.value, row.nonce, row.data.metaTx.sig.v, "0x" + row.data.metaTx.sig.r, "0x" + row.data.metaTx.sig.s)
          console.info("withdraw hash Result hash:" + hash)
          this.$message.info("The withdraw has been submitted. Please wait...")
          let status = await getTransactionStatus(hash);
          console.log('Transaction Status: ' + status);
          if (status == 0) {
            this.$message.error("withdraw failed")
          } else {
            this.$message.success("withdraw success,Please check your wallet!")
            await this.loadWithdrawList();
          }
          this.data.btnLoad = false;
        } catch (err) {
          let str = 'Error: user rejected transaction';
          if (err.toString().indexOf(str) != -1) {
            this.$message.warning("user rejected transaction")
          }
          console.log(err)
          this.data.btnLoad = false;
        }
      }
    },
    async loadWithdrawList(){
      //
      this.data.listLoad = true;
      if(!await switchNetwork("0x13881")){
        this.$message.error(this.$t('common.msg.metaMaskNetWorkNotFound'));
        this.data.listLoad = false;
        return;
      }else {
        if (this.$route.query.keyword) {
          this.data.query.keyword = this.$route.query.keyword
        }
        let MateMaskAddress = getMetaMaskLoginUserAddress()
        if (MateMaskAddress == undefined || MateMaskAddress == null) {
          this.data.listLoad = false;
          return;
        }
        this.data.query.owner = MateMaskAddress;
        const params = {
          address: this.data.query.owner,
          txType: 11,
          offset: Number(this.$route.query.currentPage==undefined?1:this.$route.query.currentPage),
          limit: Number(this.$route.query.pageSize==undefined?20:this.$route.query.pageSize)
        }
        if(this.data.query.dateRangeValue && this.data.query.dateRangeValue.length==2){
          params.startTime = this.data.query.dateRangeValue[0]
          params.endTime = this.data.query.dateRangeValue[1]
        }
        let newNonce = await etherNonces();
        transactionList(params).then(async (result) => {
          if(result.code == 0){
            this.data.tableList = [];
            this.data.query.page.total = result.data.total
            let items = result.data.items;
            for (let key in items) {
              let dataObj = JSON.parse(items[key].data.replace("\\", ""))
              let item = {
                hash: items[key].hash,
                createTime: items[key].createTime,
                height: items[key].height,
                amount: dataObj.withDrawReq.amount,
                nonce: dataObj.metaTx.originData.nonce,
                status: dataObj.metaTx.originData.nonce>=newNonce?false:true,
                data: dataObj
              }
              this.data.tableList.push(item)
            }
          }
          this.data.listLoad = false;
        }).catch((err) => {
          this.data.listLoad = false;
          console.log(err);
        });
      }
    },
    pageSizeChange(pageSize){
      this.data.query.page.pageSize = pageSize
      this.data.query.page.currentPage = 1
      this.changUrl();
      this.loadWithdrawList()
    },
    pageCurrentChange(currentPage){
      this.data.query.page.currentPage = currentPage
      this.changUrl();
      this.loadWithdrawList()
    },
    search(){
      this.data.query.page.currentPage = 1
      this.changUrl();
      this.loadWithdrawList()
    },
    changUrl(){
      let url = this.$router.currentRoute.value.path;
      this.$router.push({path:url,query:{pageSize:this.data.query.page.pageSize,currentPage:this.data.query.page.currentPage}});
    }
  },
  created() {
    this.loadWithdrawList();
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
