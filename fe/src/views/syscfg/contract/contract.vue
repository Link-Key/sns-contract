<template>
  <div>
    <el-form ref="form" label-width="80px" v-loading="loading">
      <el-form-item label="">
        <h1>合约地址</h1>
      </el-form-item>
      <el-form-item>
        <el-form :inline="true"  class="demo-form-inline">
          <el-form-item label="合约地址">
            <el-col >
              <el-input placeholder="合约地址" v-model="contractAddress" disabled></el-input>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col >
              <el-button @click="updateAddress()" type="primary" icon="el-icon-edit">修改地址</el-button>
            </el-col>
          </el-form-item>
        </el-form>
      </el-form-item>
      <el-form-item >
        <h1>代币销毁</h1>
      </el-form-item>
      <el-form-item>
        <el-form :inline="true"  class="demo-form-inline">
          <el-form-item label="代币销毁比例">
            <el-col >
              <el-input placeholder="0" v-model="destroyRate" disabled></el-input>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col >
              <el-button @click="updateDestroyRate()" type="primary" icon="el-icon-edit" :disabled="hasSetDestroyRate">{{!hasSetDestroyRate?'修改销毁比例':'已确定销毁比例'}}</el-button>
            </el-col>
          </el-form-item>
        </el-form>
      </el-form-item>

      <el-form-item>
        <el-form :inline="true"  class="demo-form-inline">
          <el-form-item label="总销毁次数">
            <el-col  >
              <el-input placeholder="总销毁次数" v-model="destroyTimes" disabled></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="已销毁次数">
            <el-col  >
              <el-input placeholder="已销毁次数" v-model="destroiedTimes" disabled></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="销毁周期(秒)">
            <el-col  >
              <el-input placeholder="销毁周期" v-model="destroyStep" disabled></el-input>
            </el-col>
          </el-form-item>
        </el-form>
      </el-form-item>
      <el-form-item>
        <el-form :inline="true"  class="demo-form-inline">
          <el-form-item >
            <el-col :span="5" >
              <el-button @click="setDestroy()" type="primary" icon="el-icon-edit" :disabled="destroyTimes != 0 ">{{destroyTimes == 0 ?'设置参数':'参数已设置'}}</el-button>
            </el-col>
          </el-form-item>
          <el-form-item >
            <el-col :span="5" >
              <el-button @click="destroy()" type="danger" icon="el-icon-delete" :disabled="destroyFinish">{{!destroyFinish?'销毁':'销毁完成'}}</el-button>
            </el-col>
          </el-form-item>
        </el-form>
      </el-form-item>  

      <el-form-item>
        <h1>空投奖励</h1>
      </el-form-item>
      <el-form-item>
        <el-form :inline="true"  class="demo-form-inline">
          <el-form-item label="代币空投比例">
            <el-col >
              <el-input placeholder="0" v-model="airdropRate" disabled></el-input>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col >
              <el-button @click="updateAirdropRate()" type="primary" icon="el-icon-edit" :disabled="hasSetAirdropRate">{{!hasSetAirdropRate?'修改空投比例':'已确定空投比例'}}</el-button>
            </el-col>
          </el-form-item>
        </el-form>
      </el-form-item>
      <el-form-item>
        <el-form :inline="true"  class="demo-form-inline">
          <el-form-item label="预约地址数">
            <el-col >
              <el-input placeholder="预约地址数" v-model="airdropAddressNum" disabled></el-input>
            </el-col>
          </el-form-item>
           <el-form-item label="机器人地址数">
            <el-col >
              <el-input placeholder="机器人地址数" v-model="robotAddressNum" disabled></el-input>
            </el-col>
          </el-form-item>
           <el-form-item label="空投数量">
            <el-col >
              <el-input placeholder="空投数量" v-model="airdropNum" disabled></el-input>
            </el-col>
          </el-form-item>
          
        </el-form>
      </el-form-item>
      <el-form-item>
        <el-form :inline="true"  class="demo-form-inline">
          <el-form-item>
            <el-col >
             <el-button @click="determineAirdropNum()" type="primary" icon="el-icon-check" :disabled="hasDetermine">{{airdropNum == 0 ?'确定预约地址数':'地址数已确认'}}</el-button>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col >
             <el-button @click="airdrop()" type="success" icon="el-icon-heavy-rain" :disabled="airdropFinish || !hasDetermine">{{airdropFinish?'空投发放完成':'发放空投'}}</el-button>
            </el-col>
          </el-form-item>
        </el-form>
      </el-form-item>
    </el-form>

    <el-dialog title="设置销毁参数" :visible.sync="dialogVisible" width="30%"  v-loading="setDestoryloading">
      <el-form ref="form" label-width="80px">
        <el-form-item label="销毁次数">
          <el-input v-model="destroyTimes"></el-input>
        </el-form-item>
        <el-form-item label="销毁周期(秒)">
          <el-input v-model="destroyStep"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="inputPrivatekey('','setDestroy')">确 定</el-button>
      </span>
    </el-dialog>
   
  </div>
