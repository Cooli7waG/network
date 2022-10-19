<template>
  <el-breadcrumb style="margin-bottom: 20px;">
    <el-breadcrumb-item :to="{ path: '/wallet/txs' }">{{ $t('txinfo.path.txs') }}</el-breadcrumb-item>
    <el-breadcrumb-item>{{ $t('txinfo.path.info') }}</el-breadcrumb-item>
  </el-breadcrumb>
  <div v-if="data.transaction">
    <el-row class="info-box" :gutter="20">
      <el-col :span="4">{{ $t('txinfo.info.hash') }}:</el-col>
      <el-col :span="16">{{ data.transaction.hash }}</el-col>
    </el-row>
    <el-row :gutter="20" class="info-box">
      <el-col :span="4">{{ $t('txinfo.info.status') }}:</el-col>
      <el-col :span="16">
        <el-alert v-if="data.transaction.status==1" title="Success" type="success" show-icon :closable="false" style="width: 100px;height: 30px;"/>
        <el-alert v-else title="Failed" type="error" show-icon :closable="false" style="width: 100px;height: 30px"/>
      </el-col>
    </el-row>
    <el-row :gutter="20" class="info-box">
      <el-col :span="4">{{ $t('txinfo.info.height') }}:</el-col>
      <el-col :span="16">
        <router-link :to="{name:'Transaction',params:{height:data.transaction.height}}">{{ Number(data.transaction.height).toLocaleString() }}
        </router-link>
      </el-col>
    </el-row>
    <el-row :gutter="20" class="info-box">
      <el-col :span="4">{{ $t('txinfo.info.txType') }}:</el-col>
      <el-col :span="16">{{ data.transaction.txType ? Constant.TXType[data.transaction.txType] : '' }}</el-col>
    </el-row>
    <el-row :gutter="20" class="info-box">
      <el-col :span="4">{{ $t('txinfo.info.txTime') }}:</el-col>
      <el-col :span="16">{{ formatDate(data.transaction.createTime, "yyyy-MM-dd hh:mm:ss") }}</el-col>
    </el-row>
    <el-row :gutter="20" class="info-box">
      <el-col :span="4">{{ $t('txinfo.info.txData') }}:</el-col>
      <el-col :span="16">
        <!--<json-viewer :value="JSON.parse(data.transaction.data)" :copyable=false :boxed=true :expand-depth=10 />-->
        <!--<pre style="font-family: Helvetica,Arial,sans-serif;">{{JSON.stringify(JSON.parse(data.transaction.data), null, 4)}}</pre>-->
        <pre>{{JSON.stringify(JSON.parse(data.transaction.data), null, 4)}}</pre>
      </el-col>
    </el-row>
  </div>
  <div v-if="data.noData">
    <el-col :sm="12" :lg="6">
      <el-result
          icon="warning"
          :title="$t('txinfo.msg.noData')"
      >
        <template #extra>
          <router-link to="/wallet/txs">{{ $t('common.button.back') }}</router-link>
        </template>
      </el-result>
    </el-col>
  </div>
</template>

<script>
import Constant from '@/utils/constant.js'
import {findByHash} from '@/api/transaction.js'
import {onMounted, reactive} from "vue";
import {useRoute} from 'vue-router'
import {formatDate} from "@/utils/data_format";

export default {
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
  setup() {

    const route = useRoute()
    const data = reactive({
      query: {
        txHash: ''
      },
      transaction: '',
      noData: false
    })
    const loadTransactionInfo = () => {
      findByHash(data.query.txHash).then((result) => {
        data.transaction = result.data
        if (result.data == null) {
          data.noData = true
        }
        //data.transaction.data = '{version:1}';
      }).catch((err) => {
        console.log(err);
      });
    }

    onMounted(() => {
      data.query.txHash = route.params.hash;
      loadTransactionInfo()
    })

    return {
      data
    }
  }
}
</script>
<style lang="scss" scoped>
.info-box {
  padding: 5px 0px;
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
.el-alert__content {
  line-height: 19px !important;
}
.el-alert__title {
  font-size: var(--el-alert-title-font-size);
  line-height: 19px !important;
  vertical-align: text-top;
}
</style>
