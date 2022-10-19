<template>
  <el-row style="margin-top: 10px">
    <!-- 数据统计 -->
    <el-col class="card-hor-style" :lg="24">
      <div style="padding: 5px 10px;">
        <div class="card-col" style="background: -webkit-linear-gradient(left, #C4E1FF , #66B3FF);">
          <div class="numericalView">{{miner.total}}</div>
          <span>{{ $t('walletDashboard.TotalMiners') }}</span>
        </div>
        <div class="card-col" style="background: -webkit-linear-gradient(left, #FFBD9D , #FF5809);">
          <div class="numericalView">{{Number(Number(user.earningMint).toFixed(3)).toLocaleString()}}</div>
          <span>{{ $t('walletDashboard.MiningReward') }}</span>
        </div>
        <div class="card-col" style="background: -webkit-linear-gradient(left, #BBFFBB , #28FF28);">
          <div class="numericalView">{{user.balance}}</div>
          <span>{{ $t('walletDashboard.AKREBalance') }}</span>
        </div>
      </div>
    </el-col>
    <!-- 柱状图 -->
    <el-col class="card-hor-style" :sm="24" :lg="12">
      <el-card class="box-card" style="height: 400px;margin: 10px 20px">
        <template #header>
          <div class="card-header">
            <span>{{RewardTrendTitle}}</span>
            <el-button class="card-operate-btn" type="text">
              <el-dropdown>
                <span class="el-dropdown-link">
                  <el-icon style="font-size: 18px"><Operation /></el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="handleStatisticsRewardByDay(7)">{{ $t('walletDashboard.Last7days') }}</el-dropdown-item>
                    <el-dropdown-item @click="handleStatisticsRewardByDay(15)">{{ $t('walletDashboard.Last15days') }}</el-dropdown-item>
                    <el-dropdown-item @click="handleStatisticsRewardByDay(30)">{{ $t('walletDashboard.Last30days') }}</el-dropdown-item>
                    <el-dropdown-item @click="handleStatisticsRewardByDay(90)">{{ $t('walletDashboard.Last90days') }}</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </el-button>
          </div>
        </template>
        <div class="text item" v-loading="loadBalance">
          <div id="histogram" style="height:300px;"></div>
        </div>
      </el-card>
    </el-col>
    <!-- 饼图 -->
    <el-col class="card-hor-style" :sm="24" :lg="12">
      <el-card class="box-card" style="height: 400px;margin: 10px 20px">
        <template #header>
          <div class="card-header">
            <span>{{RewardPercentageTitle}}</span>
            <el-button class="card-operate-btn" type="text">
              <el-dropdown>
                <span class="el-dropdown-link">
                  <el-icon style="font-size: 18px"><Operation /></el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="handleMinerPieStatisticsRewardByDay(7)">{{ $t('walletDashboard.Last7days') }}</el-dropdown-item>
                    <el-dropdown-item @click="handleMinerPieStatisticsRewardByDay(15)">{{ $t('walletDashboard.Last15days') }}</el-dropdown-item>
                    <el-dropdown-item @click="handleMinerPieStatisticsRewardByDay(30)">{{ $t('walletDashboard.Last30days') }}</el-dropdown-item>
                    <el-dropdown-item @click="handleMinerPieStatisticsRewardByDay(90)">{{ $t('walletDashboard.Last90days') }}</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </el-button>
          </div>
        </template>
        <div class="text item" v-loading="loadPie" :element-loading-text="loadPieText">
          <div id="minerPie" style="height:300px;"></div>
        </div>
      </el-card>
    </el-col>
    <el-dialog v-model="dialogLineVisible" title="" :show-close=false>
      <el-card class="box-card" style="height: 400px;margin-top: -60px;margin-left: -20px;margin-bottom: -30px;margin-right: -20px">
        <template #header>
          <div class="card-header">
            <span>{{minerRewardTitle}}</span>
            <el-button class="card-operate-btn" type="text">
              <el-dropdown>
                <span class="el-dropdown-link" style="margin-top: -10px">
                  <el-icon style="font-size: 18px"><Operation /></el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="handleMinerStatisticsRewardByDay(7)">Last 7 days</el-dropdown-item>
                    <el-dropdown-item @click="handleMinerStatisticsRewardByDay(15)">Last 15 days</el-dropdown-item>
                    <el-dropdown-item @click="handleMinerStatisticsRewardByDay(30)">Last 30 days</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
              <el-icon class="closeDialogLineVisible" @click="dialogLineVisible = false"><Close /></el-icon>
            </el-button>
          </div>
        </template>
        <div class="text item" v-loading="loadMinerBalance">
          <div id="minerHistogram" style="height:300px;"></div>
        </div>
      </el-card>
    </el-dialog>
  </el-row>