</template>

<script>
import {
    getWeb3Instance,
    getDestroyStep,
    setDestroyStep,
    destroy,
    airdrop,

  } from "@/util/web3Utils";


export default {
  data() {
    return {
      contractAddress:"",

      hasSetDestroyRate:false,
      destroyRate:0,
      destroyTimes:0,
      destroiedTimes:0,
      destroyStep:0,
      destroyFinish:false,

      hasSetAirdropRate:false,
      airdropRate:0,
      airdropAddressNum:0,
      airdropNum:0,
      airdropFinish:false,

      robotAddressNum:0,

      dialogVisible:false,
      hasDetermine:false,

      loading: false,
      setDestoryloading:false
    }
  },
  watch:{
    hasDetermine:function(val){
      console.log('hasDetermine',val)
      if(val == true){
        this.hasSetAirdropRate = true;
      }
    },
    destroyTimes:function(val){
      console.log('destroyTimes',val)
      if(val != 0){
        this.hasSetDestroyRate = true;
      }
    } 

  } ,
  created(){
    this.init();
  },
  methods:{
    init(){
      this.loading = true;
      this.getContractAddress();
      this.getDestroyRate();
      this.getDestroyStep();
      this.getAirdropRate();
      this.getAirdropData();
    },
    //更新合约地址
    updateAddress(){
      const _this = this;

      _this.$prompt('请输入新合约地址', '合约地址', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^(0x)?[0-9a-fA-F]{40}$/,
        inputErrorMessage: '合约地址格式不正确',
        inputPlaceholder: _this.contractAddress
      }).then(({ value }) => {
        _this.loading = true;
        let params = {address : value}
        _this.$api.management.contract_update(params).then(res => {
          _this.loading = false;
          if(res.data.code == 200){
            _this.$message({
              type: 'success',
              message: '更新成功'
            });  
            _this.init();
          }else if(res.data.code == -1){
            _this.$message({
              type: 'error',
              message: '未知错误！！'
            }); 
          }
         
        })
      }).catch(() => {
        _this.$message({
          type: 'info',
          message: '取消更新'
        });       
      });
    },

    //得到销毁比例
    getDestroyRate(){
      const _this = this;
      _this.$api.management.contract_getDestroyRate().then(res => {
        _this.destroyRate = res.data.data.destroyRate;
        _this.hasSetDestroyRate = res.data.data.setDestroyRateOnce;
        if(_this.hasSetDestroyRate){
          _this.destroyFinish = true;
        }
      })
    },

    //修改销毁比例
    updateDestroyRate(){
      const _this = this;
      
      _this.$prompt('请输入新的销毁比例', '销毁比例', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[0-9]{2}$/,
        inputErrorMessage: '销毁比例格式不正确',
        inputPlaceholder: _this.destroyRate
      }).then(({ value }) => {
        _this.loading = true;
        let param = {destroyRate : value}
        _this.inputPrivatekey(param,'updateDestroyRate');
      }).catch(() => {
        _this.$message({
          type: 'info',
          message: '取消修改'
        });       
      });
    },

    //设置销毁参数
    setDestroy(){
      const _this = this;
      _this.dialogVisible = true;
    },

    //销毁
    destroy(){
      const _this = this;
      this.$confirm('即将执行销毁, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        _this.loading = true;
        _this.inputPrivatekey('','destroy');
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消销毁'
        });
      });
    },

    //得到空投比例
    getAirdropRate(){
      const _this = this;
      _this.$api.management.contract_getAirdropRate().then(res => {
        _this.airdropRate = res.data.data.airdropRate;
        _this.hasSetAirdropRate = res.data.data.setAirdropRateOnce;
      })
    },

    //修改空投比例
    updateAirdropRate(){
      const _this = this;
      
      _this.$prompt('请输入新的空投比例', '空投比例', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[0-9]{2}$/,
        inputErrorMessage: '空投比例格式不正确',
        inputPlaceholder: _this.airdropRate
      }).then(({ value }) => {
        _this.loading = true;
        let param = {airdropRate : value}
        _this.inputPrivatekey(param,'updateAirdropRate');
      }).catch(() => {
        _this.$message({
          type: 'info',
          message: '取消修改'
        });       
      });
    },

    //空投
    airdrop(){
      const _this = this;
      this.$confirm('即将发放空投奖励, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        _this.inputPrivatekey('','airdrop');
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消发放'
        });
      });
    },

    //得到销毁次数
    getDestroyStep(){
      const _this = this;
      _this.$api.management.contract_getDestroyStep().then(res => {
        _this.destroyTimes = res.data.data.times;
        _this.destroiedTimes = res.data.data.destroiedTimes;
        _this.destroyStep = res.data.data.destroyStep;
        if(_this.destroyTimes == _this.destroiedTimes ){
           _this.destroyFinish = true;
        }
        _this.loading = false;
      })
    },

    //得到合约地址
    getContractAddress(){
      const _this = this;
      this.$api.officialWebsite.contract_find().then(res => {
        _this.contractAddress = res.data.data;
      })
    },

    //得到预约地址数
    getAirdropData(){
      const _this = this;
      this.$api.management.contract_getAirdropData().then(res => {
        _this.airdropAddressNum = res.data.data.accountCount ;
        _this.airdropNum = res.data.data.airdropNum || 0;
        _this.airdropFinish = res.data.data.airdropFinish;
        if(_this.airdropNum == 0){
          _this.hasDetermine = false
        }else{
          _this.hasDetermine = true
        }
        _this.robotAddressNum = res.data.data.robotAddressNum ;
      })
    }, 

    //确定预约地址数
    determineAirdropNum(){
      const _this = this;
      this.$confirm('预约地址数为:'+_this.airdropAddressNum+',机器人地址数为:'+_this.robotAddressNum+',是否确认?（确认后不可修改！）', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        _this.loading = true;
        _this.inputPrivatekey('','determineAirdropNum');
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消确认'
        });
      });
      
    },

    //输入管理员密钥
    inputPrivatekey(param,type){
      const _this = this;
      _this.$prompt('请输入管理员私钥', '管理员私钥', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^0[xX][0-9a-fA-F]{64}$/,
        inputErrorMessage: '私钥格式不正确'
      }).then(({ value }) => {
        console.log("value",value)
        let privatekey = value;
        let params;
        switch (type) {

          case 'setDestroy':
            params = {
              stepLength:_this.destroyStep,
              destroyTimes:_this.destroyTimes,
              privateKey: privatekey
            }
            _this.setDestoryloading = true;
            _this.$api.management.contract_setDestroyStep(params).then(res => {
              _this.setDestoryloading = false;
              if(res.data.code == 200){
                _this.$message({
                  type: 'success',
                  message: '更新成功'
                });  
                _this.init();
              }else if(res.data.code == -1){
                _this.$message({
                  type: 'error',
                  message: '未知错误！！'
                }); 
              }
              
            })
            break;

          case 'updateDestroyRate':
            params = {
              destroyRate: param.destroyRate,
              privateKey: privatekey
            }

            _this.$api.management.contract_updateDestroyRate(params).then(res => {
              _this.loading = false;
              if(res.data.code == 200){
                _this.$message({
                  type: 'success',
                  message: '更新成功'
                });  
                _this.init();
              }else if(res.data.code == -1){
                _this.$message({
                  type: 'error',
                  message: '未知错误！！'
                }); 
              }
            })
            break;

          case 'destroy':
            params = {
              privateKey: privatekey
            }
            _this.$api.management.contract_destroy(params).then(res => {
               _this.loading = false;
              if(res.data.code == 200){
                _this.destroiedTimes += 1;
                _this.$message({
                  type: 'success',
                  message: '更新成功'
                });  
                _this.init();
              }else if(res.data.code == 500){
                _this.$message({
                  type: 'error',
                  message: '销毁失败！！'
                }); 
              }else if(res.data.code == -1){
                _this.$message({
                  type: 'error',
                  message: '未知错误！！'
                }); 
              }
            })
            break;

          case 'updateAirdropRate':
            params = {
              destroyRate: param.airdropRate,
              privateKey: privatekey
            }
            
            _this.$api.management.contract_updateAirdropRate(params).then(res => {
              _this.loading = false;
              if(res.data.code == 200){
                _this.$message({
                  type: 'success',
                  message: '更新成功'
                });  
                _this.init();
              }else if(res.data.code == -1){
                _this.$message({
                  type: 'error',
                  message: '未知错误！！'
                }); 
              }
            })
            break;

          case 'airdrop':
            _this.$message({
              type: 'info',
              message: '空投等待时间较长，等通过区块链浏览器查看交易情况！！！'
            });  
            params = {
              privateKey: privatekey
            }
            _this.$api.management.contract_airdrop(params).then(res => {
              if(res.data.code == 200){
                _this.$message({
                  type: 'success',
                  message: '更新成功'
                });  
                _this.init();
              }else if(res.data.code == -1){
                _this.$message({
                  type: 'error',
                  message: '未知错误！！'
                }); 
              }
            })
            break;

          case 'determineAirdropNum':
            params = {
              privateKey: privatekey
            }
            _this.$api.management.contract_determineAirdropNum(params).then(res => {
               _this.loading = false;
              if(res.data.data.txHash){
                _this.hasDetermine = true;
                _this.$alert('确认空投地址数成功，请查看交易'+res.data.data.txHash, '确认成功', {
                  confirmButtonText: '确定',
                  callback: action => {
                    _this.$message({
                      type: 'info',
                      message: `交易正在被确认，请稍候`
                    });
                  }
                });
              }else{
                _this.$message({
                      type: 'error',
                      message: `修改失败`
                    });
              }
            })
            break;
        
          default:
            break;
        }
      }).catch(() => {
        _this.$message({
          type: 'info',
          message: '取消操作'
        });       
      });

    },
  }

}
</script>

<style>
</style>