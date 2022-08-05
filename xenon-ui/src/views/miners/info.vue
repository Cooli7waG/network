<template>
  <el-breadcrumb style="margin-bottom: 20px;">
    <el-breadcrumb-item :to="{ path: '/' }">{{$t('minerinfo.path.home')}}</el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: '/miners' }">{{$t('minerinfo.path.miners')}}</el-breadcrumb-item>
    <el-breadcrumb-item >{{$t('minerinfo.path.info')}}</el-breadcrumb-item>
  </el-breadcrumb>
  <div v-if="data.device">
    <el-row :gutter="20">
      <el-col :span="4">{{$t('minerinfo.info.address')}}:</el-col>
      <el-col :span="16">{{data.device.address}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">{{$t('minerinfo.info.minerType')}}:</el-col>
      <el-col :span="16">{{data.device.minerType?Constant.MinerType[data.device.minerType]:''}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">{{$t('minerinfo.info.ownerAddress')}}:</el-col>
      <el-col :span="16">{{data.device.ownerAddress}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">{{$t('minerinfo.info.maker')}}:</el-col>
      <el-col :span="16">{{data.device.maker}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">{{$t('minerinfo.info.locationType')}}:</el-col>
      <el-col :span="16">{{data.device.locationType}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">{{$t('minerinfo.info.latlog')}}:</el-col>
      <el-col :span="16">{{data.device.latitude+","+data.device.longitude}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">{{$t('minerinfo.info.h3index')}}:</el-col>
      <el-col :span="16">{{data.device.h3index}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">{{$t('minerinfo.info.terminate')}}:</el-col>
      <el-col :span="16">{{data.device.terminate}}</el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4">{{$t('minerinfo.info.createTime')}}:</el-col>
      <el-col :span="16">{{formatDate(data.device.createTime, "yyyy-MM-dd hh:mm:ss")}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">{{$t('minerinfo.info.earningMint')}}:</el-col>
      <el-col :span="16">{{data.device.earningMint}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">{{$t('minerinfo.info.earningService')}}:</el-col>
      <el-col :span="16">{{data.device.earningService}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">{{$t('minerinfo.info.power')}}:</el-col>
      <el-col :span="16">{{data.device.power}}</el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="4">{{$t('minerinfo.info.totalEnergyGeneration')}}:</el-col>
      <el-col :span="16">{{data.device.totalEnergyGeneration}}</el-col>
    </el-row>




  </div>
  <div v-if="data.noData">
    <el-col :sm="12" :lg="6">
      <el-result
          icon="warning"
          :title="$t('minerinfo.msg.noData')"
      >
        <template #extra>
          <router-link to="/miners">{{$t('common.button.back')}}</router-link>
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