</template>

<script>
import {getMetaMaskLoginUserAddress} from "@/api/metamask_utils";
import {statisticsRewardByDay,statisticsRewardsByOwnerAddress} from "@/api/walletDashboard";
import {deviceList} from "@/api/miners";
import {accountInfo} from "@/api/account";
import {getAddress} from "@/utils/data_format";
import * as echarts from 'echarts';
import {getGAKREBalance} from "@/api/browserUtils";

export default {
  name:"walletDashboard",
  props: {
    msg: String
  },
  data(){
    return {
      chartType:'bar',
      loadPie:false,
      loadPieText:"Loading...",
      loadBalance:false,
      loadMinerBalance:false,
      loadingHistogram:false,
      RewardTrendTitle:"Reward History - Last 15 Days",
      dialogLineVisible:false,
      RewardPercentageTitle:"Miner Reward(%) - Last 15 Days",
      minerRewardTitle:" Reward Percentage - Last 15 Days",
      miner : {
        address:undefined,
        total:0,
        list:undefined
      },
      user : {
        balance:'loading...',
        earningMint:0
      },
      timerInterval:undefined
    }
  },
  created() {
    this.loadFindByAddress();
    this.handleStatisticsRewardByDay(15);
    this.handleMinerPieStatisticsRewardByDay(15);
    this.getBalance();
    this.timer();
  },
  unmounted(){
    window.clearInterval(this.timerInterval)
  },
  methods:{
    timer() {
      this.timerInterval = window.setInterval(() => {
        this.getBalance();
      }, 5000)
    },
    getBalance(){
      let balance = getGAKREBalance()
      let oldBalance = this.user.balance;
      let newBalance = Number(Number(balance).toFixed(3)).toLocaleString();
      if(oldBalance != newBalance){
        this.user.balance = newBalance;
        this.loadFindByAddress();
      }
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
    handleStatisticsRewardByDay(day){
      const owner = getMetaMaskLoginUserAddress();
      let time = this.timeFormat(day);
      const data = {
        "address": owner,
        "startTime": time.startTime,
        "endTime": time.endTime
      }
      this.loadBalance = true
      statisticsRewardByDay(data).then(rsp => {
        if (rsp.code == 0) {
          let rotateX=-20;
          if(rsp.data.length > 7){
            rotateX = -40
          }
          let RewardHistory = this.$t("walletDashboard.RewardHistory")
          this.RewardTrendTitle = RewardHistory.replace('7',day)
          this.drawHistogram(rsp.data,rotateX);
        } else {
          console.log("get statisticsRewardByDay error!")
        }
      })
      this.loadBalance = false
    },
    async handleMinerStatisticsRewardByDay(day) {
      this.minerRewardTitle = this.miner.address + " - Last "+day+" Days"
      this.loadMinerBalance = true;
      let time = this.timeFormat(day);
      let rotateX = -20;
      if (day > 7) {
        rotateX = -40
      }
      const histogramData = {
        xAxisData: [],
        legendData: [],
        series: []
      }
      //
      const data = {
        "address": this.miner.address,
        "startTime": time.startTime,
        "endTime": time.endTime
      }
      await statisticsRewardByDay(data).then(rsp => {
        if (rsp.code == 0) {
          const xAxisData = []
          const series = {
            name: this.miner.address,
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
      this.loadMinerBalance = false
    },
    async handleGetMinerCount(limit){
      const owner = getMetaMaskLoginUserAddress();
      if(owner == undefined || owner == null){
        return;
      }
      const params={
        offset:1,
        limit:limit,
        address:owner
      }
      deviceList(params).then((result)=>{
        this.miner.total = result.data.total
        this.miner.list = result.data.items
      }).catch((err) =>{
        console.log(err);
      });
    },
    async loadFindByAddress() {
      const userAddress = getMetaMaskLoginUserAddress();
      if (userAddress == undefined || userAddress == null) {
        return;
      }
      accountInfo(userAddress).then((result) => {
        if (result.code == 0) {
          this.user.earningMint = result.data.earningMint.toLocaleString();
        }
        this.handleGetMinerCount(10);
      }).catch((err) => {
        console.log(err);
      });
    },
    drawHistogram(data,rotateX){
      const labels = [];
      const values = [];
      for (let index=0;index<data.length;index++){
        labels[index] = data[index].dataDate
        values[index] = data[index].reward
      }
      const app = {}
      const chartDom = document.getElementById('histogram')
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
        formatter: '{c}',
        fontSize: 16,
        rich: {
          name: {}
        }
      };
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
          data: ['Reward'],
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
            axisTick: { show: false },
            data: labels,
            axisLabel: {
              interval: 0,
              rotate: rotateX
            },
          }
        ],
        yAxis: [{
          type: 'value'
        }],
        series: [
          {
            name: 'reward',
            type: this.chartType,
            barGap: 0,
            label: labelOption,
            emphasis: {
              focus: 'series'
            },
            data: values
          }
        ]
      };
      option && myChart.setOption(option);
      myChart.on('magictypechanged', (params) => {
        if (params.currentType == 'line') {
          this.chartType = 'line'
        } else if (params.currentType == 'bar') {
          this.chartType = 'bar'
        }
      })
    },
    drawMinerHistogram(histogramData,rotateX){
      //
      console.log("histogramData:"+JSON.stringify(histogramData))
      //
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
              show: false,
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
    handleMinerPieStatisticsRewardByDay(day) {
      const owner = getMetaMaskLoginUserAddress();
      if (owner == undefined || owner == null) {
        return;
      }
      let time = this.timeFormat(day);
      const params = {
        "ownerAddress": owner,
        "startTime": time.startTime,
        "endTime": time.endTime
      }
      statisticsRewardsByOwnerAddress(params).then(result => {
        let RewardPercentage = this.$t("walletDashboard.RewardPercentage")
        this.RewardPercentageTitle = RewardPercentage.replace('7',day)
        const pieData = {
          legendData: [],
          series: []
        }
        for (let item in result.data) {
          pieData.legendData.push(result.data[item].address)
          const series = {
            name: result.data[item].address,
            value: result.data[item].reward
          }
          pieData.series.push(series)
        }
        this.drawPie(pieData);
        this.loadPie = false;
      })
    },
    drawPie(dataPie){
      const chartDom = document.getElementById('minerPie');
      const myChart = echarts.init(chartDom);
      let option;
      option = {
        title: {
          text: '',
          subtext: '',
          left: 'left'
        },
        tooltip: {
          trigger: 'item',
          textStyle: {
            fontSize: 8
          },
          formatter: function (params){
            let value = Number(params.value).toFixed(3).toLocaleString();
            return params.seriesName+' Address：'+params.name+'<br/>Reward: '+value+' ('+params.percent+'%)'
          }
        },
        legend: {
          type: 'scroll',
          orient: 'vertical',
          right: 10,
          top: 20,
          bottom: 20,
          data: dataPie.legendData,
          formatter: function (params){
            return getAddress(params)
          }
        },
        series: [
          {
            name: 'Miner',
            type: 'pie',
            radius: '80%',
            center: ['30%', '50%'],
            data: dataPie.series,
            labelLine: {
              show: true
            },
            label: {
              show: true,
              position: 'outside',
              formatter: function (params){
                return params.percent+"%"
              }
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };
      option && myChart.setOption(option);
      myChart.on('click', params =>{
        this.miner.address = params.name;
        this.dialogLineVisible = true;
        this.handleMinerStatisticsRewardByDay(15)
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.numericalView{
  font-weight: bold;
  font-size: 26px;
}
.closeDialogLineVisible :hover{
  color: #545c64;
}
.closeDialogLineVisible{
  font-size: 18px;
  color: #72767b;
  margin-left: 10px;
  margin-top: -10px
}
.card-col{
  margin: 10px;
  width: 260px;
  float: left;
  padding-left: 25px;
  padding-top: 35px;
  padding-bottom: 35px;
  border-radius: 20px;
}
.card-col span{
  font-size: 25px;
}
.card-operate-btn{
  float: right;
  padding: 3px 0px;
  font-size: 20px;
}
.card-hor-style{
  padding: 5px 10px;
}
</style>
