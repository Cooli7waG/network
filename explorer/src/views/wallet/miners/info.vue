<template>
  <div v-if="data.device">
    <el-row>
      <el-col :span="13">
        <el-breadcrumb style="margin-bottom: 20px;">
          <el-breadcrumb-item :to="{ path: '/wallet/miners' }">My Miners</el-breadcrumb-item>
          <el-breadcrumb-item>{{ $t('minerinfo.path.info') }}</el-breadcrumb-item>
        </el-breadcrumb>
        <el-row :gutter="20">
          <el-col :xs="12" :sm="10" :md="10" :lg="8" :xl="6">{{ $t('minerinfo.info.address') }}:</el-col>
          <el-col :xs="12" :sm="14" :md="14" :lg="14" :xl="18" style="height: 25px;line-height: 25px">
              <span style="vertical-align: middle;margin-right: 5px">
                {{ data.device.address }}
              </span>
            <span v-show="data.nftInfo" style="cursor: pointer" @click="gotoOpenSea()">
                <el-tooltip class="box-item" effect="dark" content="Open this NFT on opensea.io" placement="right">
                  <svg xmlns="http://www.w3.org/2000/svg" style="vertical-align: middle" width="18" height="18" fill="none" viewBox="0 0 100 100"><path fill="#2081E2" d="M100 50C100 77.6127 77.6127 100 50 100C22.3873 100 0 77.6127 0 50C0 22.3873 22.3873 0 50 0C77.6185 0 100 22.3873 100 50Z"/><path fill="#fff" d="M24.6679 51.6801L24.8836 51.341L37.8906 30.9932C38.0807 30.6953 38.5276 30.7261 38.6714 31.0497C40.8444 35.9196 42.7194 41.9762 41.841 45.7468C41.466 47.2982 40.4386 49.3992 39.2827 51.341C39.1338 51.6236 38.9694 51.901 38.7947 52.1681C38.7125 52.2914 38.5738 52.3633 38.4248 52.3633H25.048C24.6884 52.3633 24.4778 51.9729 24.6679 51.6801Z"/><path fill="#fff" d="M82.6444 55.461V58.6819C82.6444 58.8668 82.5314 59.0312 82.367 59.1031C81.3602 59.5346 77.9132 61.1168 76.48 63.11C72.8224 68.2008 70.0279 75.48 63.7812 75.48H37.721C28.4847 75.48 21 67.9697 21 58.7024V58.4045C21 58.1579 21.2003 57.9576 21.4469 57.9576H35.9745C36.2621 57.9576 36.4727 58.2247 36.4471 58.5072C36.3443 59.4524 36.519 60.4182 36.9659 61.2966C37.8289 63.0484 39.6166 64.1426 41.5481 64.1426H48.74V58.5278H41.6303C41.2656 58.5278 41.0499 58.1065 41.2605 57.8086C41.3375 57.6904 41.4249 57.5672 41.5173 57.4285C42.1903 56.473 43.1509 54.9884 44.1064 53.2983C44.7588 52.1579 45.3906 50.9404 45.8992 49.7178C46.002 49.4969 46.0841 49.2708 46.1663 49.0499C46.305 48.6595 46.4489 48.2948 46.5516 47.9301C46.6544 47.6218 46.7365 47.2982 46.8187 46.9951C47.0602 45.9574 47.1629 44.8581 47.1629 43.7177C47.1629 43.2708 47.1424 42.8033 47.1013 42.3564C47.0807 41.8684 47.0191 41.3803 46.9574 40.8923C46.9163 40.4608 46.8393 40.0344 46.7571 39.5875C46.6544 38.9351 46.5105 38.2879 46.3461 37.6354L46.2896 37.3889C46.1663 36.9419 46.0636 36.5156 45.9198 36.0687C45.5139 34.6662 45.0465 33.2998 44.5533 32.0207C44.3735 31.5121 44.168 31.0241 43.9625 30.5361C43.6595 29.8015 43.3512 29.1337 43.0687 28.5018C42.9249 28.2141 42.8016 27.9521 42.6783 27.685C42.5396 27.3819 42.3958 27.0788 42.2519 26.7912C42.1492 26.5703 42.031 26.3648 41.9488 26.1593L41.0704 24.536C40.9471 24.3151 41.1526 24.0531 41.394 24.1199L46.8907 25.6096H46.9061C46.9163 25.6096 46.9215 25.6148 46.9266 25.6148L47.6509 25.8151L48.4472 26.0412L48.74 26.1233V22.8562C48.74 21.2791 50.0037 20 51.5654 20C52.3462 20 53.0551 20.3185 53.5637 20.8373C54.0722 21.3562 54.3907 22.0651 54.3907 22.8562V27.7056L54.9764 27.8699C55.0226 27.8854 55.0688 27.9059 55.1099 27.9367C55.2538 28.0446 55.4592 28.2038 55.7212 28.3991C55.9267 28.5634 56.1476 28.7638 56.4147 28.9693C56.9438 29.3956 57.5757 29.9453 58.2692 30.5772C58.4541 30.7364 58.6339 30.9008 58.7983 31.0652C59.6922 31.8974 60.6939 32.8734 61.6494 33.9522C61.9165 34.2553 62.1785 34.5635 62.4456 34.8871C62.7127 35.2159 62.9953 35.5395 63.2418 35.8632C63.5655 36.2947 63.9148 36.7416 64.2179 37.2091C64.3617 37.43 64.5261 37.656 64.6648 37.8769C65.0552 38.4676 65.3994 39.079 65.7282 39.6903C65.8669 39.9728 66.0107 40.281 66.134 40.5841C66.4987 41.4009 66.7864 42.2331 66.9713 43.0653C67.0278 43.2451 67.0689 43.4403 67.0895 43.615V43.6561C67.1511 43.9026 67.1717 44.1646 67.1922 44.4317C67.2744 45.2845 67.2333 46.1372 67.0484 46.9951C66.9713 47.3599 66.8686 47.704 66.7453 48.0688C66.622 48.4181 66.4987 48.7828 66.3395 49.127C66.0313 49.841 65.6665 50.5551 65.235 51.2229C65.0963 51.4695 64.9319 51.7315 64.7675 51.9781C64.5877 52.24 64.4028 52.4866 64.2384 52.7281C64.0124 53.0363 63.771 53.3599 63.5244 53.6476C63.3035 53.9507 63.0775 54.2538 62.8309 54.5209C62.4867 54.9267 62.1579 55.312 61.8137 55.6819C61.6083 55.9233 61.3874 56.1699 61.1613 56.3908C60.9405 56.6373 60.7144 56.8582 60.5089 57.0637C60.1648 57.4079 59.8771 57.675 59.6356 57.8959L59.0706 58.4148C58.9884 58.4867 58.8805 58.5278 58.7675 58.5278H54.3907V64.1426H59.8976C61.1305 64.1426 62.3018 63.7059 63.247 62.9045C63.5706 62.622 64.9833 61.3994 66.6528 59.5552C66.7093 59.4935 66.7813 59.4473 66.8635 59.4268L82.0742 55.0295C82.3568 54.9473 82.6444 55.163 82.6444 55.461Z"/></svg>
                </el-tooltip>
              </span>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :xs="12" :sm="10" :md="10" :lg="8" :xl="6">{{ $t('minerinfo.info.minerType') }}:</el-col>
          <el-col :xs="12" :sm="14" :md="14" :lg="14" :xl="18">
            {{ data.device.minerType ? Constant.MinerType[data.device.minerType] : '' }}
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :xs="12" :sm="10" :md="10" :lg="8" :xl="6">{{ $t('minerinfo.info.ownerAddress') }}:</el-col>
          <el-col :xs="12" :sm="14" :md="14" :lg="14" :xl="18">{{ data.device.ownerAddress }}</el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :xs="12" :sm="10" :md="10" :lg="8" :xl="6">{{ $t('minerinfo.info.maker') }}:</el-col>
          <el-col :xs="12" :sm="14" :md="14" :lg="14" :xl="18">{{ data.device.maker }}</el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :xs="12" :sm="10" :md="10" :lg="8" :xl="6">{{ $t('minerinfo.info.latlog') }}:</el-col>
          <el-col :xs="12" :sm="14" :md="14" :lg="14" :xl="18">
            {{ data.device.latitude + "," + data.device.longitude }}
            <i class="iconfont icon-weizhi" style="color: deepskyblue;cursor: pointer" @click="gotoMap(data.device.latitude,data.device.longitude)"/>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :xs="12" :sm="10" :md="10" :lg="8" :xl="6">{{ $t('minerinfo.info.createTime') }}:</el-col>
          <el-col :xs="12" :sm="14" :md="14" :lg="14" :xl="18">
            {{ formatDate(data.device.createTime, "yyyy-MM-dd hh:mm:ss") }}
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :xs="12" :sm="10" :md="10" :lg="8" :xl="6">{{ $t('minerinfo.info.earningMint') }}:</el-col>
          <el-col :xs="12" :sm="14" :md="14" :lg="14" :xl="18">{{ formatToken(data.device.earningMint) }} gAKRE</el-col>
        </el-row>
        <!--<el-row :gutter="20">
          <el-col :xs="12" :sm="10" :md="10" :lg="8" :xl="6">{{ $t('minerinfo.info.earningService') }}:</el-col>
          <el-col :xs="12" :sm="14" :md="14" :lg="14" :xl="18">{{ formatToken(data.device.earningService) }}</el-col>
        </el-row>-->
        <el-row :gutter="20">
          <el-col :xs="12" :sm="10" :md="10" :lg="8" :xl="6">{{ $t('minerinfo.info.power') }}:</el-col>
          <el-col :xs="12" :sm="14" :md="14" :lg="14" :xl="18">{{ formatPower(data.device.avgPower/1000/ 1000) }}</el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :xs="12" :sm="10" :md="10" :lg="8" :xl="6">{{ $t('minerinfo.info.totalEnergyGeneration') }}:</el-col>
          <el-col :xs="12" :sm="14" :md="14" :lg="14" :xl="18">
            {{ formatElectricity(data.device.totalEnergyGeneration/1000) }}
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="11" style="padding-left: 30px">
        <div id="minerHistogram" style="height:224px;"></div>
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
              <router-link :to="'/wallet/tx/'+scope.row.hash">{{scope.row.hash}}</router-link>
            </template>
          </el-table-column>
          <el-table-column prop="height" label="Block Height">
            <template #default="scope">
              {{Number(scope.row.height).toLocaleString()}}
            </template>
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
          <!--<el-table-column prop="address" label="Miner Address" width="220" :show-overflow-tooltip=true></el-table-column>-->
          <el-table-column prop="height" label="Block Height">
            <template #default="scope">
              {{Number(scope.row.height).toLocaleString()}}
            </template>
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
              <el-input style="width: 240px" v-model="data.page.txHash" placeholder="Please input transaction hash" clearable v-on:keydown.enter="handleGetTransaction"/>
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
              <el-button type="primary" @click="handleSearchTransaction">搜索</el-button>
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
          <el-table-column prop="height" label="Block Height">
            <template #default="scope">
              {{Number(scope.row.height).toLocaleString()}}
            </template>
          </el-table-column>
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
  <div class="showMap" :class="{show:data.dialogMapVisible,hide:!data.dialogMapVisible}">
    <div style="width: 650px;height: 400px;background-color: #FFFFFF;border-radius: 5px;padding: 1px">
      <el-row style="padding: 4px">
        <el-col :span="12"><span><el-icon><MapLocation /></el-icon> Miner Location</span></el-col>
        <el-col :span="12" style="text-align: right">
          <el-icon class="closeMap" @click="data.dialogMapVisible = false"><Close /></el-icon>
        </el-col>
      </el-row>
      <div id="map" style="width: 650px;height: 374px;border-bottom-right-radius: 5px;border-bottom-left-radius: 5px"/>
    </div>
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
import {queryByMiner, reportDataList} from '@/api/miners.js'
import {transactionList} from '@/api/transaction.js'
import {onMounted, reactive} from "vue";
import {useRoute} from 'vue-router'
import {formatDate,formatPower,formatNumber,formatElectricity,formatToken} from "@/utils/data_format";
import {getRewardList} from "@/api/account_reward";
import {Tile as TileLayer, Vector as VectorLayer} from "ol/layer";
import XYZSource from "ol/source/XYZ";
import {Feature, Map, View} from "ol";
import {Point} from "ol/geom";
import {Vector as VectorSource} from "ol/source";
import {statisticsRewardByDay} from "@/api/walletDashboard";
import * as echarts from "echarts";
import {getWalletMinerPageInfo, setWalletMinerPageInfo} from "@/api/browserUtils";
import {getMinerTokenID} from "@/api/contract_utils";

