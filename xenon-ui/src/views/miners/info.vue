<template>
  <el-breadcrumb style="margin-bottom: 20px;">
    <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: '/miners' }">Miners列表</el-breadcrumb-item>
    <el-breadcrumb-item >Miner详情</el-breadcrumb-item>
  </el-breadcrumb>
  <div v-if="data.device">
    <el-row :gutter="20">
      <el-col :span="4">Address:</el-col>
      <el-col :span="16">{{data.device.address}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">Miner 类型:</el-col>
      <el-col :span="16">{{data.device.minerType?Constant.MinerType[data.device.minerType]:''}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">设备制造商名称:</el-col>
      <el-col :span="16">{{data.device.maker}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">位置类型:</el-col>
      <el-col :span="16">{{data.device.locationType}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">经纬度:</el-col>
      <el-col :span="16">{{data.device.latitude+","+data.device.longitude}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">H3Index:</el-col>
      <el-col :span="16">{{data.device.h3index}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">是否终止:</el-col>
      <el-col :span="16">{{data.device.terminate}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">创建时间:</el-col>
      <el-col :span="16">{{formatDate(data.device.createTime, "yyyy-MM-dd hh:mm:ss")}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">Onboard入网块高:</el-col>
      <el-col :span="16">{{data.device.deviceDetial.startHeight}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">电力来源:</el-col>
      <el-col :span="16">{{data.device.deviceDetial.energy}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">设备能力:</el-col>
      <el-col :span="16">{{data.device.deviceDetial.capabilities}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">设备型号:</el-col>
      <el-col :span="16">{{data.device.deviceDetial.deviceModel}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">设备序列号:</el-col>
      <el-col :span="16">{{data.device.deviceDetial.deviceSerialNum}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">发电电压:</el-col>
      <el-col :span="16">{{data.device.deviceDetial.sGVoltage}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">发电电流:</el-col>
      <el-col :span="16">{{data.device.deviceDetial.sGCurrent}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">发电功率（低）:</el-col>
      <el-col :span="16">{{data.device.deviceDetial.sGPowerLow}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">发电功率（高）:</el-col>
      <el-col :span="16">{{data.device.deviceDetial.sGPowerHigh}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">蓄电池电压:</el-col>
      <el-col :span="16">{{data.device.deviceDetial.sBVoltage}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">蓄电池电流:</el-col>
      <el-col :span="16">{{data.device.deviceDetial.sBCurrent}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">蓄电池功率（低）:</el-col>
      <el-col :span="16">{{data.device.deviceDetial.sBPowerLow}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">蓄电池功率（高）:</el-col>
      <el-col :span="16">{{data.device.deviceDetial.sBPowerHigh}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">负载电压:</el-col>
      <el-col :span="16">{{data.device.deviceDetial.sLVoltage}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">负载电流:</el-col>
      <el-col :span="16">{{data.device.deviceDetial.sLCurrent}}</el-col>
    </el-row>


    <el-row :gutter="20">
      <el-col :span="4">负载功率（低）:</el-col>
      <el-col :span="16">{{data.device.deviceDetial.sLPowerLow}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">负载功率（高）:</el-col>
      <el-col :span="16">{{data.device.deviceDetial.sLPowerHigh}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">总累计充电量:</el-col>
      <el-col :span="16">{{data.device.deviceDetial.totalChargeVol}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">总累计用电量:</el-col>
      <el-col :span="16">{{data.device.deviceDetial.totalUsageVol}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">数据最新更新时间:</el-col>
      <el-col :span="16">{{data.device.deviceDetial.updateTime?formatDate(data.device.deviceDetial.updateTime, "yyyy-MM-dd hh:mm:ss"):''}}</el-col>
    </el-row>
  </div>
  <div v-if="data.noData">
    <el-col :sm="12" :lg="6">
      <el-result
          icon="warning"
          title="没有找到数据"
      >
        <template #extra>
          <router-link to="/miners">返回</router-link>
        </template>
      </el-result>
    </el-col>
  </div>
</template>

<script>
import Constant from '@/utils/constant.js'
import {queryByMiner} from '@/api/miners.js'
import {onMounted, reactive} from "vue";
import { useRoute  } from 'vue-router'
import {formatDate} from "@/utils/data_format";
export default {
  components: {},

  props: {
    msg: String
  },
  computed: {
    formatDate() {
      return formatDate
    },
    Constant() {
      return Constant
    }
  },
  setup(){

    const route = useRoute()
    const data = reactive({
      query:{
        minerAddress:''
      },
      device:'',
      noData:false
    })
    const loadQueryByMiner=()=>{
      queryByMiner(data.query.minerAddress).then((result)=>{
        data.device=result.data
        if(result.data==null){
          data.noData=true
        }
      }).catch((err) =>{
        console.log(err);
      });
    }

    onMounted(() => {
      console.log("onMounted")
      data.query.minerAddress=route.params.address;
      loadQueryByMiner()
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
