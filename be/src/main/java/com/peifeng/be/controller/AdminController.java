package com.peifeng.be.controller;

import com.peifeng.be.contract.FreeEagle;
import com.peifeng.be.model.Contract;
import com.peifeng.be.model.RobotAccount;
import com.peifeng.be.service.AccountService;
import com.peifeng.be.service.ContractService;
import com.peifeng.be.service.RobotAccountService;
import com.peifeng.be.util.Wallet;
import com.peifeng.be.util.Web3JUtils;
import com.peifeng.be.util.result.Result;
import com.peifeng.be.util.result.ResultEnum;
import org.bitcoinj.crypto.MnemonicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.*;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/6/7 15:29
 */
@Controller
@RequestMapping(value = "/api/admin")
public class AdminController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RobotAccountService robotAccountService;


    @ResponseBody
    @PostMapping("/airdrop")
    public Result airdrop(@RequestParam(name = "privateKey", required = true)String privateKey){

        Contract contract = contractService.findAllContract();

        int airdropAddressNum = accountService.accountCount();

        FreeEagle freeEagle = Web3JUtils.getContract(privateKey,contract.getAddress());
        Result result = new Result();
        TransactionReceipt receipt;
        try {
            receipt = freeEagle.setAirdropAddressNum(new BigInteger(String.valueOf(airdropAddressNum))).send();
            if ("0x1".equals(receipt.getStatus())) {
                //List<Account> accounts = accountService.findAllAccount()
            }

            result.setCode(ResultEnum.SUCCESS.getCode());
            result.setMsg(ResultEnum.SUCCESS.getMsg());

        }catch (Exception duplicateKeyException){
            result.setCode(ResultEnum.REPEAT.getCode());
            result.setMsg(ResultEnum.REPEAT.getMsg());
        }


        return result;
    }

    @ResponseBody
    @PostMapping("/generateRobotAccount")
    public Result generateRobotAccount(@RequestBody Map<String,Integer> params){
        List<String> strs;
        RobotAccount robotAccount;
        Result result = new Result();
        List<RobotAccount> robotAccounts = new ArrayList<>();
        for(int i = 0; i < params.get("count"); i++){
            try {
                robotAccount = new RobotAccount();
                strs = new Wallet().getMnemonicCode();
                ECKeyPair keyPair = new Wallet().createWallet(strs);
                robotAccount.setMnemonicCode(strs.toString());
                robotAccount.setAddress("0x"+ Keys.getAddress(keyPair.getPublicKey()));
                robotAccount.setPrivateKey("0x"+keyPair.getPrivateKey().toString(16));
                robotAccount.setPublicKey(keyPair.getPublicKey().toString(16));
                robotAccount.setLssued(Boolean.FALSE);
                robotAccounts.add(robotAccount);
            }catch (MnemonicException.MnemonicLengthException mnemonicLengthException){
                result.setCode(ResultEnum.UNKONW_ERROR.getCode());
                result.setMsg("生成第"+i+"账户时发生错误");
                return result;
            }
        }
        robotAccountService.batchInsert(robotAccounts);
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(ResultEnum.SUCCESS.getMsg());
        return result;
    }

}
