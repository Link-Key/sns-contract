<template>
  <div id="app">
      <el-form ref="form" label-width="80px" >
        <el-form-item label="">
          <h1>代币信息</h1>
        </el-form-item>
        <el-form-item>
          <el-button @click="handle()" type="primary" icon="el-icon-edit">查看代币信息</el-button>
        </el-form-item> 
      </el-form>

  </div>
</template>
<script>
import Vue from 'vue'
export default {
 
  methods: {
    widthHeight() {
      this.searchTableHeight = window.innerHeight -180;
      this.searchTableWidth = window.innerWidth - 230
    },
    handle() {
      console.log('123',this.contractUrl)
      window.open(this.contractUrl, '_blank')
    },
    getContractAddress() {
      const _this = this;
      this.$api.officialWebsite.contract_find().then(res => {
        _this.contractUrl = "https://testnet.bscscan.com/address/" + res.data.data;
      })
    },
  },
  data() {
    return {
      contractUrl: '',
      searchTableHeight: 0,
      searchTableWidth: 0
    }
  },
   mounted() {
    window.onresize = () => {
      this.widthHeight(); // 自适应高宽度
    };
    this.$nextTick(function () {
      this.widthHeight();
    });
  },
  created() {
    // 从路由里动态获取 url地址   具体地址看libs下util.js里的 backendMenuToRoute  方法 
    this.getContractAddress();
  },
  watch: {
    '$route': function () {
      // 监听路由变化
      this.reportUrl =  'https://testnet.bscscan.com/'
    }
  }
}
</script>