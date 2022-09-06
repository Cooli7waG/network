<template>
  <el-breadcrumb style="margin-bottom: 20px;">
    <el-breadcrumb-item :to="{ path: '/wallet/miners' }">My Miners</el-breadcrumb-item>
    <el-breadcrumb-item>{{ $t('minerinfo.path.info') }}</el-breadcrumb-item>
  </el-breadcrumb>
  <div v-if="data.device">
    <div>
      <el-row :gutter="20">
        <el-col :xs="12" :sm="8" :md="7" :lg="6" :xl="4">{{ $t('minerinfo.info.address') }}:</el-col>
        <el-col :xs="12" :sm="16" :md="17" :lg="18" :xl="20">{{ data.device.address }}</el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :xs="12" :sm="8" :md="7" :lg="6" :xl="4">{{ $t('minerinfo.info.minerType') }}:</el-col>
        <el-col :xs="12" :sm="16" :md="17" :lg="18" :xl="20">
          {{ data.device.minerType ? Constant.MinerType[data.device.minerType] : '' }}
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :xs="12" :sm="8" :md="7" :lg="6" :xl="4">{{ $t('minerinfo.info.ownerAddress') }}:</el-col>
        <el-col :xs="12" :sm="16" :md="17" :lg="18" :xl="20">{{ data.device.ownerAddress }}</el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :xs="12" :sm="8" :md="7" :lg="6" :xl="4">{{ $t('minerinfo.info.maker') }}:</el-col>
        <el-col :xs="12" :sm="16" :md="17" :lg="18" :xl="20">{{ data.device.maker }}</el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :xs="12" :sm="8" :md="7" :lg="6" :xl="4">{{ $t('minerinfo.info.latlog') }}:</el-col>
        <el-col :xs="12" :sm="16" :md="17" :lg="18" :xl="20">
          {{ data.device.latitude + "," + data.device.longitude }}
          <i class="iconfont icon-weizhi" style="color: deepskyblue;cursor: pointer" @click="gotoMap(data.device.latitude,data.device.longitude)"/>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :xs="12" :sm="8" :md="7" :lg="6" :xl="4">{{ $t('minerinfo.info.createTime') }}:</el-col>
        <el-col :xs="12" :sm="16" :md="17" :lg="18" :xl="20">
          {{ formatDate(data.device.createTime, "yyyy-MM-dd hh:mm:ss") }}
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :xs="12" :sm="8" :md="7" :lg="6" :xl="4">{{ $t('minerinfo.info.earningMint') }}:</el-col>
        <el-col :xs="12" :sm="16" :md="17" :lg="18" :xl="20">{{ formatToken(data.device.earningMint) }}</el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :xs="12" :sm="8" :md="7" :lg="6" :xl="4">{{ $t('minerinfo.info.earningService') }}:</el-col>
        <el-col :xs="12" :sm="16" :md="17" :lg="18" :xl="20">{{ formatToken(data.device.earningService) }}</el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :xs="12" :sm="8" :md="7" :lg="6" :xl="4">{{ $t('minerinfo.info.power') }}:</el-col>
        <el-col :xs="12" :sm="16" :md="17" :lg="18" :xl="20">{{ formatPower(data.device.avgPower/1000/ 1000) }}</el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :xs="12" :sm="8" :md="7" :lg="6" :xl="4">{{ $t('minerinfo.info.totalEnergyGeneration') }}:</el-col>
        <el-col :xs="12" :sm="16" :md="17" :lg="18" :xl="20">
          {{ (data.device.totalEnergyGeneration / 1000 / 1000).toFixed(3) }}
        </el-col>
      </el-row>
    </div>
    <el-tabs v-model="data.activeName" @tab-click="handleClick" style="margin-top: 15px" type="border-card">
      <el-tab-pane label="Report" name="report">
        <el-pagination
            v-model:currentPage="data.page.offset"
            v-model:page-size="data.page.limit"
            :hide-on-single-page="data.page.hidePage"
            :page-sizes="[10, 25, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="data.page.total"
            @size-change="pageSizeChange"
            @current-change="pageCurrentChange"
            style="margin-top: 5px"
        />
        <el-table v-loading="data.reportLoading" :data="data.reportData" border style="width: 100%;margin-top: 5px">
          <el-table-column prop="hash" label="Transaction Hash" width="220" :show-overflow-tooltip=true>
            <template #default="scope">
              <router-link :to="'/wallet/tx/'+scope.row.hash">{{scope.row.hash}}</router-link>
            </template>
          </el-table-column>
          <el-table-column prop="height" label="Block Height">
          </el-table-column>
          <el-table-column prop="power" label="Power" width="180">
            <template #default="scope">
              {{formatPower((scope.row.power/1000).toFixed(3))}}
            </template>
          </el-table-column>
          <el-table-column prop="total" label="Total">
            <template #default="scope">
              {{formatElectricity((scope.row.total/1000).toFixed(3))}}
            </template>
          </el-table-column>
          <el-table-column prop="timestamp" label="Timestamp">
            <template #default="scope">{{formatDate(new Date(scope.row.timestamp), "yyyy-MM-dd hh:mm:ss")}}</template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="Reward" name="reward">
        <el-pagination
            v-model:currentPage="data.page.offset"
            v-model:page-size="data.page.limit"
            :hide-on-single-page="data.page.hidePage"
            :page-sizes="[10, 25, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="data.page.total"
            @size-change="pageSizeChange"
            @current-change="pageCurrentChange"
            style="margin-top: 5px"
        />
        <el-table v-loading="data.rewardLoading" :data="data.rewardData" border style="width: 100%;margin-top: 5px">
          <el-table-column prop="hash" label="Transaction Hash" width="220" :show-overflow-tooltip=true>
            <template #default="scope">
              <router-link :to="'/wallet/tx/'+scope.row.hash">{{scope.row.hash}}</router-link>
            </template>
          </el-table-column>
          <el-table-column prop="address" label="Miner Address" width="220" :show-overflow-tooltip=true>
          </el-table-column>
          <el-table-column prop="height" label="Block Height">
          </el-table-column>
          <el-table-column prop="amount" label="Amount" width="180">
            <template #default="scope">
              {{formatToken(scope.row.amount)}}
            </template>
          </el-table-column>
          <el-table-column prop="rewardPercent" label="Reward Percent">
            <template #default="scope">
              {{ Number(scope.row.rewardPercent).toFixed(2)}}%
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="Timestamp">
            <template #default="scope">{{formatDate(scope.row.createTime, "yyyy-MM-dd hh:mm:ss")}}</template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="Transaction" name="transaction">
        <el-row :gutter="24">
          <el-form ref="queryForm" :inline="true">
            <el-form-item label="TxHash" prop="txHash">
              <el-input style="width: 240px" v-model="data.page.txHash" placeholder="Please input transaction hash" clearable @keyup.enter.native="handleGetTransaction"/>
            </el-form-item>
            <el-form-item label="TxType" prop="txType">
              <el-select style="width: 260px" v-model="data.page.txType" placeholder="Please select transaction type" clearable @change="handleGetTransaction">
                <el-option v-for="item in data.txTypeOptions" :key="item.value" :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-date-picker
                  v-model="data.dateRangeValue"
                  type="daterange"
                  unlink-panels
                  range-separator="To"
                  start-placeholder="Start date"
                  end-placeholder="End date"
                  :shortcuts="shortcuts"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                  @change="handleGetTransaction"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleGetTransaction">搜索</el-button>
            </el-form-item>
          </el-form>
        </el-row>
        <el-pagination
            v-model:currentPage="data.page.offset"
            v-model:page-size="data.page.limit"
            :hide-on-single-page="data.page.hidePage"
            :page-sizes="[10, 25, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="data.page.total"
            @size-change="pageSizeChange"
            @current-change="pageCurrentChange"
            style="margin-top: 5px"
        />
        <el-table v-loading="data.transactionLoading" :data="data.transactionData" border style="width: 100%;margin-top: 5px">
          <el-table-column prop="hash" label="Transaction Hash" width="220" :show-overflow-tooltip=true>
            <template #default="scope">
              <router-link :to="'/wallet/tx/'+scope.row.hash">{{scope.row.hash}}</router-link>
            </template>
          </el-table-column>
          <el-table-column prop="txType" :label="$t('txs.table.txType')" width="180">
            <template #default="scope">
              {{ scope.row.txType ? Constant.TXType[scope.row.txType] : '' }}
            </template>
          </el-table-column>
          <el-table-column prop="height" label="Block Height"/>
          <el-table-column prop="amount" label="Amount" width="180">
            <template #default="scope">
              {{formatToken(scope.row.amount)}}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="Status" width="100">
            <template #default="scope">
              <el-tag v-if="scope.row.status == 1" class="ml-2" type="success">Success</el-tag>
              <el-tag v-else class="ml-2" type="danger">Failed</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="Timestamp">
            <template #default="scope">{{formatDate(scope.row.createTime, "yyyy-MM-dd hh:mm:ss")}}</template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

  </div>
  <div v-if="data.noData">
    <el-col :sm="12" :lg="6">
      <el-result icon="warning" :title="$t('minerinfo.msg.noData')">
        <template #extra>
          <router-link to="/wallet/miners">{{ $t('common.button.back') }}</router-link>
        </template>
      </el-result>
    </el-col>
  </div>
