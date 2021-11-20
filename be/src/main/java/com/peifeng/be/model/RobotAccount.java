package com.peifeng.be.model;

public class RobotAccount {
    private Integer id;

    private String mnemonicCode;

    private String address;

    private String privateKey;

    private String publicKey;

    private Boolean lssued;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMnemonicCode() {
        return mnemonicCode;
    }

    public void setMnemonicCode(String mnemonicCode) {
        this.mnemonicCode = mnemonicCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Boolean getLssued() {
        return lssued;
    }

    public void setLssued(Boolean lssued) {
        this.lssued = lssued;
    }
}