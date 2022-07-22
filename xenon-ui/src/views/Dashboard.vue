<template>
  <el-row :gutter="24">
    <el-col :span="8">
      <el-card class="box-card" >
        <div  class="text item">
          <div class="lable">{{$t('dashboard.virtualMiners')}}</div>
          <div class="content">{{ data.blockchainstats.virtualMiners }}</div>
        </div>
        <div  class="text item">
          <div class="lable">{{$t('dashboard.lightSolarMiners')}}</div>
          <div class="content">{{ data.blockchainstats.lightSolarMiners }}</div>
        </div>
        <div  class="text item">
          <div class="lable">{{$t('dashboard.blocks')}}</div>
          <div class="content">{{ data.blockchainstats.blocks }}</div>
        </div>

      </el-card>
    </el-col>
    <el-col :span="8">
      <el-card class="box-card">
        <div  class="text item">
          <div class="lable">{{$t('dashboard.poggChallenges')}}</div>
          <div class="content">{{ data.blockchainstats.poggChallenges }}</div>
        </div>

        <div  class="text item">
          <div class="lable">{{$t('dashboard.transactions')}}</div>
          <div class="content">{{ data.blockchainstats.transactions }}</div>
        </div>
        <div  class="text item">
          <div class="lable">{{$t('dashboard.tokenSupply')}}</div>
          <div class="content">{{ data.blockchainstats.tokenSupply }}</div>
        </div>
      </el-card>
    </el-col>
    <el-col :span="8">
      <el-card class="box-card">
        <div  class="text item" style="float:left">
          <div class="lable">{{$t('dashboard.totalPowerLow')}}</div>
          <div class="content">{{ data.minerStatistics.totalPowerLow }}</div>
        </div>
        <div  class="text item" style="text-align: right;">
          <div class="lable">{{$t('dashboard.totalPowerHigh')}}</div>
          <div class="content">{{ data.minerStatistics.totalPowerHigh }}</div>
        </div>
        <div  class="text item" style="float:left">
          <div class="lable">{{$t('dashboard.totalChargeVol')}}</div>
          <div class="content">{{ data.minerStatistics.totalChargeVol }}</div>
        </div>
        <div  class="text item" style="text-align: right;">
          <div class="lable">{{$t('dashboard.totalUsageVol')}}</div>
          <div class="content">{{ data.minerStatistics.totalUsageVol }}</div>
        </div>
        <div  class="text item" >
          <div class="lable">&nbsp;</div>
          <div class="content">&nbsp;</div>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
import {getBlockchainstats,getMinerStatistics} from '@/api/statistics.js'
import {onMounted, reactive} from "vue";
import {toEther} from '@/utils/utils.js'
export default {
  name: 'Dashboard',
  props: {
    msg: String
  },
  setup(){

    const data = reactive({
      blockchainstats:{
        blocks: 0,
        lightSolarMiners: 0,
        poggChallenges: 0,
        tokenSupply: 0,
        transactions: 0,
        virtualMiners: 0
      },
      minerStatistics:{
        totalPowerLow: 0.0,
        totalPowerHigh: 0.0,
        totalChargeVol: 0.0,
        totalUsageVol: 0
      }
    })

    const loadBlockchainstats=()=>{
      getBlockchainstats().then((result)=>{
        console.log(result)
        data.blockchainstats=result.data;

        data.blockchainstats.tokenSupply=toEther(data.blockchainstats.tokenSupply,8)
        console.log(data.blockchainstats)
      }).catch((err) =>{
        console.log(err);
      });
    }

    const loadMinerStatistics=()=>{
      getMinerStatistics().then((result)=>{
        console.log(result)
        data.minerStatistics=result.data;
      }).catch((err) =>{
        console.log(err);
      });
    }

    onMounted(() => {
      console.log("onMounted")
      loadBlockchainstats()
      loadMinerStatistics()
    })



    return {
      data
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
</style>
