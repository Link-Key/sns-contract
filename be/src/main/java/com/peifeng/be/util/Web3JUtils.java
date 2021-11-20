package com.peifeng.be.util;

import com.peifeng.be.contract.FreeEagle;
import org.bitcoinj.crypto.MnemonicException;
import org.springframework.util.StringUtils;
import org.web3j.crypto.ECKeyPair;
import org.web3j.utils.Numeric;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/5/24 15:41
 */
public class Web3JUtils {
    public static boolean isETHValidAddress(String input) {
        if (StringUtils.isEmpty(input) || !input.startsWith("0x")) {
            return false;
        }
        return isValidAddress(input);
    }

    public static boolean isValidAddress(String input) {
        String cleanInput = Numeric.cleanHexPrefix(input);

        try {
            Numeric.toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }

        return cleanInput.length() == 40;
    }

    public static boolean isPrivateKey(String input){
        String regex="^0[xX][0-9a-fA-F]{64}$";
        if(input.matches(regex)){
            return true;
        }else{
            return false;
        }
    }


    public static Web3j initWeb3j(){
        return Web3j.build(new HttpService("https://data-seed-prebsc-1-s3.binance.org:8545/"));
        //return Web3j.build(new HttpService("https://bsc-dataseed.binance.org/"));

        //return Web3j.build(new HttpService("https://rpc-mainnet.matic.network"));

    }


    public static FreeEagle getContract(String privateKey, String contractAddress) {
        try {
            if (privateKey.isEmpty()) {
                List<String> strs = new ArrayList<>();
                strs.add("dress");
                strs.add("van");
                strs.add("seminar");
                strs.add("echo");
                strs.add("subject");
                strs.add("capable");
                strs.add("assume");
                strs.add("volcano");
                strs.add("pumpkin");
                strs.add("couch");
                strs.add("lend");
                strs.add("leaf");
                ECKeyPair keyPair = new Wallet().createWallet(strs);
                privateKey = "0x" + keyPair.getPrivateKey().toString(16);
            }
        }catch (MnemonicException.MnemonicLengthException e) {
            e.printStackTrace();
        }
        Credentials credentials = Credentials.create(privateKey);
        BigInteger gasPrice = null;
        try {
            gasPrice = initWeb3j().ethGasPrice().send().getGasPrice();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return FreeEagle.load(contractAddress, initWeb3j(), credentials, gasPrice,new BigInteger("3000000"));
    }

    public static void main(String[] args) throws MnemonicException.MnemonicLengthException {
        List<String> strs = new ArrayList<>();
        strs.add("dress");
        strs.add("van");
        strs.add("seminar");
        strs.add("echo");
        strs.add("subject");
        strs.add("capable");
        strs.add("assume");
        strs.add("volcano");
        strs.add("pumpkin");
        strs.add("couch");
        strs.add("lend");
        strs.add("leaf");
        ECKeyPair keyPair = new Wallet().createWallet(strs);
        String privateKey = "0x" + keyPair.getPrivateKey().toString(16);
        System.out.println(privateKey);
        System.out.println((3+5)+"");
        boolean a = false;
        int num = 0;
        for(int i = 0; i<7;i++){

            if(!a){
                num = i;
                break;
            }
        }
        System.out.println(num);
    }

}
