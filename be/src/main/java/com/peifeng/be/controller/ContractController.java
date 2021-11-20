package com.peifeng.be.controller;

import com.github.pagehelper.PageInfo;
import com.peifeng.be.contract.FreeEagle;
import com.peifeng.be.model.*;
import com.peifeng.be.service.AccountService;
import com.peifeng.be.service.ContractService;
import com.peifeng.be.service.RobotAccountService;
import com.peifeng.be.util.Web3JUtils;
import com.peifeng.be.util.exception.AddressException;
import com.peifeng.be.util.exception.Web3Exception;
import com.peifeng.be.util.result.AddressEnum;
import com.peifeng.be.util.result.Result;
import com.peifeng.be.util.result.ResultEnum;
import com.peifeng.be.util.result.Web3Enum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.protocol.admin.Admin;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/5/28 10:23
 */
@Controller
@RequestMapping(value = "/api/contract")
public class ContractController {
    @Autowired
    private ContractService contractService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RobotAccountService robotAccountService;

    private Integer accountCount;

    private String contractAddress;

    private Integer robotAccountCount;

    private static String emptyAddress = "0x0000000000000000000000000000000000000000";

    @PostConstruct
    public void init(){
        contractAddress = contractService.findAllContract().getAddress();

    }

    @ResponseBody
    @GetMapping("/find")
    public Result<Contract> find(){
        Result result = new Result();
        if(contractAddress == null){
            result.setCode(ResultEnum.UNKONW_ERROR.getCode());
            result.setMsg(ResultEnum.UNKONW_ERROR.getMsg());
        }else{
            result.setData(contractAddress);
            result.setCode(ResultEnum.SUCCESS.getCode());
            result.setMsg(ResultEnum.SUCCESS.getMsg());
        }
        return result;
    }

    @ResponseBody
    @PostMapping("/update")
    public Result<Contract> update(@RequestBody Contract contract){
        String address = contract.getAddress();
        if(!Web3JUtils.isETHValidAddress(address)){
            throw new AddressException(AddressEnum.FORMAT_ERROR);
        }
        Result result = new Result();
        int resultNum = contractService.updateByPrimaryKeySelective(contract);
        if(resultNum == 1){
            contractAddress = address;
            result.setData(contract);
            result.setCode(ResultEnum.SUCCESS.getCode());
            result.setMsg(ResultEnum.SUCCESS.getMsg());
        }else{
            result.setCode(ResultEnum.UNKONW_ERROR.getCode());
            result.setMsg(ResultEnum.UNKONW_ERROR.getMsg());
        }
        return result;
    }

