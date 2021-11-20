/**
 * 所有模块接口列表
 */
import axios from '../request/http' // 导入http中创建的axios实例


//设置接口地址
axios.defaults.baseURL = process.env.VUE_APP_BASEURL

//官网模快
const officialWebsite = {
    //查询合约地址
    contract_find () {
        return axios.get(`/api/contract/find`)
    },
    
    //领取空投奖励
    account_add(params){
        return axios.post(`/api/account/add`,params)
    }

}
//管理系统
const management = {
    //查询账户
    account_find (params) {
        return axios.post(`/api/account/find`, params)
    },

    //查询机器人账户
    robot_account_find (params) {
        return axios.post(`/api/robotAccount/find`, params)
    },

    //删除机器人账户
    robot_account_delete (params) {
        return axios.post(`/api/robotAccount/delete`, params)
    },

    //生成机器人账户
    admin_generateRobotAccount(params){
        return axios.post(`/api/admin/generateRobotAccount`, params)
    },

    //修改合约地址
    contract_update (params) {
        return axios.post(`/api/contract/update`,params)
    },

    //得到空投比例
    contract_getAirdropRate(){
        return axios.get(`/api/contract/getAirdropRate`)
    },

    //修改空投比例
    contract_updateAirdropRate(params){
        return axios.post(`/api/contract/updateAirdropRate`,params)
    },

    //空投奖励
    contract_airdrop(params){
        return axios.post(`/api/contract/airdrop`,params)
    },

    //得到销毁比例
    contract_getDestroyRate(){
        return axios.get(`/api/contract/getDestroyRate`)
    },

    //修改销毁比例
    contract_updateDestroyRate(params){
        return axios.post(`/api/contract/updateDestroyRate`,params)
    },

    //销毁
    contract_destroy(params){
        return axios.post(`/api/contract/destroy`,params)
    },

    //设置销毁时长和次数
    contract_setDestroyStep(params){
        return axios.post(`/api/contract/setDestroyStep`,params)
    },

    //得到销毁步骤
    contract_getDestroyStep(){
        return axios.get(`/api/contract/getDestroyStep`)
    },

    //得到确认地址数
    contract_getAirdropData(){
        return axios.get(`/api/contract/getAirdropData`)
    },

    //确认地址数
    contract_determineAirdropNum(params){
        return axios.post(`/api/contract/determineAirdropNum`,params)
    }
}

//系统模快
const systemModule = {
    //登录
    login (params) {
        return axios.post(`login`, params)
    }
}

//质检模块
const qualityControl = {
    //商品管理
    goodsManage: {
        //列表、查询
        getList (params) {
            return axios.post(`/goodList`, params)
        }
    }
}

//导出接口
export default {
    officialWebsite,
    management
}
