<template>
  <div>
    <el-row>
      <el-col :span="12">
        <el-breadcrumb style="margin-bottom: 20px;">
          <el-breadcrumb-item :to="{ path: '/wallet/miners' }">My Miners</el-breadcrumb-item>
          <el-breadcrumb-item>{{ $t('minerinfo.path.info') }}</el-breadcrumb-item>
        </el-breadcrumb>
      </el-col>
      <el-col :span="12" style="padding-left: 50px">
        <span>Reward Trend</span>
      </el-col>
    </el-row>
  </div>
  <div v-if="data.device">
    <div>
      <el-row>
        <el-col :span="12">
          <el-row :gutter="20">
            <el-col :xs="12" :sm="10" :md="10" :lg="8" :xl="6">{{ $t('minerinfo.info.address') }}:</el-col>
            <el-col :xs="12" :sm="14" :md="14" :lg="14" :xl="18">{{ data.device.address }}</el-col>
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
            <el-col :xs="12" :sm="14" :md="14" :lg="14" :xl="18">{{ formatToken(data.device.earningMint) }}</el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :xs="12" :sm="10" :md="10" :lg="8" :xl="6">{{ $t('minerinfo.info.earningService') }}:</el-col>
            <el-col :xs="12" :sm="14" :md="14" :lg="14" :xl="18">{{ formatToken(data.device.earningService) }}</el-col>
          </el-row>
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
        <el-col :span="12" style="padding-left: 50px">
          <div id="minerHistogram" style="height:224px;"></div>
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

export default {
  components: {},
  props: {
    msg: String
  },
  created() {
    this.handleGetList();
    this.handleMinerStatisticsRewardByDay(15);
  },
  methods: {
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