    @ResponseBody
    @PostMapping("/setAirdropAddressNum")
    public Result setAirdropAddressNum(@RequestBody Map<String,String> params){
        Result result = new Result();
        String privateKey = params.get("privateKey");
        if(!Web3JUtils.isPrivateKey(privateKey)){
            throw new AddressException(AddressEnum.PRIVATEKEY_ERROR);
        }
        BigInteger airdropAddressNum = new BigInteger(params.get("airdropAddressNum"));
        FreeEagle freeEagle = Web3JUtils.getContract(privateKey,contractAddress);
        try {
            TransactionReceipt receipt = freeEagle.setAirdropAddressNum(airdropAddressNum).send();
            if(receipt.isStatusOK()){
                result.setCode(Web3Enum.SUCCESS.getCode());
                result.setMsg(Web3Enum.SUCCESS.getMsg());
            }
        }catch (Web3Exception e){
            result.setCode(e.getCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Web3Enum.ERROR.getCode());
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @GetMapping("/getAirdropRate")
    public Result getAirdropRate(){
        Result result = new Result();

        //得到销毁次数
        String methodName = "airdropRate";
        String airdropRate = null;
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Uint> typeReference = new TypeReference<Uint>() {};
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress,
                data);

        EthCall ethCall;
        try {
            ethCall = Web3JUtils.initWeb3j().ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            String value = ethCall.getValue();
            List<Type> results = FunctionReturnDecoder.decode(value, function.getOutputParameters());
            airdropRate = results.get(0).getValue().toString();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //查看销毁次数与
        methodName = "setAirdropRateOnce";

        outputParameters.clear();
        TypeReference<Bool> typeReference1 = new TypeReference<Bool>() {};
        outputParameters.add(typeReference1);
        function = new Function(methodName, inputParameters, outputParameters);

        data = FunctionEncoder.encode(function);
        transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress,
                data);

        boolean setAirdropRateOnce = false;
        try {
            ethCall = Web3JUtils.initWeb3j().ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            String value = ethCall.getValue();
            List<Type> results = FunctionReturnDecoder.decode(value, function.getOutputParameters());
            setAirdropRateOnce = (boolean)results.get(0).getValue();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        Map<String,Object> map = new HashMap<>();
        map.put("airdropRate",airdropRate);
        map.put("setAirdropRateOnce",setAirdropRateOnce);

        result.setData(map);
        result.setCode(Web3Enum.SUCCESS.getCode());
        result.setMsg(Web3Enum.SUCCESS.getMsg());
        return result;
    }

    @ResponseBody
    @PostMapping("/updateAirdropRate")
    public Result updateAirdropRate(@RequestBody Map<String,String> params){
        Result result = new Result();
        String privateKey = params.get("privateKey");
        String destroyRate = params.get("destroyRate");
        String txHash = null;

        if(!Web3JUtils.isPrivateKey(privateKey)){
            throw new AddressException(AddressEnum.PRIVATEKEY_ERROR);
        }
        FreeEagle freeEagle = Web3JUtils.getContract(privateKey,contractAddress);

        try {
            TransactionReceipt receipt = freeEagle.setAirdropRate(new BigInteger(destroyRate)).send();
            if(receipt.isStatusOK()){
                txHash = receipt.getTransactionHash();
                //txHash="000";
                Map<String,String> map = new HashMap<>();
                map.put("txHash",txHash);
                result.setData(map);
                result.setCode(Web3Enum.SUCCESS.getCode());
                result.setMsg(Web3Enum.SUCCESS.getMsg());
            }
        }catch (Web3Exception e){
            result.setCode(e.getCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            result.setCode(Web3Enum.ERROR.getCode());
            result.setMsg(e.getMessage());
        }

        return result;
    }


    @ResponseBody
    @PostMapping("/airdrop")
    public Result airdrop(@RequestBody Map<String,String> params){
        Result result = new Result();
        String privateKey = params.get("privateKey");
        if(!Web3JUtils.isPrivateKey(privateKey)){
            throw new AddressException(AddressEnum.PRIVATEKEY_ERROR);
        }

        List<String> accountStrs = new ArrayList<>();
        List<Account> accountlist = new ArrayList<>();
        List<String> robotStrs = new ArrayList<>();
        List<RobotAccount> robotList = new ArrayList<>();
        List<String> airdropList = new ArrayList<>();
        int timeStepLength = 40;

        FreeEagle freeEagle = Web3JUtils.getContract(privateKey,contractAddress);
        try {
            AccountExample accountExample = new AccountExample();
            accountExample.createCriteria().andLssuedEqualTo(Boolean.FALSE);
            accountlist = accountService.selectByExample(accountExample);
            accountlist.forEach(account -> {
                accountStrs.add(account.getAddress());
            });
            if(accountlist.size() != 0){
                sendTransfer(freeEagle, accountStrs, airdropList, timeStepLength,1);
            }

            RobotAccountExample robotAccountExample = new RobotAccountExample();
            robotAccountExample.createCriteria().andLssuedEqualTo(Boolean.FALSE);
            robotList = robotAccountService.selectByExample(robotAccountExample);
            robotList.forEach(account -> {
                robotStrs.add(account.getAddress());
            });
            if(robotList.size() != 0){
                sendTransfer(freeEagle, robotStrs, airdropList, timeStepLength,2);
            }

            result.setCode(Web3Enum.SUCCESS.getCode());
            result.setMsg(Web3Enum.SUCCESS.getMsg());
        }catch (Web3Exception e){
            result.setCode(e.getCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Web3Enum.ERROR.getCode());
            result.setMsg(e.getMessage());
        }


        if(alreadyAirdropAccountList.size() != 0){
            accountService.batchUpdate(alreadyAirdropAccountList);
        }
        if(alreadyAirdropRobotList.size() != 0){
            robotAccountService.batchUpdate(alreadyAirdropRobotList);
        }
        return result;
    }

    private List<String> alreadyAirdropAccountList = new ArrayList<>();
    private List<String> alreadyAirdropRobotList = new ArrayList<>();

    private void sendTransfer(FreeEagle freeEagle, List<String> accounts, List<String> airdropList,
                              int timeStepLength,int type)  {
        airdropList.clear();

        if(accounts.size() < timeStepLength){
            timeStepLength = accounts.size();
        }

        for(int i = 0; i < timeStepLength; i++){
            airdropList.add(accounts.get(i));
        }

        TransactionReceipt receipt = null;
        try{
            receipt = freeEagle.airdrop(airdropList).send();
        }catch (Exception e) {
            timeStepLength -= 10;
            sendTransfer(freeEagle, accounts, airdropList, timeStepLength,type);
        }

        switch (type){
            case 1:
                alreadyAirdropAccountList.addAll(airdropList);
                break;
            case 2:
                alreadyAirdropRobotList.addAll(airdropList);
                break;
            default:
                break;
        }
        accounts.removeAll(airdropList);
        if(accounts.size() != 0){
            sendTransfer(freeEagle, accounts, airdropList, timeStepLength,type);
        }else{
            return;
        }



    }


    @ResponseBody
    @GetMapping("/getDestroyRate")
    public Result getDestroyRate(){
        Result result = new Result();

        //得到销毁次数
        String methodName = "destroyRate";
        String destroyRate = null;
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Uint> typeReference = new TypeReference<Uint>() {};
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress,
                data);

        EthCall ethCall;
        try {
            ethCall = Web3JUtils.initWeb3j().ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            String value = ethCall.getValue();
            List<Type> results = FunctionReturnDecoder.decode(value, function.getOutputParameters());
            destroyRate = results.get(0).getValue().toString();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //查看销毁次数与
        methodName = "setDestroyRateOnce";

        outputParameters.clear();
        TypeReference<Bool> typeReference1 = new TypeReference<Bool>() {};
        outputParameters.add(typeReference1);
        function = new Function(methodName, inputParameters, outputParameters);

        data = FunctionEncoder.encode(function);
        transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress,
                data);

        boolean setDestroyRateOnce = false;
        try {
            ethCall = Web3JUtils.initWeb3j().ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            String value = ethCall.getValue();
            List<Type> results = FunctionReturnDecoder.decode(value, function.getOutputParameters());
            setDestroyRateOnce = (boolean)results.get(0).getValue();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        Map<String,Object> map = new HashMap<>();
        map.put("destroyRate",destroyRate);
        map.put("setDestroyRateOnce",setDestroyRateOnce);

        result.setData(map);
        result.setCode(Web3Enum.SUCCESS.getCode());
        result.setMsg(Web3Enum.SUCCESS.getMsg());
        return result;
    }

    @ResponseBody
    @PostMapping("/updateDestroyRate")
    public Result updateDestroyRate(@RequestBody Map<String,String> params){
        Result result = new Result();
        String privateKey = params.get("privateKey");
        String destroyRate = params.get("destroyRate");
        if(!Web3JUtils.isPrivateKey(privateKey)){
            throw new AddressException(AddressEnum.PRIVATEKEY_ERROR);
        }
        FreeEagle freeEagle = Web3JUtils.getContract(privateKey,contractAddress);
        try {
            BigInteger destroyRateValue = new BigInteger(destroyRate);
            TransactionReceipt receipt = freeEagle.setDestroyRate(destroyRateValue).send();
            if(receipt.isStatusOK()){
                result.setCode(Web3Enum.SUCCESS.getCode());
                result.setMsg(Web3Enum.SUCCESS.getMsg());
            }
        }catch (Web3Exception e){
            result.setCode(e.getCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Web3Enum.ERROR.getCode());
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @PostMapping("/setDestroyStep")
    public Result setDestroyStep(@RequestBody Map<String,String> params){
        Result result = new Result();
        String stepLength = params.get("stepLength");
        String destroyTimes = params.get("destroyTimes");
        String privateKey = params.get("privateKey");
        if(!Web3JUtils.isPrivateKey(privateKey)){
            throw new AddressException(AddressEnum.PRIVATEKEY_ERROR);
        }
        FreeEagle freeEagle = Web3JUtils.getContract(privateKey,contractAddress);
        try {
            TransactionReceipt receipt = freeEagle.setDestroyStep(new BigInteger(stepLength+""),new BigInteger(destroyTimes+"")).send();
            if(receipt.isStatusOK()){
                result.setCode(Web3Enum.SUCCESS.getCode());
                result.setMsg(Web3Enum.SUCCESS.getMsg());
            }
        }catch (Web3Exception e){
            result.setCode(e.getCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Web3Enum.ERROR.getCode());
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @PostMapping("/destroy")
    public Result destroy(@RequestBody Map<String,String> params){
        //@RequestBody Map<String,String> params
        Result result = new Result();

        String privateKey = params.get("privateKey");
        if(!Web3JUtils.isPrivateKey(privateKey)){
            throw new AddressException(AddressEnum.PRIVATEKEY_ERROR);
        }
        FreeEagle freeEagle = Web3JUtils.getContract(privateKey,contractAddress);
        try {
            TransactionReceipt receipt = freeEagle.destroy().send();
            if(receipt.isStatusOK()){
                result.setCode(Web3Enum.SUCCESS.getCode());
                result.setMsg(Web3Enum.SUCCESS.getMsg());
            }
        }catch (Web3Exception e){
            result.setCode(e.getCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Web3Enum.ERROR.getCode());
            result.setMsg(e.getMessage());
        }
        return result;
    }


    @ResponseBody
    @GetMapping("/getDestroyStep")
    public Result getDestroyStep(){
        Result result = new Result();

        //得到销毁次数
        String methodName = "destroyTimes";
        String times = null;
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Uint> typeReference = new TypeReference<Uint>() {};
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress,
                data);

        EthCall ethCall;
        try {
            ethCall = Web3JUtils.initWeb3j().ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            String value = ethCall.getValue();
            List<Type> results = FunctionReturnDecoder.decode(value, function.getOutputParameters());
            times = results.get(0).getValue().toString();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //查看销毁次数与
        methodName = "destroyStep";
        BigInteger destroyTime = new BigInteger("0");

        Uint256 destroyTimeValue = new Uint256(destroyTime);

        inputParameters.add(destroyTimeValue);

        function = new Function(methodName, inputParameters, outputParameters);

        data = FunctionEncoder.encode(function);
        transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress,
                data);

        String destroyStep1 = null;
        try {
            ethCall = Web3JUtils.initWeb3j().ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            String value = ethCall.getValue();
            List<Type> results = FunctionReturnDecoder.decode(value, function.getOutputParameters());
            destroyStep1 = results.get(0).getValue().toString();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //销毁次数-1之间的时间间隔
        destroyTimeValue = new Uint256(destroyTime.add(BigInteger.ONE));

        inputParameters.clear();
        inputParameters.add(destroyTimeValue);

        function = new Function(methodName, inputParameters, outputParameters);

        data = FunctionEncoder.encode(function);
        transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress,
                data);

        String destroyStep2 = null;
        try {
            ethCall = Web3JUtils.initWeb3j().ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            String value = ethCall.getValue();
            List<Type> results = FunctionReturnDecoder.decode(value, function.getOutputParameters());
            destroyStep2 = results.get(0).getValue().toString();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Integer destroyStep = Integer.parseInt(destroyStep2) - Integer.parseInt(destroyStep1);

        //已经销毁的次数
        String destroiedTimes = null;
        methodName = "destroyed";

        TypeReference<Bool> typeReference1 = new TypeReference<Bool>() {};
        outputParameters.clear();
        outputParameters.add(typeReference1);
        boolean destroyed = false;
        for(int i = 0; i < Integer.parseInt(destroyStep1); i++){
            Uint256 destroiedTimesValue = new Uint256(new BigInteger(i+""));
            inputParameters.clear();
            inputParameters.add(destroiedTimesValue);

            function = new Function(methodName, inputParameters, outputParameters);
            data = FunctionEncoder.encode(function);
            transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress,
                    data);
            try {
                ethCall = Web3JUtils.initWeb3j().ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
                String value = ethCall.getValue();
                List<Type> results = FunctionReturnDecoder.decode(value, function.getOutputParameters());
                destroyed = (boolean) results.get(0).getValue();

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            if(!destroyed){
                destroiedTimes =  i+"";
                break;
            }
        }

        Map<String,String> map = new HashMap<>();
        map.put("times",times);
        map.put("destroiedTimes",destroiedTimes);
        map.put("destroyStep",destroyStep.toString());

        result.setData(map);
        result.setCode(Web3Enum.SUCCESS.getCode());
        result.setMsg(Web3Enum.SUCCESS.getMsg());
        return result;
    }

    @ResponseBody
    @GetMapping("/getAirdropData")
    public Result getAirdropData(){
        Result result = new Result();


        //得到空投奖励
        String methodName = "getAirdropNum";
        //getAirdropNum
        String airdropNum = null;
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Uint> typeReference = new TypeReference<Uint>() {};
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractService.findAllContract().getAddress(),
                data);

        EthCall ethCall;
        List<Type> results;
        try {
            ethCall = Web3JUtils.initWeb3j().ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            String value = ethCall.getValue();
            results = FunctionReturnDecoder.decode(value, function.getOutputParameters());
            if(!results.isEmpty()){
                airdropNum = results.get(0).getValue().toString();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        methodName = "airdropFinish";
        boolean airdropFinish = false;

        outputParameters.clear();
        TypeReference<Bool> typeReference1 = new TypeReference<Bool>() {};
        outputParameters.add(typeReference1);
        function = new Function(methodName, inputParameters, outputParameters);
        data = FunctionEncoder.encode(function);
        transaction = Transaction.createEthCallTransaction(fromAddr, contractService.findAllContract().getAddress(),
                data);
        try {
            ethCall = Web3JUtils.initWeb3j().ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            String value = ethCall.getValue();
            results = FunctionReturnDecoder.decode(value, function.getOutputParameters());
            if(!results.isEmpty()){
                airdropFinish = (boolean)results.get(0).getValue();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        accountCount = accountService.accountCount();
        robotAccountCount = robotAccountService.robotAccountCount();

        Map<String,Object> map = new HashMap<>();
        map.put("airdropNum",airdropNum);
        map.put("airdropFinish",airdropFinish);
        map.put("accountCount",accountCount+"");
        map.put("robotAddressNum",robotAccountCount+"");

        result.setData(map);
        result.setCode(Web3Enum.SUCCESS.getCode());
        result.setMsg(Web3Enum.SUCCESS.getMsg());
        return result;

    }

    @ResponseBody
    @PostMapping("/determineAirdropNum")
    public Result determineAirdropNum(@RequestBody Map<String,String> params){
        Result result = new Result();
        String privateKey = params.get("privateKey");
        String txHash = null;

        ////设置空投地址数
        //String methodName = "setAirdropAddressNum";
        ////getAirdropNum
        //Integer airdropAddressNum = accountCount + robotAccountCount;
        //String fromAddr = emptyAddress;
        //List<Type> inputParameters = new ArrayList<>();
        //List<TypeReference<?>> outputParameters = new ArrayList<>();
        //
        //Uint256 accountCountValue = new Uint256(new BigInteger(airdropAddressNum+""));
        //inputParameters.add(accountCountValue);
        //
        //Function function = new Function(methodName, inputParameters, outputParameters);
        //
        //String data = FunctionEncoder.encode(function);
        //
        //EthGetTransactionCount ethGetTransactionCount = Web3JUtils.initWeb3j()
        //        .ethGetTransactionCount(fromAddr, DefaultBlockParameterName.PENDING).sendAsync().get();
        //BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        //BigInteger gasPrice = Convert.toWei(BigDecimal.valueOf(5), Convert.Unit.GWEI).toBigInteger();
        //
        //Transaction transaction = Transaction.createFunctionCallTransaction(fromAddr, nonce, gasPrice,
        //        BigInteger.valueOf(60000), contractAddress, data);
        //
        //EthSendTransaction ethSendTransaction = Web3JUtils.initWeb3j().ethSendTransaction(transaction).sendAsync().get();
        if(!Web3JUtils.isPrivateKey(privateKey)){
            throw new AddressException(AddressEnum.PRIVATEKEY_ERROR);
        }
        FreeEagle freeEagle = Web3JUtils.getContract(privateKey,contractAddress);
        accountCount = accountService.accountCount();
        robotAccountCount = robotAccountService.robotAccountCount();
        Integer airdropAddressNum = accountCount + robotAccountCount;
        try {
            TransactionReceipt receipt = freeEagle.setAirdropAddressNum(new BigInteger(airdropAddressNum+"")).send();
            //TransactionReceipt receipt = freeEagle.getBalance().send();
            if(receipt.isStatusOK()){
                txHash = receipt.getTransactionHash();
                //txHash="000";
                Map<String,String> map = new HashMap<>();
                map.put("txHash",txHash);
                result.setData(map);
                result.setCode(Web3Enum.SUCCESS.getCode());
                result.setMsg(Web3Enum.SUCCESS.getMsg());
            }
        }catch (Web3Exception e){
            result.setCode(e.getCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            result.setCode(Web3Enum.ERROR.getCode());
            result.setMsg(e.getMessage());
        }

        return result;
    }



    private Admin admin;
    @ResponseBody
    @GetMapping("TestTransfer")
    public void Test(){


        String txHash = null;

        try {
            admin = Admin.build(new HttpService("https://data-seed-prebsc-1-s3.binance.org:8545/"));
            PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(
                    "0x9bDe3A309707FbD7CAbcd550aB4e2078D9F04033", "0xaaac9e8e4b509de2ff2ca107aa4bbe4882ba0c9a8b255874868acf11821764bb", BigInteger.valueOf(10)).send();
            if (personalUnlockAccount.accountUnlocked()) {
                String methodName = "transfer";
                List<Type> inputParameters = new ArrayList<>();
                List<TypeReference<?>> outputParameters = new ArrayList<>();

                Address tAddress = new Address("0x9bDe3A309707FbD7CAbcd550aB4e2078D9F04033");

                Uint256 value = new Uint256(new BigInteger("100000000000000000"));
                inputParameters.add(tAddress);
                inputParameters.add(value);

                TypeReference<Bool> typeReference = new TypeReference<Bool>() {
                };
                outputParameters.add(typeReference);

                Function function = new Function(methodName, inputParameters, outputParameters);

                String data = FunctionEncoder.encode(function);

                EthGetTransactionCount ethGetTransactionCount = Web3JUtils.initWeb3j()
                        .ethGetTransactionCount("0x9bDe3A309707FbD7CAbcd550aB4e2078D9F04033", DefaultBlockParameterName.PENDING).sendAsync().get();
                BigInteger nonce = ethGetTransactionCount.getTransactionCount();
                BigInteger gasPrice = Convert.toWei(BigDecimal.valueOf(5), Convert.Unit.GWEI).toBigInteger();

                Transaction transaction = Transaction.createFunctionCallTransaction("0x9bDe3A309707FbD7CAbcd550aB4e2078D9F04033", nonce, gasPrice,
                        BigInteger.valueOf(60000), contractService.findAllContract().getAddress(), data);

                EthSendTransaction ethSendTransaction = Web3JUtils.initWeb3j().ethSendTransaction(transaction).sendAsync().get();
                txHash = ethSendTransaction.getTransactionHash();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
