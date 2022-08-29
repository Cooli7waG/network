<template>
  <el-breadcrumb >
      <el-breadcrumb-item :to="{ path: '/' }">{{$t('accountinfo.path.home')}}</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/accounts' }">{{$t('accountinfo.path.accounts')}}</el-breadcrumb-item>
      <el-breadcrumb-item >{{$t('accountinfo.path.info')}}</el-breadcrumb-item>
  </el-breadcrumb>
  <div v-if="account" v-loading="loading" style="margin-top: 10px">
    <el-row :gutter="20" class="info-box">
      <el-col :span="4">{{$t('accountinfo.info.address')}}:</el-col>
      <el-col :span="16">{{account.address}}</el-col>
    </el-row>
    <el-row :gutter="20" class="info-box">
      <el-col :span="4">{{$t('accountinfo.info.accountType')}}:</el-col>
      <el-col :span="16">{{accountType(account.accountType)}}</el-col>
    </el-row>
    <el-row :gutter="20" class="info-box">
      <el-col :span="4">{{$t('accountinfo.info.earningMint')}}:</el-col>
      <el-col :span="16">{{account.earningMint}}</el-col>
    </el-row>
    <el-row :gutter="20" class="info-box">
      <el-col :span="4">{{$t('accountinfo.info.earningService')}}:</el-col>
      <el-col :span="16">{{account.earningService}}</el-col>
    </el-row>
    <el-row :gutter="20" class="info-box">
      <el-col :span="4">{{$t('accountinfo.info.amountMiner')}}:</el-col>
      <el-col :span="16">{{account.amountMiner}}</el-col>
    </el-row>
    <el-row :gutter="20" class="info-box">
      <el-col :span="4">{{$t('accountinfo.info.balance')}}:</el-col>
      <el-col :span="16">{{account.balance}}</el-col>
    </el-row>
    <el-row :gutter="20" class="info-box">
      <el-col :span="4">{{$t('accountinfo.info.createTime')}}:</el-col>
      <el-col :span="16">{{formatDate(account.createTime, "yyyy-MM-dd hh:mm:ss")}}</el-col>
    </el-row>
  </div>
  <el-tabs v-show="isShow" v-model="activeName" style="margin-top: 15px" type="border-card">
    <el-tab-pane label="Miners" name="miners">
      <el-pagination
          v-model:currentPage="page.offset"
          v-model:page-size="page.limit"
          :hide-on-single-page="page.hidePage"
          :page-sizes="[10, 25, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="page.total"
          @size-change="pageSizeChange"
          @current-change="pageCurrentChange"
          style="margin-top: 5px"
      />
      <el-table v-loading="minerLoading" :data="minerData" border style="width: 100%;margin-top: 5px">
        <el-table-column prop="address" label="Miner Address" width="220" :show-overflow-tooltip=true>
          <template v-slot="scope">
            <router-link :to="'/miner/'+scope.row.address">{{scope.row.address}}</router-link>
          </template>
        </el-table-column>
        <el-table-column prop="minerType" label="Miner Type">
          <template v-slot="scope">
            {{formatMinerType(scope.row.minerType)}}
          </template>
        </el-table-column>
        <el-table-column prop="maker" label="Miner Maker"/>
        <el-table-column prop="energy" label="Energy(kWh)">
          <template v-slot="scope">
            {{formatElectricity(scope.row.energy)}}
          </template>
        </el-table-column>
        <el-table-column prop="capabilities" label="Capabilities"/>
        <el-table-column prop="power" :label="$t('miners.table.power')">
          <template v-slot="scope">
            {{formatPower(scope.row.power)}}
          </template>
        </el-table-column>
        <el-table-column prop="totalEnergyGeneration" label="Total Energy Generation"/>
        <el-table-column prop="location" label="Location" width="180">
          <template v-slot="scope">
            {{formatLocation(scope.row.locationType,scope.row.latitude,scope.row.longitude)}}
          </template>
        </el-table-column>
        <el-table-column prop="timestamp" label="Timestamp">
          <template v-slot="scope">{{formatDate(scope.row.createTime, "yyyy-MM-dd hh:mm:ss")}}</template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
  </el-tabs>
  <div v-if="noData">
    <el-col :sm="12" :lg="6">
      <el-result icon="warning" :title="$t('accountinfo.msg.noData')">
        <template #extra>
          <router-link to="/accounts">{{$t('common.button.back')}}</router-link>
        </template>
      </el-result>
    </el-col>
  </div>
</template>

<script>
import {findByAddress} from '@/api/account.js'
import {formatDate,formatPower,formatElectricity,formatLocation} from "@/utils/data_format";
import {toEther} from '@/utils/utils.js'
import {getMinersByOwnerAddress} from "@/api/miners";
import Constant from '@/utils/constant.js'
export default {
  data(){
    return {
      page:{
        hidePage:true,
        address:null,
        offset:1,
        limit:25,
        total:0,
        tag:1
      },
      isShow:false,
      minerLoading:true,
      minerData:undefined,
      activeName: 'miners',
      account:'',
      noData:false,
      loading:true
    }
  },
  methods:{
    pageSizeChange(pageSize) {
      this.page.limit = pageSize
      this.minerLoading = true;
      this.handleGetReport();
    },
    pageCurrentChange(currentPage) {
      this.page.offset = currentPage
      this.minerLoading = true;
      this.handleGetReport();
    },
    handleGetMiners(){
      getMinersByOwnerAddress(this.page).then(rsp => {
        this.page.total = rsp.data.total;
        this.minerData = rsp.data.items;
        this.minerLoading = false;
      });
    },
    loadFindByAddress(){
      this.page.address = this.$route.params.address;
      findByAddress(this.page.address).then((result)=>{
        this.account=result.data
        if(result.data==null){
          this.noData=true
        }else{
          this.account.balance=toEther(this.account.balance,8)
          this.isShow = true;
          this.handleGetMiners();
        }
        this.loading = false
      }).catch((err) =>{
        this.loading = false
        console.log(err);
      });
    },
    formatDate(value,format){
      return formatDate(value,format)
    },
    formatPower(value){
      return formatPower(value)
    },
    formatElectricity(value){
      return formatElectricity(value)
    },
    accountType(accountType){
      return Constant.AccountType[accountType];
    },
    formatMinerType(minerType){
      return Constant.MinerType[minerType];
    },
    formatLocation(locationType,latitude,longitude){
      return formatLocation(locationType,latitude,longitude)
    }
  },
  created() {
    this.loadFindByAddress();
  }
}
</script>
<style lang="scss" scoped>
.info-box {
  padding: 5px 0px;
}
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
