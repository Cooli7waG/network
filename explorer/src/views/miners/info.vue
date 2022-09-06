<template>
  <el-breadcrumb style="margin-bottom: 20px;">
    <el-breadcrumb-item :to="{ path: '/' }">{{ $t('minerinfo.path.home') }}</el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: '/miners' }">{{ $t('minerinfo.path.miners') }}</el-breadcrumb-item>
    <el-breadcrumb-item>{{ $t('minerinfo.path.info') }}</el-breadcrumb-item>
  </el-breadcrumb>
  <div v-if="data.device">
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
              <router-link :to="'/tx/'+scope.row.hash">{{scope.row.hash}}</router-link>
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
              <router-link :to="'/tx/'+scope.row.hash">{{scope.row.hash}}</router-link>
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
    </el-tabs>

  </div>
  <div v-if="data.noData">
    <el-col :sm="12" :lg="6">
      <el-result icon="warning" :title="$t('minerinfo.msg.noData')">
        <template #extra>
          <router-link to="/miners">{{ $t('common.button.back') }}</router-link>
        </template>
      </el-result>
    </el-col>
  </div>
</template>

<script>
import "@/assets/css/iconfont.css"
import Constant from '@/utils/constant.js'
import {queryByMiner,reportDataList} from '@/api/miners.js'
import {getRewardList} from '@/api/account_reward.js'
import {onMounted, reactive} from "vue";
import {useRoute} from 'vue-router'
import {formatDate,formatPower,formatNumber,formatElectricity,formatToken} from "@/utils/data_format";

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
      }
    },
    handleClick(tab) {
      this.data.page.offset = 1;
      if (tab.props.name == 'report') {
        this.data.page.tag = 1;
        this.data.reportLoading = true;
        this.handleGetReport();
        let url = window.location.href;
        if(!url.endsWith("#report")){
          if(url.endsWith("#reward")){
            url = url.replace("#reward","#report")
          }else {
            url = url+"#report";
          }
        }
        history.pushState('','',url)
      }
      if (tab.props.name == 'reward') {
        this.data.page.tag = 2;
        this.data.rewardLoading = true;
        this.handleGetReward();
        let url = window.location.href;
        if(!url.endsWith("#reward")){
          if(url.endsWith("#report")){
            url = url.replace("#report","#reward")
          }else {
            url = url+"#reward";
          }
        }
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
    }
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
      page:{
        hidePage:true,
        address:null,
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
    })
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
      data
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
