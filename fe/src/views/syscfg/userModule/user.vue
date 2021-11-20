<template>
  <div>
    <section class="search-form-wrapper">
      <el-form :inline="true">
        <el-row :gutter="24">
          <el-col :span="20">
            <div class="fl">
              <el-form-item>
                <el-input placeholder="请输入用户地址" clearable v-model="searchForm.address" ></el-input>
              </el-form-item>
              <el-form-item>
                <el-select v-model="searchForm.lssued" placeholder="请选择是否已发放">
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button @click="searchBtn()" type="primary" icon="el-icon-search">查询</el-button>
                <el-button @click="resetSearchBtn()" size="mini">重置</el-button>
              </el-form-item>
            </div>
          </el-col>
          <el-col :span="4" class="text-right">
            <!-- <el-button type="primary" @click="addHandle()" icon="el-icon-plus">
              新增用户
            </el-button> -->
          </el-col>
        </el-row>
      </el-form>
    </section>

    <div
      class="listfile"
      :style="minHeight"
      style="overflow: auto; background: #fff; margin-top: 15px"
    >
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column
          type="selection"
          width="55"
          align="center"
        ></el-table-column>
        <el-table-column prop="address" label="地址" width="400" align="center">
        </el-table-column>
        <el-table-column prop="createtime" label="领取日期" width="260" align="center">
        </el-table-column>
        <el-table-column label="是否已发放空投" width="180" align="center">
          <template #default="scope">
            <p>{{ scope.row.lssued ? '是' : '否' }}</p>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <!-- <el-button size="mini" @click="handleEdit(scope.$index, scope.row)"
              >编辑</el-button
            > -->
            <el-button size="mini" type="danger" @click.native.prevent="deleteRow(scope.$index, tableData)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div style="float: right; margin: 20px 0">
      <el-pagination
        background
        layout="total,prev, pager, next"
        :total="total"
        :current-page.sync="currentPage"
        :page-size="pageSize"
        @size-change="size_Change"
        @current-change="current_change"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      //搜索
      searchForm: {
        address:"",
        lssued:""
      },
      options: [{
        value: false,
        label: '未发放'
      }, {
        value: true,
        label: '已发放'
      }],
        

      total: 0,// 初始化信息总条数
      currentPage: 1, //当前页
      pageSize: 10,// 每页显示多少条
      minHeight: {
        height: '',
      },
      tableData: [
        {id:1,address:'0x123456',createtime:'2021-06-10',lssued:'true'},
        {id:2,address:'0x1234567',createtime:'2021-06-10',lssued:'true'}],
      Data: [],
    };
  },
  methods: {
    //查询
    searchBtn() {
      // return this.$message({
      //   type: "error",
      //   message: "请先配置接口哦"
      // });
      let _this = this;
      this.$api.management.account_find(this.searchForm).then(res => {
        _this.tableData = res.data;
        _this.total = _this.tableData.length;
        _this.Data = _this.tableData;
      })
    },
    // //重置
    resetSearchBtn() {
      this.searchForm = 
      {
        address:"",
        lssued:""
      }
    },
    //添加
    // addHandle() {
    //   this.$emit("addHandle");
    // },
    handleEdit(index, row) {
      console.log(index, row);
    },
    deleteRow(index, rows) {
      rows.splice(index, 1);
    },
    //获取当前浏览器的高度的函数
    getminHeight() {
      this.minHeight.height = (window.innerHeight) * 0.7 + 'px';
    },
    //index当前页码
    // changepage(index){
    //     this.currentPage = index;
    //     var _start = ( index - 1 ) * this.pageSize;
    //     var _end = index * this.pageSize;
    //     this.tableData = this.tableData.slice(_start,_end);
    //     let sessionId = Cookies.get('status'); 
    // },
    current_change(currentPage) {
      console.log(`当前页: ${currentPage}`);
      this.currentPage = currentPage;
      var _start = (currentPage - 1) * this.pageSize;
      var _end = currentPage * this.pageSize;
      this.tableData = this.Data.slice(_start, _end);
      // let sessionId = Cookies.get('status');
    },
    size_Change(val) {
      this.pageSize = val;
      console.log(`每页 ${val} 条`);
    },
  },
  created() {
    this.searchBtn();
  },
  mounted() {
    this.getminHeight(); //调获取当前浏览器的高度 
    this.Data = this.tableData;
  },
  destroyed() {
    this.$destroy();
  }
};
</script>

<style>
</style>