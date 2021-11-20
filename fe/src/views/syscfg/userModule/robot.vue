<template>
  <div>
    <section class="search-form-wrapper">
      <el-form :inline="true">
        <el-row :gutter="24">
          <el-col :span="18">
            <div class="fl">
              <el-form-item>
                <el-input placeholder="请输入用户地址" clearable v-model="address" ></el-input>
              </el-form-item>
              
              <el-form-item>
                <el-button @click="searchBtn()" type="primary" icon="el-icon-search">查询</el-button>
                <el-button @click="resetSearchBtn()" size="mini">重置</el-button>
              </el-form-item>
            </div>
          </el-col>
          <el-col :span="3" >
            <el-button type="danger" @click="deleteMoreRow()" icon="el-icon-delete">
              删除用户
            </el-button>
          </el-col>
          <el-col :span="3" class="text-right">
            <el-button type="primary" @click="addRoot()" icon="el-icon-plus">
              新增用户
            </el-button>
          </el-col>
        </el-row>
      </el-form>
    </section>

    <div class="listfile" :style="minHeight" style="overflow: auto; background: #fff; margin-top: 15px">

      <el-table ref="multipleTable" tooltip-effect="dark" @selection-change="handleSelectionChange" :data="tableData" border style="width: 100%" height="650">
        <el-table-column type="selection" width="55" align="center">
        </el-table-column>
        <el-table-column prop="address" label="地址" width="300" align="center">
        </el-table-column>
         <el-table-column prop="mnemonicCode" label="助记词" width="300" align="center">
        </el-table-column>
        <el-table-column prop="privateKey" label="私钥" width="300" align="center">
        </el-table-column>
        <el-table-column prop="publicKey" label="公钥" width="400" align="center">
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
      <el-pagination background layout="total,prev, pager, next" :total="total" :current-page.sync="currentPage" :page-size="pageSize" @size-change="size_Change" @current-change="current_change"></el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      //搜索
      searchForm: {
        
      },
      address:"",
      
      multipleSelection:[],
      deleteIds:[],

      total: 0,// 初始化信息总条数
      currentPage: 1, //当前页
      pageSize: 10,// 每页显示多少条
      minHeight: {
        height: '',
      },
      tableData: [],
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
      let params = {
        pageNum:_this.currentPage+'',
        pageSize:_this.pageSize+'',
        robotAccount:_this.address
      }
      this.$api.management.robot_account_find(params).then(res => {
        console.log(res)
        _this.tableData = res.data.list;
        _this.total = res.data.total;
        _this.Data = _this.tableData;
      })
      // _this.generateRobotAccount({count:1})
    },
    // //重置
    resetSearchBtn() {
      this.searchForm = 
      {
        address:""
      }
    },
    //添加
    addRoot() {
      const _this = this;
      _this.$prompt('请输入生成数量', '生成机器人账户', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /^[0-9]*$/,
          inputErrorMessage: '只能输入数字'
        }).then(({ value }) => {
          console.log("value",value)
         
          let params = {count : value}
          _this.generateRobotAccount(params)
          
        }).catch(() => {
          _this.$message({
            type: 'info',
            message: '取消生成'
          });       
        });
    },
    generateRobotAccount(params){
      const _this = this;
      _this.$api.management.admin_generateRobotAccount(params).then(res => {
        _this.$message({
          type: 'success',
          message: '成功生成 ' + params.count + '个机器人账户'
        });
        _this.searchBtn()
      }).catch(() =>{
        _this.$message({
            type: 'info',
            message: '生成失败'
          });
      })

    },

    handleEdit(index, row) {
      console.log(index, row);
    },

    deleteRobotAccount(params){
      const _this = this;
      _this.$api.management.robot_account_delete(params).then(res => {
        this.$message({
            type: 'success',
            message: '删除成功!'
          });
        _this.deleteIds = []
        _this.searchBtn()
      }).catch(() =>{
        _this.$message({
            type: 'info',
            message: '删除失败！'
          });
      })
    },

    deleteRow(index, rows) {
      const _this = this;
      this.$confirm('永久删除该账户, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          rows.forEach((item,indexed) =>{
            if(index == indexed){
              if( _this.deleteIds.length == 0){
                _this.deleteIds.push(item.id);
              }else{
                _this.deleteIds.forEach((itemed,indexeds) =>{
                  let flag = true;
                  if(itemed == item.id){
                    flag = false;
                  }
                  if(flag){
                    _this.deleteIds.push(item.id);
                  }
                })
              }
            }
          })
          let params = {ids:_this.deleteIds};
          _this.deleteRobotAccount(params);
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
    },

    deleteMoreRow() {
      const _this = this;
      if(_this.multipleSelection.length == 0 ){
        return _this.$message({
          type: 'info',
          message: '请选择删除信息'
        });
      }
      this.$confirm('永久删除该账户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        _this.multipleSelection.forEach((item,indexed) =>{
          if( _this.deleteIds.length == 0){
            _this.deleteIds.push(item.id);
          }else{
            let flag = true;
            _this.deleteIds.forEach((itemed,indexeds) =>{
              if(itemed == item.id){
                flag = false;
              }
            })
            if(flag){
              _this.deleteIds.push(item.id);
            }
          }
        })  
        let params = {ids:_this.deleteIds};
        _this.deleteRobotAccount(params);
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },

    handleSelectionChange(val) {
      const _this = this;
      _this.multipleSelection = val;
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
      // var _start = (currentPage - 1) * this.pageSize;
      // var _end = currentPage * this.pageSize;
      // this.tableData = this.Data.slice(_start, _end);
      // let sessionId = Cookies.get('status');
      this.searchBtn()
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