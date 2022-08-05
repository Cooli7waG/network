<template>
  <el-row :gutter="24">
    <el-col :span="12">
      <el-card class="box-card" >
        <div  class="text item">
          <div class="lable">{{$t('dashboard.miners')}}</div>
          <div class="content">{{ data.minerStatistics.miners }}</div>
        </div>
        <div  class="text item">
          <div class="lable">{{$t('dashboard.totalPowerLow')}}</div>
          <div class="content">{{ data.minerStatistics.totalPowerLow }}</div>
        </div>
        <div  class="text item">
          <div class="lable">{{$t('dashboard.totalChargeVol')}}</div>
          <div class="content">{{ data.minerStatistics.totalChargeVol }}</div>
        </div>

      </el-card>
    </el-col>
    <el-col :span="12">
      <el-card class="box-card">
        <div  class="text item">
          <div class="lable">{{$t('dashboard.uSDBmtMarketPrice')}}</div>
          <div class="content">{{ data.blockchainstats.uSDBmtMarketPrice }}</div>
        </div>

        <div  class="text item">
          <div class="lable">{{$t('dashboard.totalBMTMarket')}}</div>
          <div class="content">{{ data.blockchainstats.totalBMTMarket }}</div>
        </div>
        <div  class="text item">
          <div class="lable">{{$t('dashboard.tokenSupply')}}</div>
          <div class="content">{{ data.blockchainstats.tokenSupply }}</div>
        </div>
      </el-card>
    </el-col>
  </el-row>
  <el-row>
    <el-col>

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
        uSDBmtMarketPrice: 0,
        totalBMTMarket: 0,
        tokenSupply: 0
      },
      minerStatistics:{
        miners: 0,
        totalPowerLow: 0.0,
        totalChargeVol: 0.0
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