export default {
  name:'wallet-miner-info',
  components: {},
  props: {
    msg: String
  },
  created() {
    this.handleGetList();
    this.handleMinerStatisticsRewardByDay(15);
    this.handleIsNft();
  },
  methods: {
    gotoOpenSea(){
      window.open(this.data.nftInfo.url,'_blank');
    },
    async handleIsNft() {
      this.data.nftInfo = await getMinerTokenID(this.$route.params.address);
    },
    handleGetPageInfo(){
      let pageInfo = getWalletMinerPageInfo()
      this.data.page.limit = Number(pageInfo.pageSize)
      this.data.page.offset = Number(pageInfo.currentPage)
      let url = window.location.href;
      let urlArr = url.split("#");
      url = urlArr[0]+(pageInfo.type==undefined?"#report":"#"+pageInfo.type)
      history.pushState('','',url)
    },
    handleSetPageInfo(pageSize,currentPage){
      let url = window.location.href;
      let urlArr = url.split("#");
      let type = (urlArr[1] == undefined?"report":urlArr[1])
      setWalletMinerPageInfo(pageSize,currentPage,type)
    },
    timeFormat(offset){
      let date = new Date()
      let startMonth = date.getUTCMonth()+1
      if(startMonth<10){
        startMonth = "0"+startMonth
      }
      let startDate = date.getUTCDate();
      if(startDate<10){
        startDate = "0"+startDate
      }
      let endTime = date.getUTCFullYear()+"-"+startMonth+"-"+startDate+"T23:59:59.000Z";
      date = date - ((offset-1) * 24 * 60 * 60 * 1000)
      date = new Date(date);
      let endMonth = date.getUTCMonth()+1
      if(endMonth<10){
        endMonth = "0"+endMonth
      }
      let endDate = date.getUTCDate();
      if(endDate<10){
        endDate = "0"+endDate
      }
      let startTime =  date.getUTCFullYear()+"-"+endMonth+"-"+endDate+"T00:00:00.000Z";
      let time = {startTime:startTime,endTime:endTime}
      return time;
    },
    async handleMinerStatisticsRewardByDay(day) {
      //
      this.data.minerRewardTitle = this.$route.params.address + " - Last "+day+" Days"
      this.data.loadMinerBalance = true;
      let time = this.timeFormat(day);
      let rotateX = -10;
      if (day > 7) {
        rotateX = -30
      }
      const histogramData = {
        xAxisData: [],
        legendData: [],
        series: []
      }
      const data = {
        "address": this.$route.params.address,
        "startTime": time.startTime,
        "endTime": time.endTime
      }
      await statisticsRewardByDay(data).then(rsp => {
        if (rsp.code == 0) {
          const xAxisData = []
          const series = {
            name:  this.$route.params.address,
            data: [],
            type: 'line',
            emphasis: {
              focus: 'series'
            }
          }
          for (let index = 0; index < rsp.data.length; index++) {
            xAxisData.push(rsp.data[index].dataDate);
            series.data.push(rsp.data[index].reward);
          }
          //
          histogramData.xAxisData = xAxisData;
          histogramData.series.push(series)
        }
      })
      this.drawMinerHistogram(histogramData, rotateX);
      this.dataloadMinerBalance = false
    },
    drawMinerHistogram(histogramData,rotateX){
      const app = {}
      const chartDom = document.getElementById('minerHistogram')
      const myChart = echarts.init(chartDom)
      let option
      app.config = {
        rotate: 90,
        align: 'left',
        verticalAlign: 'middle',
        position: 'insideBottom',
        distance: 15,
        onChange: function () {
          const labelOption = {
            rotate: app.config.rotate,
            align: app.config.align,
            verticalAlign: app.config.verticalAlign,
            position: app.config.position,
            distance: app.config.distance
          };
          myChart.setOption({
            series: [{label: labelOption}]
          });
        }
      };
      const labelOption = {
        show: false,
        position: app.config.position,
        distance: app.config.distance,
        align: app.config.align,
        verticalAlign: app.config.verticalAlign,
        rotate: app.config.rotate,
        formatter: '{c}  {name|{a}}',
        fontSize: 16,
        rich: {
          name: {}
        }
      };
      for(let index;index<histogramData.series.length;index++){
        histogramData.series[index].type = 'line'
        histogramData.series[index].label = labelOption
        histogramData.series[index].emphasis.focus = 'series'
      }
      option = {
        title:{
          show : true,
          text : "       Reward History"
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          formatter: function (params){
            return params[0].name+"<br/>Reward:"+ Number(Number(params[0].value).toFixed(3)).toLocaleString()
          },
        },
        legend: {
          data: histogramData.legendData
        },
        toolbox: {
          show: true,
          orient: 'vertical',
          left: 'right',
          top: 'top',
          feature: {
            mark: { show: false },
            magicType: {
              show: true,
              type: ['line','bar'],
              title:{
                line:'Switch to Line Chart',
                bar:'Switch to Bar Chart'
              }
            },
            restore: { show: false }
          }
        },
        xAxis: [
          {
            type: 'category',
            axisTick: { show: true },
            data: histogramData.xAxisData,
            axisLabel: {
              interval: 0,
              rotate: rotateX
            }
          }
        ],
        yAxis: [{type: 'value'}],
        series: histogramData.series
      };
      option && myChart.setOption(option);
    },
    gotoMap(latitude,longitude){
      this.data.dialogMapVisible = true;
      this.showMap(latitude,longitude);
    },
    handleGetList(){
      this.handleGetPageInfo();
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
      this.handleSetPageInfo(this.data.page.limit,1)
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
      this.handleSetPageInfo(this.data.page.limit,this.data.page.offset)
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
      this.handleSetPageInfo(this.data.page.limit,1)
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
    handleSearchTransaction(){
      this.data.page.limit = 25
      this.data.page.offset = 1
      this.handleSetPageInfo(this.data.page.limit,this.data.page.offset)
      this.handleGetTransaction();
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
    //创建底图
    createBaseLayer(){
      const layers = [];
      const world_Street_MapLayer = new TileLayer({
        source: new XYZSource({
          attributions: 'Tiles © <a href="https://services.arcgisonline.com/arcgis/rest/services/World_Street_Map/MapServer">ArcGIS</a>',
          url: 'https://services.arcgisonline.com/arcgis/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}'
        }),
      });
      layers.push(world_Street_MapLayer)
      return layers
    },
    showMap(latitude,longitude){
      let elementById = document.getElementById("map");
      for (const childNode of elementById.childNodes) {
        childNode.remove();
      }
      elementById.innerHTML = "";
      //
      var features = [];
      let x = (longitude * 20037508.34) / 180
      let y = Math.log(Math.tan(((90 + latitude) * Math.PI) / 360)) / (Math.PI / 180)
      y = (y * 20037508.34) / 180
      const f = new Feature(new Point([x, y]));
      features.push(f);
      //
      const map = new Map({
        layers: this.createBaseLayer(),
        target: "map",
        view: new View({
          center: [x, y],
          zoom: 4,
        })
      })
      //标注点图层
      const markerSource = new VectorSource();
      markerSource.clear(true);
      markerSource.addFeatures(features);
      const vectorLayer = new VectorLayer({source: markerSource, visible: true});
      //
      map.addLayer(vectorLayer);
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
      nftInfo:undefined,
      minerRewardTitle:"",
      loadMinerBalance:false,
      query: {
        minerAddress: ''
      },
      txTypeOptions:[
        {value:1,label:"TX_Register_Miner"},
        {value:2,label:"TX_Onboard_Miner"},
        {value:3,label:"TX_Transfer_Miner"},
        {value:4,label:"TX_Terminate_Miner"},
        {value:5,label:"TX_Airdrop_Miner"},
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
      dialogMapVisible:false,
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
.closeMap {
  cursor: pointer;
}
.closeMap:hover {
  color: #72767b;
}
.showMap {
  position: absolute;
  z-index: 999;
  width: 100%;
  height: 100%;
  left: 0px;
  background-color: rgba(0,0,0,0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  &.show {
    top: 0px;
  }
  &.hide{
    top: -5000px;
  }
}
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