</template>

<script>
import "@/assets/css/iconfont.css"
import Constant from '@/utils/constant.js'
import {queryByMiner,reportDataList} from '@/api/miners.js'
import {transactionList} from '@/api/transaction.js'
import {onMounted, reactive} from "vue";
import {useRoute} from 'vue-router'
import {formatDate,formatPower,formatNumber,formatElectricity,formatToken} from "@/utils/data_format";
import {getRewardList} from "@/api/account_reward";

export default {
  components: {},
  props: {
    msg: String
  },
  created() {
    this.handleGetList();
  },
  methods: {
    gotoMap(latitude,longitude){
      console.log("latitude:"+latitude+",longitude:"+longitude)
    },
    handleGetList(){
      let url = window.location.href;
      if(url.endsWith("#reward")){
        this.data.activeName = "reward"
        this.handleGetReward();
      }else if(url.endsWith("#transaction")){
        this.data.activeName = "transaction"
        this.handleGetTransaction();
      }else {
        this.data.activeName = "report"
        this.handleGetReport();
      }
    },
    pageSizeChange(pageSize) {
      this.data.page.limit = pageSize
      if(this.data.page.tag == 1){
        this.data.reportLoading = true;
        this.handleGetReport();
      }else if(this.data.page.tag == 2){
        this.data.rewardLoading = true;
        this.handleGetReward();
      }else if(this.data.page.tag == 3){
        this.data.transactionLoading = true;
        this.handleGetTransaction();
      }
    },
    pageCurrentChange(currentPage) {
      this.data.page.offset = currentPage
      if(this.data.page.tag == 1){
        this.data.reportLoading = true;
        this.handleGetReport();
      }else if(this.data.page.tag == 2){
        this.data.rewardLoading = true;
        this.handleGetReward();
      }else if(this.data.page.tag == 3){
        this.data.transactionLoading = true;
        this.handleGetTransaction();
      }
    },
    handleClick(tab) {
      this.data.page.offset = 1;
      if (tab.props.name == 'report') {
        this.data.page.tag = 1;
        this.data.reportLoading = true;
        this.handleGetReport();
        let url = window.location.href;
        let urlArr = url.split("#");
        url = urlArr[0]+"#report"
        history.pushState('','',url)
        return;
      }
      //
      if (tab.props.name == 'reward') {
        this.data.page.tag = 2;
        this.data.rewardLoading = true;
        this.handleGetReward();
        let url = window.location.href;
        let urlArr = url.split("#");
        url = urlArr[0]+"#reward"
        history.pushState('','',url)
        return;
      }
      //
      if (tab.props.name == 'transaction') {
        this.data.page.tag = 3;
        this.data.rewardLoading = true;
        this.handleGetTransaction();
        let url = window.location.href;
        let urlArr = url.split("#");
        url = urlArr[0]+"#transaction"
        history.pushState('','',url)
      }
    },
    handleGetReport(){
      this.data.page.address = this.$route.params.address;
      reportDataList(this.data.page).then(rsp =>{
        this.data.page.total = rsp.data.total;
        this.data.reportData = rsp.data.items
        this.data.reportLoading = false;
      })
    },
    handleGetReward(){
      this.data.page.address = this.$route.params.address;
      getRewardList(this.data.page).then(rsp =>{
        this.data.page.total = rsp.data.total;
        this.data.rewardData = rsp.data.items
        this.data.rewardLoading = false;
      })
    },
    handleGetTransaction(){
      this.data.page.address = this.$route.params.address;
      if(this.data.dateRangeValue){
        this.data.page.startTime = this.data.dateRangeValue[0]
        this.data.page.endTime = this.data.dateRangeValue[1]
      }else {
        this.data.page.startTime = undefined
        this.data.page.endTime = undefined
      }
      transactionList(this.data.page).then(rsp =>{
        this.data.page.total = rsp.data.total;
        this.data.transactionData = rsp.data.items
        this.data.transactionLoading = false;
      })
    },
  },
  computed: {
    formatDate() {
      return formatDate
    },
    formatElectricity() {
      return formatElectricity
    },
    formatPower() {
      return formatPower
    },
    formatNumber() {
      return formatNumber
    },
    formatToken() {
      return formatToken
    },
    Constant() {
      return Constant
    }
  },
  setup() {
    const route = useRoute()
    const data = reactive({
      query: {
        minerAddress: ''
      },
      txTypeOptions:[
        {value:1,label:"TX_Register_miner"},
        {value:2,label:"TX_Onboard_Miner"},
        {value:3,label:"TX_Transfer_Miner"},
        {value:4,label:"TX_Terminate_Miner"},
        {value:5,label:"TX_Airdrop_Miner"},
        {value:6,label:"TX_Claim_Miner"},
        {value:6,label:"TX_Claim_Miner"},
        {value:7,label:"TX_Commit_PoGG"},
        {value:8,label:"TX_Report_PoGG"},
        {value:9,label:"TX_Reward_PoGG"},
        {value:10,label:"TX_Transfer"},
      ],
      dateRangeValue: [],
      page:{
        hidePage:true,
        address:null,
        txType:null,
        startTime:null,
        endTime:null,
        offset:1,
        limit:25,
        total:0,
        tag:1
      },
      activeName: 'report',
      device: '',
      noData: false,
      reportLoading:true,
      reportData:undefined,
      rewardLoading:true,
      rewardData:undefined,
      transactionLoading:true,
      transactionData:undefined,
    })
    const shortcuts = [
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
    const loadQueryByMiner = () => {
      queryByMiner(data.query.minerAddress).then((result) => {
        if (result.data == null) {
          data.noData = true
        }
        data.device = result.data
      }).catch((err) => {
        console.log(err);
      });
    }
    onMounted(() => {
      data.query.minerAddress = route.params.address;
      data.page.address = route.params.address;
      loadQueryByMiner()
    })
    return {
      data,
      shortcuts
    }
  }
}
</script>
<style lang="scss" scoped>
.el-tabs1 {
  --el-tabs-header-height: 40px;
  border: 1px solid #e7eaf3;
  border-radius: 0.5rem;
}
.el-pagination{
  justify-content: flex-end;
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
</style>
