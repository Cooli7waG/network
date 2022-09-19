<template>
  <el-row style="margin-top: 10px">
    <!-- 数据统计 -->
    <el-col class="card-hor-style" :lg="24">
      <div style="padding: 5px 10px;">
        <div class="card-col" style="background: -webkit-linear-gradient(left, #C4E1FF , #66B3FF);">
          <div>{{miner.total}}</div>
          <span>Total Miner</span>
        </div>
        <div class="card-col" style="background: -webkit-linear-gradient(left, #FFBD9D , #FF5809);">
          <div>{{user.earningMint}}</div>
          <span>Mining Reward</span>
        </div>
        <div class="card-col" style="background: -webkit-linear-gradient(left, #BBFFBB , #28FF28);">
          <div>{{user.balance}}</div>
          <span>AKRE Balance</span>
        </div>
      </div>
    </el-col>
    <!-- 柱状图 -->
    <el-col class="card-hor-style" :sm="24" :lg="12">
      <el-card class="box-card" style="width: 863px;height: 400px;margin: 10px 20px">
        <template #header>
          <div class="card-header">
            <span>Rewwrd Trend</span>
            <el-button class="card-operate-btn" type="text">
              <el-dropdown>
                <span class="el-dropdown-link">
                  <el-icon style="font-size: 18px"><Operation /></el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="handleStatisticsRewardByDay(7)">Last 7 days</el-dropdown-item>
                    <el-dropdown-item @click="handleStatisticsRewardByDay(15)">Last 15 days</el-dropdown-item>
                    <el-dropdown-item @click="handleStatisticsRewardByDay(30)">Last 30 days</el-dropdown-item>
                    <el-dropdown-item @click="handleStatisticsRewardByDay(90)">Last 90 days</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </el-button>
          </div>
        </template>
        <div class="text item" v-loading="loadBalance">
          <div id="histogram" style="width: 828px;height:300px;"></div>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
import {getMetaMaskLoginUserAddress} from "@/api/metamask_utils";
import {statisticsByDateTimeRange, statisticsRewardByDay} from "@/api/walletDashboard";
import {deviceList} from "@/api/miners";
import {findByAddress} from "@/api/account";
import {getTokenFixed} from "@/utils/data_format";
import {toEther} from "@/utils/utils";
import * as echarts from 'echarts';

export default {
  props: {
    msg: String
  },
  data(){
    return {
      loadBalance:false,
      loadingHistogram:false,
      miner : {
        total:0
      },
      user : {
        balance:0,
        earningMint:0
      }
    }
  },
  created() {
    this.handleStatisticsRewardByDay(15);
    this.loadFindByAddress();
  },
  methods:{
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
      console.log("startTime:"+time.startTime)
      console.log("endTime:"+time.endTime)
      const data = {
        "address": owner,
        "startTime": time.startTime,
        "endTime": time.endTime
      }
      this.loadingHistogram = true;
      statisticsRewardByDay(data).then(rsp => {
        if (rsp.code == 0) {
          let rotateX=-20;
          if(rsp.data.length > 7){
            rotateX = -40
          }
          this.drawHistogram(rsp.data,rotateX);
        } else {
          console.log("get statisticsRewardByDay error!")
        }
      })
      this.loadingHistogram = false;
    },
    handleStatisticsByDateTimeRange(){
      statisticsByDateTimeRange().then(rsp => {
        if (rsp.code == 0) {

        } else {

        }
      })
    },
    handleGetMinerCount(){
      const owner = getMetaMaskLoginUserAddress();
      if(owner == undefined || owner == null){
        return;
      }
      const params={
        offset:1,
        limit:20,
        address:owner
      }
      deviceList(params).then((result)=>{
        this.miner.total=result.data.total
      }).catch((err) =>{
        console.log(err);
      });
    },
    loadFindByAddress(){
      const userAddress = getMetaMaskLoginUserAddress();
      if(userAddress == undefined || userAddress == null){
        return;
      }
      this.loadBalance = true;
      findByAddress(userAddress).then((result)=>{
        if(result.code == 0){
          this.user.earningMint = result.data.earningMint.toLocaleString();
          const fixed = getTokenFixed(result.data.balance);
          this.user.balance = toEther(result.data.balance,fixed);
        }
        this.handleGetMinerCount();
        this.loadBalance = false;
      }).catch((err) =>{
        console.log(err);
        this.loadBalance = false;
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
        formatter: '{c}  {name|{a}}',
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
          }
        },
        legend: {
          data: ['Reward']
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
              }},
            restore: { show: false }
          }
        },
        xAxis: [
          {
            type: 'category',
            axisTick: { show: true },
            data: labels,
            axisLabel: {
              interval: 0,
              rotate: rotateX
            }
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: [
          {
            name: 'reward',
            type: 'bar',
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
    },
  }
}
</script>
<style lang="scss" scoped>
.card-col{
  margin: 10px;
  width: 250px;
  float: left;
  padding-left: 25px;
  padding-top: 35px;
  padding-bottom: 35px;
  border-radius: 20px;
}
.card-col span{
  font-size: 25px;
}
.card-col div {
  font-size: 28px;
  font-weight: bold;
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
