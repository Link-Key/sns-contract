// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

import "./Resolver.sol";


contract SNSResolver is Resolver {

    struct Record {
        address owner;
        //地址
        string ethAddress;
        string btcAddress;
        string ltcAddress;
        string dogeAddress;
        //ipfs
        string ipfsUrl;
        //文件记录
        string url;
        string email;
        string avatar;
        string description;
        string notice;
        string keywords;
        string comGithub;
        string comReddit;
        string comTwitter;
        string orgTelegram;
    }

    mapping(string => Record) private records;

    modifier authorised(string memory name) {
        address owner = records[name].owner;
        require(owner == msg.sender);
        _;
    }

    modifier onlySNS(address snsAddress_) {
        require(snsAddress_ == _snsAddress);
        _;
    }

    address private _snsAddress;

    /**
     * @dev Constructs a new SNS.
     */
    constructor(address snsAddress_) public {
        _snsAddress = snsAddress_;
    }


    function setRecords(string memory name_, address owner_) public onlySNS(msg.sender) {
        records[name_].owner = owner_;
        emit OwnerChanged(name_, owner_);
    }

    function getEthAddress(string memory name_) external view returns (string memory) {
        return records[name_].ethAddress;
    }

    function setEthAddress(string memory name_, string memory ethAddress_) public authorised(name_) {
        records[name_].ethAddress = ethAddress_;
        emit ContentChanged(name_, "ethAddress", ethAddress_);
    }

    function getBtcAddress(string memory name_) external view returns (string memory){
        return records[name_].btcAddress;
    }

    function setBtcAddress(string memory name_, string memory btcAddress_) external authorised(name_) {
        records[name_].btcAddress = btcAddress_;
        emit ContentChanged(name_, "btcAddress", btcAddress_);
    }

    function getLtcAddress(string memory name_) external view returns (string memory){
        return records[name_].ltcAddress;
    }

    function setLtcAddress(string memory name_, string memory ltcAddress_) external authorised(name_) {
        records[name_].ltcAddress = ltcAddress_;
        emit ContentChanged(name_, "ltcAddress", ltcAddress_);
    }

    function getDogeAddress(string memory name_) external view returns (string memory){
        return records[name_].dogeAddress;
    }

    function setDogeAddress(string memory name_, string memory dogeAddress_) external authorised(name_) {
        records[name_].dogeAddress = dogeAddress_;
        emit ContentChanged(name_, "dogeAddress", dogeAddress_);
    }

    function getIpfsUrl(string memory name_) external view returns (string memory){
        return records[name_].ipfsUrl;
    }

    function setIpfsUrl(string memory name_, string memory ipfsUrl_) external authorised(name_) {
        records[name_].ipfsUrl = ipfsUrl_;
        emit ContentChanged(name_, "ipfsUrl", ipfsUrl_);
    }

    function getUrl(string memory name_) external view returns (string memory){
        return records[name_].url;
    }

    function setUrl(string memory name_, string memory url_) external authorised(name_) {
        records[name_].url = url_;
        emit ContentChanged(name_, "url", url_);
    }

    function getEmail(string memory name_) external view returns (string memory){
        return records[name_].email;
    }

    function setEmail(string memory name_, string memory email_) external authorised(name_) {
        records[name_].email = email_;
        emit ContentChanged(name_, "email", email_);
    }

    function getAvatar(string memory name_) external view returns (string memory){
        return records[name_].avatar;
    }

    function setAvatar(string memory name_, string memory avatar_) external authorised(name_) {
        records[name_].avatar = avatar_;
        emit ContentChanged(name_, "avatar", avatar_);
    }

    function getDescription(string memory name_) external view returns (string memory){
        return records[name_].description;
    }

    function setDescription(string memory name_, string memory description_) external authorised(name_) {
        records[name_].description = description_;
        emit ContentChanged(name_, "description", description_);
    }

    function getNotice(string memory name_) external view returns (string memory){
        return records[name_].notice;
    }

    function setNotice(string memory name_, string memory notice_) external authorised(name_) {
        records[name_].notice = notice_;
        emit ContentChanged(name_, "notice", notice_);
    }

    function getKeywords(string memory name_) external view returns (string memory){
        return records[name_].keywords;
    }

    function setKeywords(string memory name_, string memory keywords_) external authorised(name_) {
        records[name_].keywords = keywords_;
        emit ContentChanged(name_, "keywords", keywords_);
    }

    function getComGithub(string memory name_) external view returns (string memory){
        return records[name_].comGithub;
    }

    function setComGithub(string memory name_, string memory comGithub_) external authorised(name_) {
        records[name_].comGithub = comGithub_;
        emit ContentChanged(name_, "comGithub", comGithub_);
    }

    function getComReddit(string memory name_) external view returns (string memory){
        return records[name_].comReddit;
    }

    function setComReddit(string memory name_, string memory comReddit_) external authorised(name_) {
        records[name_].comReddit = comReddit_;
        emit ContentChanged(name_, "comReddit", comReddit_);
    }

    function getComTwitter(string memory name_) external view returns (string memory){
        return records[name_].comTwitter;
    }

    function setComTwitter(string memory name_, string memory comTwitter_) external authorised(name_) {
        records[name_].comTwitter = comTwitter_;
        emit ContentChanged(name_, "comTwitter", comTwitter_);
    }

    function getOrgTelegram(string memory name_) external view returns (string memory){
        return records[name_].orgTelegram;
    }

    function setOrgTelegram(string memory name_, string memory orgTelegram_) external authorised(name_) {
        records[name_].orgTelegram = orgTelegram_;
        emit ContentChanged(name_, "orgTelegram", orgTelegram_);
    }

    function getAllProperties(string memory name_) external view returns (string memory, string memory, string memory, string memory, string memory, string memory, string memory, string memory, string memory, string memory, string memory, string memory, string memory, string memory, string memory){
        return
        (records[name_].ethAddress,
        records[name_].btcAddress,
        records[name_].ltcAddress,
        records[name_].dogeAddress,
        records[name_].ipfsUrl,
        records[name_].url,
        records[name_].email,
        records[name_].avatar,
        records[name_].description,
        records[name_].notice,
        records[name_].keywords,
        records[name_].comGithub,
        records[name_].comReddit,
        records[name_].comTwitter,
        records[name_].orgTelegram);
    }

}